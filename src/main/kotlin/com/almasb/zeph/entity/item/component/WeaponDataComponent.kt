package com.almasb.zeph.entity.item.component

import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.WeaponType

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponDataComponent(itemLevel: ItemLevel, val type: WeaponType, val pureDamage: Int) : EquippableComponent(itemLevel) {

    fun withRune(rune: Rune): WeaponDataComponent {
        addRune(rune)
        return this
    }

    fun withElement(element: Element): WeaponDataComponent {
        this.element = element
        return this
    }
}