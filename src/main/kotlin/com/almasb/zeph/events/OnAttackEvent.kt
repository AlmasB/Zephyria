package com.almasb.zeph.events

import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.events.Events.ON_ATTACK
import com.almasb.zeph.events.Events.ON_BEING_KILLED
import com.almasb.zeph.events.Events.ON_ITEM_USED
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

    val ON_ITEM_USED = EventType<OnItemUsedEvent>(ANY, "ON_ITEM_USED")

    val ON_ATTACK = EventType<OnAttackEvent>(ANY, "ON_ATTACK")
    val ON_BEING_KILLED = EventType<OnBeingKilledEvent>(ANY, "ON_BEING_KILLED")
}

class OnAttackEvent(
        val attacker: CharacterEntity,
        val target: CharacterEntity
) : GameEvent(ON_ATTACK)

class OnBeingKilledEvent(
        val killer: CharacterEntity,
        val killedEntity: CharacterEntity
) : GameEvent(ON_BEING_KILLED)

class OnItemUsedEvent(
        val user: CharacterEntity,
        val item: Entity
) : GameEvent(ON_ITEM_USED)