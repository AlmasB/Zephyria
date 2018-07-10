package com.almasb.zeph.combat

import com.almasb.fxgl.entity.Entity

/**
 * Buff that lasts for a period of time, can be negative
 * e.g. atk decrease.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Buff(val data: EffectData) {

    var duration = data.duration

    // TODO: cast to CharacterEntity?
    fun onBegin(entity: Entity) {
        //data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, it.bonus) }
        //data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, it.bonus) }
    }

    fun onEnd(entity: Entity) {
        //data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, -it.bonus) }
        //data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, -it.bonus) }
    }

    fun getID() = data.description.id
}