package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.Effect
import com.almasb.zeph.combat.EffectDataBuilder
import com.almasb.zeph.emptyDescription

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItem(val data: UsableItemData) : Item(data.description) {

    fun onUse(char: CharacterEntity) {
        data.onUseScripts.forEach { it.invoke(char) }
    }

    override fun hashCode(): Int {
        return description.id
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UsableItem)
            return false

        return this.description.id == other.description.id
    }
}

@DataDSL
class UsableItemDataBuilder(
        var description: Description = emptyDescription,
        var isPermanentUse: Boolean = false,
        var useSoundName: String = "",
        var beforeUseScript: (CharacterEntity) -> Boolean = { true },
        private val onUseScripts: MutableList<(CharacterEntity) -> Unit> = arrayListOf()
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun restoreHP(amount: Double) {
        appendDescription("Restores $amount HP.")
        onUseScript { it.hp.restore(amount) }
    }

    fun restoreSP(amount: Double) {
        appendDescription("Restores $amount SP.")
        onUseScript { it.sp.restore(amount) }
    }

    fun addEffect(setup: EffectDataBuilder.() -> Unit) {
        val builder = EffectDataBuilder()
        builder.description = description
        builder.setup()

        val effect = Effect(builder.build())

        description = builder.description

        onUseScript { it.addEffect(effect) }
    }

    fun onUseScript(script: (CharacterEntity) -> Unit) {
        onUseScripts += script
    }

    private fun appendDescription(text: String) {
        description = description.appendDescription(text)
    }

    fun build(): UsableItemData {
        val soundName = if (useSoundName.isEmpty()) useSoundName else "items/$useSoundName"

        return UsableItemData(description, isPermanentUse, soundName, beforeUseScript, onUseScripts)
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

        /**
         * If true, using this item does not consume it.
         */
        val isPermanentUse: Boolean,
        val useSoundName: String,

        /**
         * If returns true, can be used.
         * If returns false, item cannot be used and won't be consumed.
         */
        val beforeUseScript: (CharacterEntity) -> Boolean,

        val onUseScripts: List<(CharacterEntity) -> Unit>
) : ItemData