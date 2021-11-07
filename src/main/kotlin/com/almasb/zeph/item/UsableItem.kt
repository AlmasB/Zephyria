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
class UsableItemRestoreHPBuilder(
        var hpToRestore: Double = 0.0
)

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

//@DataDSL
//fun usableItemRestoreHP(setupHP: UsableItemRestoreHPBuilder.() -> Unit, setup: UsableItemDataBuilder.() -> Unit): UsableItemData {
//    val hpRestoreBuilder = UsableItemRestoreHPBuilder()
//    hpRestoreBuilder.setupHP()
//
//    val builder = UsableItemDataBuilder()
//    builder.setup()
//    builder.description = builder.description.copy(description = "Restores ${hpRestoreBuilder.hpToRestore} HP")
//
//    val tmp = builder.onUseScript
//    builder.onUseScript = {
//        it.hp.restore(hpRestoreBuilder.hpToRestore)
//        tmp.invoke(it)
//    }
//
//    return builder.build()
//}

@DataDSL
fun miscItem(setup: MiscItemDataBuilder.() -> Unit): MiscItemData {
    val builder = MiscItemDataBuilder()
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
        val beforeUseScript: (CharacterEntity) -> Boolean,

        val onUseScripts: List<(CharacterEntity) -> Unit>
) : ItemData

data class MiscItemData(
        override val description: Description
) : ItemData