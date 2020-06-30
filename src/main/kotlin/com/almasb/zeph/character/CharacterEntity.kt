package com.almasb.zeph.character

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.pathfinding.CellMoveComponent
import com.almasb.zeph.EntityType
import com.almasb.zeph.character.components.*
import com.almasb.zeph.combat.*
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent

/**
 * This is a convenience class ONLY and DOES NOT have any logic.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterEntity : Entity() {

    /* COMPONENTS BEGIN */

    val playerComponent: PlayerComponent?
        get() = getComponentOptional(PlayerComponent::class.java).orElse(null)

    val characterComponent: CharacterComponent
        get() = getComponent(CharacterComponent::class.java)

    val animationComponent: AnimationComponent
        get() = getComponent(AnimationComponent::class.java)

    val actionComponent: CharacterActionComponent
        get() = getComponent(CharacterActionComponent::class.java)

    val effectComponent: CharacterEffectComponent
        get() = getComponent(CharacterEffectComponent::class.java)

    /* COMPONENTS END */

    val isPlayer get() = isType(EntityType.PLAYER)

    val data get() = characterComponent.data

    val name get() = characterComponent.data.description.name

    val charClass get() = characterComponent.charClass

    val hp get() = characterComponent.hp

    val sp get() = characterComponent.sp

    val baseLevel get() = characterComponent.baseLevel
    val statLevel get() = characterComponent.statLevel
    val jobLevel get() = characterComponent.jobLevel

    val baseXP get() = characterComponent.baseXP
    val attrXP get() = characterComponent.attrXP
    val jobXP get() = characterComponent.jobXP

    val inventory get() = characterComponent.inventory

    val weaponElement get() = characterComponent.weaponElement
    val armorElement get() = characterComponent.armorElement

//    fun getEquip(place: EquipPlace) = playerComponent.getEquip(place)
//
//    fun equipProperty(place: EquipPlace) = playerComponent.equipProperty(place)

    val cellX: Int
        get() = getComponent(CellMoveComponent::class.java).cellX

    val cellY: Int
        get() = getComponent(CellMoveComponent::class.java).cellY

    fun distance(cellX: Int, cellY: Int): Int {
        return Math.abs(this.cellX - cellX) + Math.abs(this.cellY - cellY)
    }

    fun setPositionToCell(cellX: Int, cellY: Int) = getComponent(NewAStarMoveComponent::class.java).stopMovementAt(cellX, cellY)

    fun orderMove(cellX: Int, cellY: Int) = actionComponent.orderMove(cellX, cellY)

    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double, element: Element = Element.NEUTRAL): DamageResult {
        return characterComponent.dealPhysicalDamage(target, baseDamage, element)
    }

    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double, element: Element = Element.NEUTRAL): DamageResult {
        return characterComponent.dealMagicalDamage(target, baseDamage, element)
    }

    fun hasStatus(status: Status) = effectComponent.hasStatus(status)

    fun addEffect(effect: Effect) = effectComponent.addEffect(effect)
    fun removeEffect(effect: Effect) = effectComponent.removeEffect(effect)

    fun addBonus(attribute: Attribute, value: Int) {
        characterComponent.addBonus(attribute, value)
    }

    fun addBonus(stat: Stat, value: Int) {
        characterComponent.addBonus(stat, value)
    }

    fun getTotal(attribute: Attribute): Int = characterComponent.getTotal(attribute)
    fun getTotal(stat: Stat): Int = characterComponent.getTotal(stat)
}