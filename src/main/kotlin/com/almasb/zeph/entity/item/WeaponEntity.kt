package com.almasb.zeph.entity.item

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.Data
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.item.component.WeaponDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponEntity(dataComponents: List<Component>) : Entity() {

    val desc: DescriptionComponent
    val data: WeaponDataComponent

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(WeaponDataComponent::class.java)

        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }
}
