package com.almasb.zeph.entity.item.component

import com.almasb.zeph.entity.item.component.EquippableComponent
import com.almasb.zeph.entity.item.ArmorType
import com.almasb.zeph.entity.item.ItemLevel

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ArmorDataComponent(itemLevel: ItemLevel, val armorType: ArmorType, val armor: Int, val marmor: Int) : EquippableComponent(itemLevel) {

    fun armorRating(): Int {
        return armor + refineLevel * if (refineLevel > 2) itemLevel.bonus + 1 else itemLevel.bonus
    }

    fun marmorRating(): Int {
        return marmor + refineLevel * if (refineLevel > 2) itemLevel.bonus + 1 else itemLevel.bonus
    }
}