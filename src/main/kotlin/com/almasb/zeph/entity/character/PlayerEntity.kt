package com.almasb.zeph.entity.character

import com.almasb.ents.Entity
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.entity.Data
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.character.control.PlayerControl
import com.almasb.zeph.entity.item.WeaponEntity
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerEntity : CharacterEntity() {

    val money = MoneyComponent(9999)

    val attributePoints = SimpleIntegerProperty(90)
    val skillPoints = SimpleIntegerProperty(90)

    val statLevel = SimpleIntegerProperty(1)
    val jobLevel = SimpleIntegerProperty(1)

    init {
        addComponent(money)
    }

    fun getControl() = getControlUnsafe(PlayerControl::class.java)
}