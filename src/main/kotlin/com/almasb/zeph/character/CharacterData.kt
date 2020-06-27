package com.almasb.zeph.character

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.emptyDescription
import com.almasb.zeph.item.ItemData

@DslMarker
annotation class DataDSL

@DataDSL
class CharacterDataBuilder(
        var description: Description = emptyDescription,
        var type: CharacterType = CharacterType.NORMAL,
        var charClass: CharacterClass = CharacterClass.MONSTER,
        var baseLevel: Int = 1,
        val attributes: Attribute.AttributeInfo = Attribute.AttributeInfo(),
        val attackRange: Int = 1,
        var element: Element = Element.NEUTRAL,
        var rewardXP: Experience = Experience(0, 0, 0),

        /**
         * Item ID -> chance to drop
         */
        val dropItems: MutableList<Pair<Int, Int>> = arrayListOf()
) {

    private val dropInfo = DropInfo()

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun attributes(setup: Attribute.AttributeInfo.() -> Unit) {
        attributes.setup()
    }

    fun drops(setup: DropInfo.() -> Unit) {
        dropInfo.setup()

        dropItems += dropInfo.dropItems
    }

    fun build(): CharacterData {
        return CharacterData(description, type, charClass, baseLevel, attributes, attackRange, element, rewardXP, dropItems)
    }

    class DropInfo {
        val dropItems: MutableList<Pair<Int, Int>> = arrayListOf()

        infix fun Int.has(dropChance: Int) {
            dropItems += this to dropChance
        }

        infix fun ItemData.has(dropChance: Int) {
            dropItems += this.description.id to dropChance
        }
    }
}

@DataDSL
fun char(setup: CharacterDataBuilder.() -> Unit): CharacterData {
    val builder = CharacterDataBuilder()
    builder.setup()
    return builder.build()
}

data class CharacterData(
        val description: Description,
        val type: CharacterType,
        val charClass: CharacterClass,
        val baseLevel: Int,
        val attributes: Attribute.AttributeInfo,
        val attackRange: Int,
        val element: Element,
        val rewardXP: Experience,

        /**
         * Item ID -> chance to drop
         */
        val dropItems: List<Pair<Int, Int>>
)