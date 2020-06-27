package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.emptyDescription

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItem(val data: UsableItemData) : Item(data.description) {

    fun onUse(char: CharacterEntity) {
        data.onUseScript.invoke(char)
    }
}

class MiscItem(val data: MiscItemData) : Item(data.description) {

}

@DataDSL
class UsableItemDataBuilder(
        var description: Description = emptyDescription,
        var beforeUseScript: (CharacterEntity) -> Boolean = { true },
        var onUseScript: (CharacterEntity) -> Unit = { }
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): UsableItemData {
        return UsableItemData(description, beforeUseScript, onUseScript)
    }
}

@DataDSL
class MiscItemDataBuilder(
        var description: Description = emptyDescription
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

        return MiscItemData(description)
    }
}

@DataDSL
fun usableItem(setup: UsableItemDataBuilder.() -> Unit): UsableItemData {
    val builder = UsableItemDataBuilder()
    builder.setup()
    return builder.build()
}

@DataDSL
fun miscItem(setup: MiscItemDataBuilder.() -> Unit): MiscItemData {
    val builder = MiscItemDataBuilder()
    builder.setup()
    return builder.build()
}

data class UsableItemData(
        override val description: Description,
        val beforeUseScript: (CharacterEntity) -> Boolean,
        val onUseScript: (CharacterEntity) -> Unit
) : ItemData

data class MiscItemData(
        override val description: Description
) : ItemData