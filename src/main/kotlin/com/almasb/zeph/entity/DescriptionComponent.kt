package com.almasb.zeph.entity

import com.almasb.ents.AbstractComponent
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

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