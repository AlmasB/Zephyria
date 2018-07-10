package com.almasb.zeph.character

import com.almasb.zeph.character.components.CharacterComponent
import com.almasb.zeph.character.components.PlayerComponent
import com.almasb.zeph.item.Item

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerEntity(playerName: String, playerTextureName: String) : CharacterEntity(char {
    desc {
        id = 1
        name = playerName
        description = "It's you! $name"
        textureName = playerTextureName
    }
}) {

    val playerComponent = PlayerComponent(super.data)

    init {
        removeComponent(CharacterComponent::class.java)

        addComponent(playerComponent)
    }

    fun getEquip(place: EquipPlace) = playerComponent.getEquip(place)

    fun equipProperty(place: EquipPlace) = playerComponent.equipProperty(place)
}