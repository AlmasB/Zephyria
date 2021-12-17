package com.almasb.zeph.gameplay

import com.almasb.fxgl.core.Updatable
import com.almasb.fxgl.dsl.getUIFactoryService
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.text.Text
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Represents an in-game clock for gameplay purposes, such as day/night systems.
 * For example, certain quests appear at a certain time of day, or some NPCs may only be around at night.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Clock(

        /**
         * Number of real-time seconds in 24 in-game hours.
         */
        secondsIn24h: Int) : Updatable {

    private var seconds = 0.0

    private val gameDayProp = ReadOnlyIntegerWrapper()
    private val gameHourProp = ReadOnlyIntegerWrapper()
    private val gameMinuteProp = ReadOnlyIntegerWrapper()

    fun gameDayProperty(): ReadOnlyIntegerProperty {
        return gameDayProp.readOnlyProperty
    }

    fun gameHourProperty(): ReadOnlyIntegerProperty {
        return gameHourProp.readOnlyProperty
    }

    fun gameMinuteProperty(): ReadOnlyIntegerProperty {
        return gameMinuteProp.readOnlyProperty
    }

    /**
     * The range is [0..MAX_INT]
     */
    val gameDay: Int
        get() = gameDayProp.value

    /**
     * The range is [0..23].
     */
    val gameHour: Int
        get() = gameHourProp.value

    /**
     * The range is [0..59].
     */
    val gameMinute: Int
        get() = gameMinuteProp.value

    /**
     * The range is [0, 59].
     */
    var gameSecond = 0
        private set

    /**
     * 24 * 3600 / real seconds in 24h.
     */
    private val toGameSeconds = 86400.0 / secondsIn24h

    var dayTimeStart = 8
    var nightTimeStart = 20

    private val isDayTime = ReadOnlyBooleanWrapper(false)

    fun dayProperty(): ReadOnlyBooleanProperty {
        return isDayTime.readOnlyProperty
    }

    val isDay: Boolean
        get() = isDayTime.value

    val isNight: Boolean
        get() = !isDay

    private val actions = CopyOnWriteArrayList<ClockAction>()

    private fun updateActions() {
        actions.filter { it.dayToRun == gameDay && (gameHour > it.hour || (gameHour == it.hour && gameMinute >= it.minute)) }
                .forEach {
                    if (!it.isIndefinite) {
                        it.isDone = true
                    } else {
                        it.dayToRun++
                    }

                    it.action.run()
                }

        actions.removeIf { it.isDone }
    }

    /**
     * Runs [action] once when at the clock [hour] or past it.
     */
    fun runOnceAtHour(hour: Int, action: Runnable) {
        runAt(hour, minute = 0, action)
    }

    /**
     * Runs [action] indefinitely when at the clock [hour]:[minute] or past it.
     */
    fun runAt(hour: Int, minute: Int, action: Runnable) {
        actions.add(ClockAction(hour, minute, action, isIndefinite = true))
    }

    /**
     * Runs [action] once when at the clock [hour]:[minute] or past it.
     */
    fun runOnceAt(hour: Int, minute: Int, action: Runnable) {
        actions.add(ClockAction(hour, minute, action))
    }

    override fun onUpdate(tpf: Double) {
        seconds += tpf

        var totalGameSeconds = seconds * toGameSeconds

        if (totalGameSeconds > 86400) {
            seconds = 0.0
            totalGameSeconds = 0.0

            // new day
            gameDayProp.value++
        }

        gameHourProp.value = totalGameSeconds.toInt() / 3600
        gameMinuteProp.value = (totalGameSeconds.toInt() / 60) % 60
        gameSecond = totalGameSeconds.toInt() % 60

        isDayTime.value = gameHourProp.value in dayTimeStart until nightTimeStart

        updateActions()
    }

    private data class ClockAction(
            var hour: Int,
            var minute: Int,
            var action: Runnable,
            var isIndefinite: Boolean = false,
            var isDone: Boolean = false,

            /**
             * Keeps track of the day on which to run next.
             */
            var dayToRun: Int = 0
    )
}

abstract class ClockView(val clock: Clock) : Parent()

/**
 * Basic clock view in hh:mm format
 */
open class TextClockView(clock: Clock) : ClockView(clock) {

    private val text = getUIFactoryService().newText("", Color.BLACK, 16.0)

    init {
        children += text

        clock.gameHourProperty().addListener { observable, oldValue, newValue ->
            updateView()
        }

        clock.gameMinuteProperty().addListener { observable, oldValue, newValue ->
            updateView()
        }
    }

    private fun updateView() {
        val hour = "${clock.gameHourProperty().value}".padStart(2, '0')
        val min = "${clock.gameMinuteProperty().value}".padStart(2, '0')

        text.text = "$hour:$min"
    }
}