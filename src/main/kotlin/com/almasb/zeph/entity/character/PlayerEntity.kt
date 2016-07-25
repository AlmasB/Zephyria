package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.character.component.CharacterDataComponent
import com.almasb.zeph.entity.character.component.MoneyComponent
import com.almasb.zeph.entity.character.control.PlayerControl
import com.almasb.zeph.entity.item.WeaponEntity
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerEntity(name: String, textureName: String) : CharacterEntity(listOf<Component>(
        DescriptionComponent(1, name, "It's you! $name", textureName),
        CharacterDataComponent(CharacterType.NORMAL)
)) {

    val money = MoneyComponent(9999)

    val attributePoints = SimpleIntegerProperty(90)
    val skillPoints = SimpleIntegerProperty(90)

    val statLevel = SimpleIntegerProperty(1)
    val jobLevel = SimpleIntegerProperty(1)

    val playerControl: PlayerControl

    init {
        charClass.value = CharacterClass.NOVICE

        addComponent(money)

        playerControl = PlayerControl()

        addControl(playerControl)
        //addControl(PlayerActionControl())

        playerControl.equipProperty(EquipPlace.RIGHT_HAND).addListener({ o, old, newWeapon ->
            weapon.value = newWeapon as WeaponEntity
        })
    }
}