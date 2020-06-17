package com.almasb.zeph.character

import com.almasb.zeph.character.components.PlayerComponent

/**
 * PlayerComponent and CharacterComponent both are the same instance for this entity.
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
}, PlayerComponent(char {
    desc {
        id = 1
        name = playerName
        description = "It's you! $name"
        textureName = playerTextureName
    }
})) {

    val playerComponent = characterComponent as PlayerComponent

    val name = data.description.name

    val charClass = playerComponent.charClass

    val statLevel = playerComponent.statLevel
    val jobLevel = playerComponent.jobLevel

    val baseXP = playerComponent.baseXP
    val statXP = playerComponent.statXP
    val jobXP = playerComponent.jobXP

    val inventory = playerComponent.inventory

    fun getEquip(place: EquipPlace) = playerComponent.getEquip(place)

    fun equipProperty(place: EquipPlace) = playerComponent.equipProperty(place)
}