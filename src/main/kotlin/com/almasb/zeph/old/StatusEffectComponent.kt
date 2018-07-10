package com.almasb.zeph.old

import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.entity.Description
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty

/**
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class StatusEffectComponent(data: StatusEffectData) : Component() {

    val desc: Description = data.description

    val duration: DoubleProperty = SimpleDoubleProperty(data.duration)

    init {
        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun getID() = desc.id
}