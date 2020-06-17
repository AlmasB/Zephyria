package com.almasb.zeph.item

import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder

@DataDSL
class ArmorDataBuilder(
        var description: Description = Description(),
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var armorType: ArmorType = ArmorType.BODY,
        var armor: Int = 0,
        var marmor: Int = 0
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): ArmorData {
        return ArmorData(description, itemLevel, element, runes, essences, armorType, armor, marmor)
    }
}

@DataDSL
class WeaponDataBuilder(
        var description: Description = Description(),
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var type: WeaponType = WeaponType.MACE,
        var pureDamage: Int = 0
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): WeaponData {
        return WeaponData(description, itemLevel, element, runes, essences, type, pureDamage)
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
        override val description: Description,
        val itemLevel: ItemLevel,
        val element: Element,
        val runes: List<Rune>,
        val essences: List<Essence>,
        val armorType: ArmorType,
        val armor: Int,
        val marmor: Int
) : ItemData

data class WeaponData(
        override val description: Description,
        val itemLevel: ItemLevel,
        val element: Element,
        val runes: List<Rune>,
        val essences: List<Essence>,
        val type: WeaponType,
        val pureDamage: Int
) : ItemData