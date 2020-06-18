package com.almasb.zeph.events

import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.events.Events.ON_ATTACK
import javafx.event.Event
import javafx.event.EventType

/**
 * Defines a generic game event.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
sealed class GameEvent(eventType: EventType<out GameEvent>) : Event(eventType)

/**
 * Stores all game event types.
 */
object Events {
    val ANY = EventType<GameEvent>(Event.ANY)
    val ON_ATTACK = EventType<OnAttackEvent>(ANY)
}

class OnAttackEvent(
        val attacker: CharacterEntity,
        val target: CharacterEntity
) : GameEvent(ON_ATTACK)