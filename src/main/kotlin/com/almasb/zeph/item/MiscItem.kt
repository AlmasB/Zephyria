package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.emptyDescription

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MiscItem(val data: MiscItemData) : Item(data.description) {

    override fun hashCode(): Int {
        return description.id
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MiscItem)
            return false

        return this.description.id == other.description.id
    }
}

@DataDSL
class MiscItemDataBuilder(
        var description: Description = emptyDescription,
        var sellPrice: Int = 0,
        var buyPrice: Int = 0
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): MiscItemData {
        if (description.textureName.isEmpty()) {
            val fileName = description.name.toLowerCase().replace(" ", "_") + ".png"

            description = description.copy(textureName = "items/misc/$fileName")
        }

        // TODO: sell / buy log if == 0, or if sell > buy

        return MiscItemData(description, sellPrice, buyPrice)
    }
}

@DataDSL
fun miscItem(setup: MiscItemDataBuilder.() -> Unit): MiscItemData {
    val builder = MiscItemDataBuilder()
    builder.setup()
    return builder.build()
}

data class MiscItemData(
        override val description: Description,
        val sellPrice: Int,
        val buyPrice: Int
) : ItemData