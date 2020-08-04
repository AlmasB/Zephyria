package com.almasb.zeph.skill

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.ui.TooltipTextFlow
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import javafx.scene.text.TextFlow

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Skill(val data: SkillData) {

    val dynamicDescription = SimpleStringProperty(data.description.description)

    val dynamicTextFlow = TooltipTextFlow(data.description)

    val levelProperty = SimpleIntegerProperty()
    val manaCost = levelProperty.multiply(data.manaCost)

    val level: Int
        get() = levelProperty.value

    /**
     * In seconds.
     */
    val currentCooldown = SimpleDoubleProperty()

    val isOnCooldown: Boolean
        get() = currentCooldown.value > 0

    init {
        dynamicDescription.bind(SimpleStringProperty(data.description.name).concat("\n")
                .concat(data.description.description + "\n")
                .concat(levelProperty.asString("Level: %d\n"))
                .concat(manaCost.asString("Mana Cost: %d\n")))

        // TODO: this is still static for now, we need to listen for any changes in dynamicDescription and update

        dynamicTextFlow.children.addAll(
                getUIFactoryService().newText("Level: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.GREEN, 16.0).also { it.textProperty().bind(levelProperty.asString("%d\n")) },
                getUIFactoryService().newText("Cost: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.BLUE, 16.0).also { it.textProperty().bind(manaCost.asString("%d\n")) }
        )
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

    /**
     * Called on each level up of this skill.
     */
    fun onLearn(caster: CharacterEntity) {
        // apply passive bonuses per level
        if (data.type == SkillType.PASSIVE) {
            data.passiveRunes.forEach { caster.addBonus(it.attribute, it.bonus) }
            data.passiveEssences.forEach { caster.addBonus(it.stat, it.bonus) }
        }

        data.onLearnScript.invoke(caster, this)
    }

    fun onCast(caster: CharacterEntity, target: CharacterEntity) {
        data.onCastScript.invoke(caster, target, this)
    }
}