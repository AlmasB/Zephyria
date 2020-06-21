package com.almasb.zeph.character

import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.EntityType
import com.almasb.zeph.character.components.AnimationComponent
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.character.components.CharacterComponent
import com.almasb.zeph.character.components.PlayerComponent
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.entity.character.component.NewCellMoveComponent

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

    /* COMPONENTS END */

    val isPlayer get() = isType(EntityType.PLAYER)

    val data get() = characterComponent.data

    val name get() = characterComponent.data.description.name

    val charClass get() = characterComponent.charClass

    val attributes get() = characterComponent.attributes

    val stats get() = characterComponent.stats

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

    fun setPositionToCell(cellX: Int, cellY: Int) = getComponent(NewAStarMoveComponent::class.java).stopMovementAt(cellX, cellY)

    fun moveToCell(cellX: Int, cellY: Int) = actionComponent.orderMove(cellX, cellY)
}