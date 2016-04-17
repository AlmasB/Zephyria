package com.almasb.zeph.entity

import com.almasb.ents.Component
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.item.ArmorType
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.WeaponType
import com.almasb.zeph.entity.item.component.ArmorDataComponent
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
                WeaponDataComponent(ItemLevel.NORMAL, WeaponType.MACE, 0)
        )

        fun GUT_RIPPER() = listOf<Component>(
                DescriptionComponent(4003, "The Gut Ripper", "A fierce weapon that punctures and ruptures enemies with vicious and lightning fast blows", "items/weapons/gut_ripper.png"),
                WeaponDataComponent(ItemLevel.EPIC, WeaponType.DAGGER, 100)
                        .withRune(Rune(Attribute.AGILITY, 4))
                        .withRune(Rune(Attribute.DEXTERITY, 4))
                        .withRune(Rune(Attribute.LUCK, 1))
        )
    }

    object Armor {
        fun HAT() = listOf<Component>(
                DescriptionComponent(5000, "Hat", "Ordinary hat, already out of fashion", "items/armor/hat.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.HELM, 0, 0)
        )

        fun CLOTHES() = listOf<Component>(
                DescriptionComponent(5001, "Clothes", "Just normal clothes, don't count on any defense", "items/armor/clothes.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.BODY, 0, 0)
        )

        fun SHOES() = listOf<Component>(
                DescriptionComponent(5002, "Shoes", "Average size shoes", "items/armor/shoes.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.SHOES, 0, 0)
        )
    }
}