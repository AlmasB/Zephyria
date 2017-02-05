package com.almasb.zeph.entity

import com.almasb.fxgl.ecs.AbstractComponent
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class DescriptionComponent(id: Int,
                           name: String,
                           description: String,
                           textureName: String) : AbstractComponent() {

    val id = SimpleIntegerProperty(id)
    val name = SimpleStringProperty(name)
    val description = SimpleStringProperty(description)
    val textureName = SimpleStringProperty(textureName)
}