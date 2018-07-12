package com.almasb.zeph.character

import com.almasb.zeph.character.components.CharacterComponent
import com.almasb.zeph.character.components.PlayerComponent
import com.almasb.zeph.item.Item

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

    val statLevel = playerComponent.statLevel
    val jobLevel = playerComponent.jobLevel

    val baseXP = playerComponent.baseXP
    val statXP = playerComponent.statXP
    val jobXP = playerComponent.jobXP

    fun getEquip(place: EquipPlace) = playerComponent.getEquip(place)

    fun equipProperty(place: EquipPlace) = playerComponent.equipProperty(place)
}