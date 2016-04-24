package com.almasb.zeph.entity.item.component

import com.almasb.ents.Entity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.entity.character.component.StatsComponent
import com.almasb.zeph.entity.item.component.EquippableComponent
import com.almasb.zeph.entity.item.ArmorType
import com.almasb.zeph.entity.item.ItemLevel

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ArmorDataComponent(itemLevel: ItemLevel, val armorType: ArmorType, val armor: Int, val marmor: Int) : EquippableComponent(itemLevel) {

    fun withRune(rune: Rune): ArmorDataComponent {
        addRune(rune)
        return this
    }

    fun withElement(element: Element): ArmorDataComponent {
        this.element = element
        return this
    }
}