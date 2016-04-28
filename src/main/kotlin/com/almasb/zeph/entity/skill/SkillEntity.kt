package com.almasb.zeph.entity.skill

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.item.component.ArmorDataComponent
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SkillEntity(dataComponents: List<Component>) : Entity() {

    val desc: DescriptionComponent
    val data: SkillDataComponent

    val level = SimpleIntegerProperty()
    val currentCooldown = SimpleDoubleProperty()

    // TODO: rename to something meaningful
    // this is where we store the value associated with passive bonus
    var testValue = 0

    // control ?

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(SkillDataComponent::class.java)

        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun putOnCooldown() {
        currentCooldown.value = data.cooldown
    }

    fun onUpdate(tpf: Double) {
        if (currentCooldown.value > 0) {
            currentCooldown.value -= tpf
        } else if (currentCooldown.value < 0) {
            currentCooldown.value = 0.0
        }
    }
}