package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.entity.Data
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.character.control.PlayerControl
import com.almasb.zeph.entity.item.WeaponEntity
import com.almasb.zeph.entity.skill.SkillEntity
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import java.util.*

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

    init {
        data.charClass.value = GameCharacterClass.NOVICE

        addComponent(money)
        addControl(PlayerControl())
    }

    fun getControl() = getControlUnsafe(PlayerControl::class.java)
}