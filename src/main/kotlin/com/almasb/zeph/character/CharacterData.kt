package com.almasb.zeph.character

import com.almasb.zeph.combat.*
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.armor

@DslMarker
annotation class DataDSL

@DataDSL
class CharacterDataBuilder(
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

    fun attributes(setup: Attribute.AttributeInfo.() -> Unit) {
        attributes.setup()
    }

    infix fun Int.has(dropChance: Int) {
        dropItems += this to dropChance
    }

    fun build(): CharacterData {
        return CharacterData(type, charClass, baseLevel, attributes, element, rewardXP, dropItems)
    }
}

@DataDSL
fun char(setup: CharacterDataBuilder.() -> Unit): CharacterData {
    val builder = CharacterDataBuilder()
    builder.setup()
    return builder.build()
}

fun test() {
    val myChar = char {
        type = CharacterType.NORMAL
        charClass = CharacterClass.MONSTER
        baseLevel = 2
        element = Element.AIR
        rewardXP = Experience(25, 0, 0)

        attributes {
            str = 5
            int = 2
            luc = 1
        }

        //drops {
            1890 has 50
        //}
    }

    val myArmor = armor {
        itemLevel = ItemLevel.UNIQUE
        armor = 22
        marmor = 15
        essences += Essence(Stat.ARM, 5)
    }
}

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class CharacterData(
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