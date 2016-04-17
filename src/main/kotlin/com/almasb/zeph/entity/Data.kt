package com.almasb.zeph.entity

import com.almasb.ents.Component
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.WeaponType
import com.almasb.zeph.entity.item.component.WeaponDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    object Weapon {
        fun HANDS() = listOf<Component>(
                DescriptionComponent(4000, "Hands", "That's right, go kill everyone with your bare hands", "items/weapons/hands.png"),
                WeaponDataComponent(ItemLevel.NORMAL, WeaponType.MACE, 10)
        )
    }
}