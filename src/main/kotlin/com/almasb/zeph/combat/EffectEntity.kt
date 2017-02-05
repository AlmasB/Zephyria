package com.almasb.zeph.combat

import com.almasb.fxgl.ecs.Component
import com.almasb.fxgl.ecs.Entity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.character.CharacterEntity
import javafx.beans.property.DoubleProperty

/**
 * Buff that lasts for a period of time, can be negative
 * e.g. atk decrease.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class EffectEntity(dataComponents: List<Component>) : Entity() {

    val desc: DescriptionComponent
    val data: EffectDataComponent

    val duration: DoubleProperty

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(EffectDataComponent::class.java)

        duration = data.duration
        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun onBegin(char: CharacterEntity) {
        data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, it.bonus) }
        data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, it.bonus) }
    }

    fun onEnd(char: CharacterEntity) {
        data.runeEffects.forEach { char.attributes.addBonusAttribute(it.attribute, -it.bonus) }
        data.essenseEffects.forEach { char.stats.addBonusStat(it.stat, -it.bonus) }
    }

    fun getID() = desc.id
}