package com.almasb.zeph.events

import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.events.Events.ON_ARMOR_EQUIPPED
import com.almasb.zeph.events.Events.ON_ATTACK
import com.almasb.zeph.events.Events.ON_BEFORE_SKILL_CAST
import com.almasb.zeph.events.Events.ON_BEING_KILLED
import com.almasb.zeph.events.Events.ON_ITEM_PICKED_UP
import com.almasb.zeph.events.Events.ON_ITEM_USED
import com.almasb.zeph.events.Events.ON_MAGICAL_DAMAGE_DEALT
import com.almasb.zeph.events.Events.ON_MONEY_RECEIVED
import com.almasb.zeph.events.Events.ON_ORDERED_MOVE
import com.almasb.zeph.events.Events.ON_PHYSICAL_DAMAGE_DEALT
import com.almasb.zeph.events.Events.ON_SKILL_LEARNED
import com.almasb.zeph.events.Events.ON_WEAPON_EQUIPPED
import com.almasb.zeph.events.Events.ON_XP_RECEIVED
import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.Weapon
import com.almasb.zeph.skill.Skill
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

    /**
     * Fired after an item was picked up and added to inventory.
     */
    val ON_ITEM_PICKED_UP = EventType<OnItemPickedUpEvent>(ANY, "ON_ITEM_PICKED_UP")

    /**
     * Fired after an item was used.
     */
    val ON_ITEM_USED = EventType<OnItemUsedEvent>(ANY, "ON_ITEM_USED")

    val ON_WEAPON_EQUIPPED = EventType<OnWeaponEquippedEvent>(ANY, "ON_WEAPON_EQUIPPED")
    val ON_ARMOR_EQUIPPED = EventType<OnArmorEquippedEvent>(ANY, "ON_ARMOR_EQUIPPED")

    val ON_XP_RECEIVED = EventType<OnXPReceivedEvent>(ANY, "ON_XP_RECEIVED")
    val ON_MONEY_RECEIVED = EventType<OnMoneyReceivedEvent>(ANY, "ON_MONEY_RECEIVED")

    val ON_SKILL_LEARNED = EventType<OnSkillLearnedEvent>(ANY, "ON_SKILL_LEARNED")

    val ON_ATTACK = EventType<OnAttackEvent>(ANY, "ON_ATTACK")

    val ON_PHYSICAL_DAMAGE_DEALT = EventType<OnPhysicalDamageDealtEvent>(ANY, "ON_PHYSICAL_DAMAGE_DEALT")
    val ON_MAGICAL_DAMAGE_DEALT = EventType<OnMagicalDamageDealtEvent>(ANY, "ON_MAGICAL_DAMAGE_DEALT")

    val ON_BEFORE_SKILL_CAST = EventType<OnBeforeSkillCastEvent>(ANY, "ON_BEFORE_SKILL_CAST")

    /**
     * Fired just before a character is killed.
     */
    val ON_BEING_KILLED = EventType<OnBeingKilledEvent>(ANY, "ON_BEING_KILLED")

    val ORDER_ANY = EventType<GameEvent>(ANY, "ORDER_ANY")

    val ON_ORDERED_MOVE = EventType<OnOrderedMoveEvent>(ORDER_ANY, "ON_ORDERED_MOVE")
}

class OnAttackEvent(
        val attacker: CharacterEntity,
        val target: CharacterEntity
) : GameEvent(ON_ATTACK)

class OnBeingKilledEvent(
        val killer: CharacterEntity,
        val killedEntity: CharacterEntity
) : GameEvent(ON_BEING_KILLED)

class OnItemPickedUpEvent(
        val user: CharacterEntity,
        val item: Entity
) : GameEvent(ON_ITEM_PICKED_UP)

class OnItemUsedEvent(
        val user: CharacterEntity,
        val item: Entity
) : GameEvent(ON_ITEM_USED)

class OnWeaponEquippedEvent(
        val user: CharacterEntity,
        val weapon: Weapon
) : GameEvent(ON_WEAPON_EQUIPPED)

class OnArmorEquippedEvent(
        val user: CharacterEntity,
        val armor: Armor
) : GameEvent(ON_ARMOR_EQUIPPED)

class OnPhysicalDamageDealtEvent(
        val attacker: CharacterEntity,
        val target: CharacterEntity,
        val damage: Int,
        val isCritical: Boolean
) : GameEvent(ON_PHYSICAL_DAMAGE_DEALT)

class OnMagicalDamageDealtEvent(
        val attacker: CharacterEntity,
        val target: CharacterEntity,
        val damage: Int,
        val isCritical: Boolean
) : GameEvent(ON_MAGICAL_DAMAGE_DEALT)

class OnMoneyReceivedEvent(
        val receiver: CharacterEntity,
        val amount: Int
) : GameEvent(ON_MONEY_RECEIVED)

class OnXPReceivedEvent(
        val receiver: CharacterEntity,
        val xp: Experience
) : GameEvent(ON_XP_RECEIVED)

class OnSkillLearnedEvent(
        val learner: CharacterEntity,
        val skill: Skill
) : GameEvent(ON_SKILL_LEARNED)

class OnBeforeSkillCastEvent(
        val caster: CharacterEntity,
        val skill: Skill
) : GameEvent(ON_BEFORE_SKILL_CAST)

class OnOrderedMoveEvent(
        val char: CharacterEntity
// TODO: where moving?
) : GameEvent(ON_ORDERED_MOVE)