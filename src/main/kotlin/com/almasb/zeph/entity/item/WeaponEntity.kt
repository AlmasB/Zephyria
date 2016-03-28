package com.almasb.zeph.entity.item

import com.almasb.ents.Entity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.item.component.WeaponDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponEntity : Entity() {

    init {
        addComponent(DescriptionComponent(99, "Test", "TestDescription", "items/weapons/hands.png"))
        addComponent(WeaponDataComponent(ItemLevel.NORMAL, WeaponType.MACE, 10))
    }
}
