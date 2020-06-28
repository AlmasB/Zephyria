package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.*
import com.almasb.zeph.emptyDescription

@DataDSL
class ArmorDataBuilder(
        var description: Description = emptyDescription,
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var armorType: ArmorType = ArmorType.BODY,
        var armor: Int = 0,
        var marmor: Int = 0,
        var onBeingHitScript: (CharacterEntity, CharacterEntity) -> Unit = { attacker, target -> },
        var onEquipScript: (CharacterEntity) -> Unit = { self -> },
        var onUnEquipScript: (CharacterEntity) -> Unit = { self -> }
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    operator fun Attribute.plus(value: Int) {
        runes += Rune(this, value)
    }

    operator fun Stat.plus(value: Int) {
        essences += Essence(this, value)
    }

    fun build(): ArmorData {
        if (description.textureName.isEmpty()) {
            val fileName = description.name.toLowerCase().replace(" ", "_") + ".png"

            description = description.copy(textureName = "items/armor/$fileName")
        }

        return ArmorData(description, itemLevel, element, runes, essences, armorType, armor, marmor, onBeingHitScript, onEquipScript, onUnEquipScript)
    }
}

@DataDSL
class WeaponDataBuilder(
        var description: Description = emptyDescription,
        var itemLevel: ItemLevel = ItemLevel.NORMAL,
        var element: Element = Element.NEUTRAL,
        val runes: MutableList<Rune> = arrayListOf(),
        val essences: MutableList<Essence> = arrayListOf(),
        var type: WeaponType = WeaponType.MACE,
        var pureDamage: Int = 0,
        var onAttackScript: (CharacterEntity, CharacterEntity) -> Unit = { attacker, target -> },
        var onEquipScript: (CharacterEntity) -> Unit = { self -> },
        var onUnEquipScript: (CharacterEntity) -> Unit = { self -> }
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    operator fun Attribute.minus(value: Int) {
        runes += Rune(this, -value)
    }

    operator fun Stat.minus(value: Int) {
        essences += Essence(this, -value)
    }

    operator fun Attribute.plus(value: Int) {
        runes += Rune(this, value)
    }

    operator fun Stat.plus(value: Int) {
        essences += Essence(this, value)
    }

    fun build(): WeaponData {
        if (description.textureName.isEmpty()) {
            val fileName = description.name.toLowerCase().replace(" ", "_") + ".png"

            description = description.copy(textureName = "items/weapons/$fileName")
        }

        return WeaponData(description, itemLevel, element, runes, essences, type, pureDamage, onAttackScript, onEquipScript, onUnEquipScript)
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
        val marmor: Int,
        val onBeingHitScript: (CharacterEntity, CharacterEntity) -> Unit,
        val onEquipScript: (CharacterEntity) -> Unit,
        val onUnEquipScript: (CharacterEntity) -> Unit
) : ItemData

data class WeaponData(
        override val description: Description,
        val itemLevel: ItemLevel,
        val element: Element,
        val runes: List<Rune>,
        val essences: List<Essence>,
        val type: WeaponType,
        val pureDamage: Int,
        val onAttackScript: (CharacterEntity, CharacterEntity) -> Unit,
        val onEquipScript: (CharacterEntity) -> Unit,
        val onUnEquipScript: (CharacterEntity) -> Unit
) : ItemData