package com.almasb.zeph.skill

import com.almasb.zeph.character.CharacterEntity
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Skill(val data: SkillData) {

    val dynamicDescription = SimpleStringProperty(data.description.description)

    val level = SimpleIntegerProperty()
    val manaCost = level.multiply(data.manaCost)

    /**
     * In seconds.
     */
    val currentCooldown = SimpleDoubleProperty()

    val isOnCooldown: Boolean
        get() = currentCooldown.value > 0

    init {
        dynamicDescription.bind(SimpleStringProperty(data.description.name).concat("\n")
                .concat(data.description.description + "\n")
                .concat(level.asString("Level: %d\n"))
                .concat(manaCost.asString("Mana Cost: %d\n")))
    }

    fun putOnCooldown() {
        currentCooldown.value = data.cooldown
    }

    fun onUpdate(tpf: Double) {
        if (isOnCooldown) {
            currentCooldown.value -= tpf
        } else if (currentCooldown.value < 0) {
            currentCooldown.value = 0.0
        }
    }

    fun onCast(caster: CharacterEntity, target: CharacterEntity) {
        data.onCastScript.invoke(caster, target, this)
    }
}