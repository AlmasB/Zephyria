package com.almasb.zeph.entity.skill

import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.entity.Description
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SkillComponent() : Component() {

//    val desc: Description
//    val data: SkillDataComponent
//
//    val level = SimpleIntegerProperty()
//    val currentCooldown = SimpleDoubleProperty()
//
//    // TODO: rename to something meaningful
//    // this is where we store the value associated with passive bonus
//    var testValue = 0
//
//    // control ?
//
//    init {
//        dataComponents.forEach { addComponent(it) }
//
//        desc = getComponentUnsafe(Description::class.java)
//        data = getComponentUnsafe(SkillDataComponent::class.java)
//
//        val rawDescription = desc.description.value
//        desc.description.bind(desc.name.concat("\n")
//                .concat(rawDescription + "\n")
//                .concat(level.asString("Level: %d\n"))
//                .concat(level.multiply(data.mana).asString("Mana Cost: %d\n")))
//    }
//
//    val manaCost by lazy { level.multiply(data.mana) }
//
//    //fun getManaCost() = level.value * data.mana
//
//    fun isOnCooldown() = currentCooldown.value > 0
//
//    fun putOnCooldown() {
//        currentCooldown.value = data.cooldown
//    }
//
//    fun onUpdate(tpf: Double) {
//        if (isOnCooldown()) {
//            currentCooldown.value -= tpf
//        } else if (currentCooldown.value < 0) {
//            currentCooldown.value = 0.0
//        }
//    }
}