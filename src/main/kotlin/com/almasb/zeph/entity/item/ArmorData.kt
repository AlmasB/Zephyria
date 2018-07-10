package com.almasb.zeph.entity.item

import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.Description

@DataDSL
class ArmorDataBuilder(
        val description: Description = Description(),
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var armorType: ArmorType = ArmorType.BODY,
        var armor: Int = 0,
        var marmor: Int = 0
) {

    fun build(): ArmorData {
        return ArmorData(description, itemLevel, element, runes, essences, armorType, armor, marmor)
    }
}

@DataDSL
class WeaponDataBuilder(
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var type: WeaponType = WeaponType.DAGGER,
        var pureDamage: Int = 0
) {

    fun build(): WeaponData {
        return WeaponData(itemLevel, element, runes, essences, type, pureDamage)
    }
}

@DataDSL
fun armor(setup: ArmorDataBuilder.() -> Unit): ArmorData {
    val builder = ArmorDataBuilder()
    builder.setup()
    return builder.build()
}

@DataDSL
fun weapon(setup: WeaponDataBuilder.() -> Unit): WeaponData {
    val builder = WeaponDataBuilder()
    builder.setup()
    return builder.build()
}

data class ArmorData(
        val description: Description,
        val itemLevel: ItemLevel,
        val element: Element,
        val runes: List<Rune>,
        val essences: List<Essence>,
        val armorType: ArmorType,
        val armor: Int,
        val marmor: Int
)

data class WeaponData(
        val itemLevel: ItemLevel,
        val element: Element,
        val runes: List<Rune>,
        val essences: List<Essence>,
        val type: WeaponType,
        val pureDamage: Int
)