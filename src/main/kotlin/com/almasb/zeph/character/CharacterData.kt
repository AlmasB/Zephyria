package com.almasb.zeph.character

import com.almasb.zeph.combat.*
import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder

@DslMarker
annotation class DataDSL

@DataDSL
class CharacterDataBuilder(
        var description: Description = Description(),
        var type: CharacterType = CharacterType.NORMAL,
        var charClass: CharacterClass = CharacterClass.MONSTER,
        var baseLevel: Int = 1,
        val attributes: Attribute.AttributeInfo = Attribute.AttributeInfo(),
        //val defaultWeapon: Weapon = WeaponEntity(Data.Weapon.HANDS())
        var element: Element = Element.NEUTRAL,
        var rewardXP: Experience = Experience(0, 0, 0),

        /**
         * Item ID -> chance to drop
         */
        val dropItems: MutableList<Pair<Int, Int>> = arrayListOf()
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun attributes(setup: Attribute.AttributeInfo.() -> Unit) {
        attributes.setup()
    }

    infix fun Int.has(dropChance: Int) {
        dropItems += this to dropChance
    }

    fun build(): CharacterData {
        return CharacterData(description, type, charClass, baseLevel, attributes, element, rewardXP, dropItems)
    }
}

@DataDSL
fun char(setup: CharacterDataBuilder.() -> Unit): CharacterData {
    val builder = CharacterDataBuilder()
    builder.setup()
    return builder.build()
}

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class CharacterData(
        val description: Description,
        val type: CharacterType,
        val charClass: CharacterClass,
        val baseLevel: Int,
        val attributes: Attribute.AttributeInfo,
        //val defaultWeapon: Weapon = WeaponEntity(Data.Weapon.HANDS())
        val element: Element,
        val rewardXP: Experience,

        /**
         * Item ID -> chance to drop
         */
        val dropItems: List<Pair<Int, Int>>
)