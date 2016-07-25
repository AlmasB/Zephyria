package com.almasb.zeph.combat

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.entity.DescriptionComponent
import javafx.beans.property.DoubleProperty

/**
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class StatusEffectEntity(dataComponents: List<Component>): Entity() {

    val desc: DescriptionComponent
    val data: StatusEffectDataComponent

    val duration: DoubleProperty

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(StatusEffectDataComponent::class.java)

        duration = data.duration
        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun getID() = desc.id
}