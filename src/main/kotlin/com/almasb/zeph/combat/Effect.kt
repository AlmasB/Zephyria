package com.almasb.zeph.combat

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL

enum class Status {

    /**
     * Stunned character is not able to perform any actions.
     */
    STUNNED,

    /**
     * Silenced character is not able to use any skills.
     */
    SILENCED,

    /**
     * Poisoned character doesn't regenerate hp/sp
     * and loses 1% of max hp/sp instead.
     */
    POISONED
}

/**
 * Buff that lasts for a period of time, can be negative
 * e.g. atk decrease.
 *
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Effect(val data: EffectData) {

    val status get() = data.status

    val sourceID: Int
        get() = data.description.id

    var duration = data.duration

    fun onBegin(char: CharacterEntity) {
        data.runes.forEach { char.characterComponent.addBonus(it.attribute, it.bonus) }
        data.essences.forEach { char.characterComponent.addBonus(it.stat, it.bonus) }
    }

    fun onEnd(char: CharacterEntity) {
        data.runes.forEach { char.characterComponent.addBonus(it.attribute, -it.bonus) }
        data.essences.forEach { char.characterComponent.addBonus(it.stat, -it.bonus) }
    }
}

@DataDSL
class EffectDataBuilder(
        var description: Description = Description(),
        var duration: Double = 0.0,
        var status: Status? = null,
        var runes: MutableList<Rune> = arrayListOf(),
        var essences: MutableList<Essence> = arrayListOf()
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    operator fun Attribute.plus(value: Int) {
        runes.add(Rune(this, value))
    }

    operator fun Stat.plus(value: Int) {
        essences.add(Essence(this, value))
    }

    fun build(): EffectData {
        return EffectData(description, duration, status, runes, essences)
    }
}

@DataDSL
fun effect(setup: EffectDataBuilder.() -> Unit): EffectData {
    val builder = EffectDataBuilder()
    builder.setup()
    return builder.build()
}

data class EffectData(
        val description: Description,
        val duration: Double,
        val status: Status?,
        val runes: List<Rune>,
        val essences: List<Essence>
)