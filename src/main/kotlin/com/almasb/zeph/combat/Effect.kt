package com.almasb.zeph.combat

import com.almasb.zeph.character.CharacterEntity

/**
 * Buff that lasts for a period of time, can be negative
 * e.g. atk decrease.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Effect(val data: EffectData) {

    var duration = data.duration

    // TODO: cast to CharacterEntity?
    fun onBegin(char: CharacterEntity) {

        data.runes.forEach { char.attributes.addBonusAttribute(it.attribute, it.bonus) }
        data.essences.forEach { char.stats.addBonusStat(it.stat, it.bonus) }
    }

    fun onEnd(char: CharacterEntity) {
        data.runes.forEach { char.attributes.addBonusAttribute(it.attribute, -it.bonus) }
        data.essences.forEach { char.stats.addBonusStat(it.stat, -it.bonus) }
    }

    fun getID() = data.description.id
}