package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItem(val data: UsableItemData) : Item(data.description) {

    fun onUse(char: CharacterEntity) {
        data.onUseScript.invoke(char)
    }
}

@DataDSL
class UsableItemDataBuilder(
        var description: Description = Description(),
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
fun usableItem(setup: UsableItemDataBuilder.() -> Unit): UsableItemData {
    val builder = UsableItemDataBuilder()
    builder.setup()
    return builder.build()
}

data class UsableItemData(
        override val description: Description,
        val beforeUseScript: (CharacterEntity) -> Boolean,
        val onUseScript: (CharacterEntity) -> Unit
) : ItemData