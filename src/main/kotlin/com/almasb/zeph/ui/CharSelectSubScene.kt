package com.almasb.zeph.ui

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.Vec2
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.scene.SubScene
import javafx.animation.FillTransition
import javafx.animation.ParallelTransition
import javafx.animation.RotateTransition
import javafx.animation.TranslateTransition
import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Parent
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Glow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Rotate
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharSelectSubScene : SubScene() {

    init {
        val circleRoot = Pane()

        val bg = Rectangle(getAppWidth() / 1.5, getAppHeight() / 1.5)
        bg.strokeWidth = 2.0
        bg.stroke = Color.LIGHTBLUE

        val circle = Circle(bg.width / 2.0, bg.height / 2.0, 250.0, null)
        circle.stroke = Color.WHITE
        circle.strokeWidth = 2.5

        for (angle in 0..359 step 10) {
            val v = Vec2.fromAngle(angle.toDouble()).toPoint2D()

            val pt = Point2D(circle.centerX, circle.centerY).add(v.multiply(circle.radius))

            val line = Line(pt.x, pt.y, pt.x + -v.x * 20, pt.y + -v.y * 20)
            line.stroke = circle.stroke

            circleRoot.children += line
        }

        addChild(bg)



        contentRoot.translateX = getAppWidth() / 2.0 - bg.width / 2.0
        contentRoot.translateY = getAppHeight() / 2.0 - bg.height / 2.0

        val vector = Point2D(0.0, 1.0).multiply(circle.radius)

        val firstPoint = Point2D(circle.centerX, circle.centerY).add(vector)



        circleRoot.children += circle
        circleRoot.children += Card(Color.BLUE).also {
            it.translateX = firstPoint.x - 120.0
            it.translateY = firstPoint.y - 150.0
        }

        // TODO: 120 is hardcoded = 360.0 / num_items
        val secondPoint = Point2D(circle.centerX, circle.centerY).add(
                Vec2.fromAngle(90.0 + 120).toPoint2D().multiply(circle.radius)
        )

        circleRoot.children += Card(Color.RED).also { card ->
            card.translateX = secondPoint.x - 120.0
            card.translateY = secondPoint.y - 150.0

            card.rotate = 120.0

            card.setOnMouseClicked {
                animationBuilder(this)
                        .duration(Duration.seconds(1.2))
                        .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                        .rotate(circleRoot)
                        .from(0.0)
                        .to(-card.rotate)
                        .origin(Point2D(circle.centerX, circle.centerY))
                        .buildAndPlay()
            }
        }

        // 3rd
        // TODO: 120 is hardcoded = 360.0 / num_items
        val thirdPoint = Point2D(circle.centerX, circle.centerY).add(
                Vec2.fromAngle(90.0 + 120 + 120).toPoint2D().multiply(circle.radius)
        )

        circleRoot.children += Card(Color.GREEN).also {
            it.translateX = thirdPoint.x - 120.0
            it.translateY = thirdPoint.y - 150.0

            it.rotate = 120.0 + 120.0
        }

        addChild(circleRoot)





        val switch = MaleFemaleToggleSwitch()
        switch.translateX = bg.width - 200.0
        switch.translateY = 50.0
        addChild(switch)


        val btnSelect = getUIFactoryService().newButton("Select")
        btnSelect.translateX = bg.width - 220.0
        btnSelect.translateY = bg.height - 60.0
        addChild(btnSelect)

        contentRoot.effect = DropShadow(30.0, Color.BLACK)
    }
}

private class Card(color: Color) : Parent() {

    private val glow = Glow(0.8)

    private val t1 = texture("chars/class_portraits/warrior_male.png")
    private val t2 = texture("chars/class_portraits/warrior_female.png").also { it.scaleX = -1.0 }

    private var isMale = true

    init {
        val bg = Rectangle(240.0, 300.0)
        bg.fill = color

        val tBG = Rectangle(229.0, 226.0, null)
        tBG.stroke = Color.MEDIUMAQUAMARINE

        val stack = StackPane(tBG, t1)


        children.addAll(bg, stack)

        setOnMouseEntered {
            effect = glow
        }

        setOnMouseExited {
            effect = null
        }

        setOnMouseClicked {
            isMale = !isMale

            stack.children.removeAt(1)

            val rt = RotateTransition(Duration.seconds(0.4), stack)
            rt.axis = Rotate.Y_AXIS
            rt.fromAngle = if (!isMale) 0.0 else 180.0
            rt.toAngle = if (!isMale) 180.0 else 0.0
            rt.setOnFinished { stack.children.add(if (isMale) t1 else t2) }
            rt.play()
        }

        // TODO: isSelected
//        scaleXProperty().bind(
//                Bindings.`when`(hoverProperty()).then(1.0).otherwise(0.75)
//        )
//
//        scaleYProperty().bind(
//                Bindings.`when`(hoverProperty()).then(1.0).otherwise(0.75)
//        )
    }

    private fun flip() {

    }
}


/**
 * On/Off toggle switch.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class MaleFemaleToggleSwitch : Parent() {

    private var animating = false
    private val switchedOn = ReadOnlyBooleanWrapper(false)

    fun switchedOnProperty(): ReadOnlyBooleanProperty {
        return switchedOn.readOnlyProperty
    }

    /**
     * @return true if switch is ON, false - if OFF
     */
    fun isSwitchedOn(): Boolean {
        return switchedOnProperty().get()
    }

    private val translateAnimation = TranslateTransition(Duration.seconds(0.25))
    private val fillAnimation = FillTransition(Duration.seconds(0.25))
    private val animation = ParallelTransition(translateAnimation, fillAnimation)
    private var colorOn = Color.LIGHTGREEN

    fun setFill(colorOn: Color) {
        this.colorOn = colorOn
    }

    init {
        val background = Rectangle(100.0, 50.0)
        background.fill = Color.WHITE
        background.stroke = Color.LIGHTGRAY
        background.arcWidth = 50.0
        background.arcHeight = 50.0

        val trigger = Circle(25.0)
        trigger.centerX = 25.0
        trigger.centerY = 25.0
        trigger.fill = Color.WHITE
        trigger.stroke = Color.LIGHTGRAY

        translateAnimation.node = trigger
        fillAnimation.shape = background

        val M = getUIFactoryService().newText("M")
        val F = getUIFactoryService().newText("F")

        M.translateX = -25.0
        F.translateX = background.width + 15.0

        children.addAll(background, trigger, M, F)

        switchedOnProperty().addListener { obs: ObservableValue<out Boolean>?, oldState: Boolean?, isOn: Boolean ->
            if (animating) {
                animation.stop()
            }
            animating = true
            translateAnimation.setToX(if (isOn) 100 - 50.0 else 0.toDouble())
            fillAnimation.fromValue = if (isOn) Color.WHITE else colorOn
            fillAnimation.toValue = if (isOn) colorOn else Color.WHITE
            animation.onFinished = EventHandler { e: ActionEvent? -> animating = false }
            animation.play()
        }

        onMouseClicked = EventHandler { event: MouseEvent? ->
            if (!animating)
                switchedOn.set(!isSwitchedOn())
        }
    }
}