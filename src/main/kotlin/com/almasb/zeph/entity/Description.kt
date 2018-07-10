package com.almasb.zeph.entity

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Static description of an object.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class Description(
        val id: Int = 0,
        val name: String = "Unnamed",
        val description: String = "No description",
        val textureName: String = "") {

//    val id = SimpleIntegerProperty(id)
//    val name = SimpleStringProperty(name)
//    val description = SimpleStringProperty(description)
//    val textureName = SimpleStringProperty(textureName)
}