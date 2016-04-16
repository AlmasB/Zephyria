package com.almasb.zeph.entity.item

import com.almasb.ents.Entity
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.item.component.WeaponDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponEntity : Entity() {

    val data = WeaponDataComponent(ItemLevel.NORMAL, WeaponType.MACE, 10)

    init {
        addComponent(DescriptionComponent(99, "Test", "TestDescription", "items/weapons/hands.png"))
        addComponent(data)

        data.addRune(Rune(Attribute.STRENGTH, 5))
        data.addRune(Rune(Attribute.VITALITY, 15))
    }
}
