package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.character.component.CharacterDataComponent
import com.almasb.zeph.entity.character.component.MoneyComponent
import com.almasb.zeph.entity.character.control.PlayerControl
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

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
        charClass.value = CharacterClass.NOVICE

        addComponent(money)
        addControl(PlayerControl())
    }

    fun getControl() = getControlUnsafe(PlayerControl::class.java)
}