package com.almasb.zeph.entity.item

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.item.component.ArmorDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ArmorEntity(dataComponents: List<Component>) : Entity() {

    val desc: DescriptionComponent
    val data: ArmorDataComponent

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(ArmorDataComponent::class.java)

        desc.description = "${desc.name}\n${desc.description}\n$data"
    }
}