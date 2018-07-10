package com.almasb.zeph.old

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.entity.Description
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty

/**
 * Buff that lasts for a period of time, can be negative
 * e.g. atk decrease.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class EffectComponent(data: EffectData) : Component() {

    val desc: Description = data.description

    val duration: DoubleProperty = SimpleDoubleProperty(data.duration)

    init {

        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun onBegin(char: Entity) {
        //data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, it.bonus) }
        //data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, it.bonus) }
    }

    fun onEnd(char: Entity) {
        //data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, -it.bonus) }
        //data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, -it.bonus) }
    }

    fun getID() = desc.id
}