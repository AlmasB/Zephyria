package com.almasb.zeph.item

import com.almasb.zeph.Config.MAX_ESSENCES
import com.almasb.zeph.Description
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.GameMath
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.ui.TooltipTextFlow
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.text.TextFlow

enum class ItemLevel constructor(
        val bonus: Int,
        val refineChanceReduction: Int,
        val maxRunes: Int) {

    // refinement chance 100/90/80/70/60
    NORMAL(3, 10, 2),

    // refinement chance 100/85/70/55/40
    UNIQUE(5, 15, 3),

    // refinement chance 100/80/60/40/20
    EPIC(10, 20, 4)
}

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class Item(val description: Description) {

    /**
     * A property populated with dynamic description.
     * Typically a combination of static + runtime info, such as
     * actual weapon damage, actual armor rating, etc.
     */
    val dynamicDescription = SimpleStringProperty(description.name + "\n" + description.description)

    val dynamicTextFlow: TextFlow = TooltipTextFlow(description)
}

abstract class EquipItem(
        description: Description,
        val itemLevel: ItemLevel,
        dataRunes: List<Rune>,
        dataEssences: List<Essence>
) : Item(description) {

    val runes = FXCollections.observableArrayList(dataRunes)
    val essences = FXCollections.observableArrayList(dataEssences)
    var equippedCharacter: CharacterEntity? = null

    val refineLevel = SimpleIntegerProperty(0)

    /**
     * @return whether rune addition succeeded
     */
    fun addRune(rune: Rune): Boolean {
        if (runes.size >= itemLevel.maxRunes)
            return false

        equippedCharacter?.let {
            onUnEquip(it)
        }

        runes += rune

        equippedCharacter?.let {
            onEquip(it)
        }

        return true
    }

    fun removeRune(rune: Rune) {
        equippedCharacter?.let {
            onUnEquip(it)
        }

        runes -= rune

        equippedCharacter?.let {
            onEquip(it)
        }
    }

    /**
     * @return whether essence addition succeeded
     */
    fun addEssence(essence: Essence): Boolean {
        if (essences.size >= MAX_ESSENCES)
            return false

        equippedCharacter?.let {
            onUnEquip(it)
        }

        essences += essence

        equippedCharacter?.let {
            onEquip(it)
        }

        return true
    }

    fun removeEssence(essence: Essence) {
        equippedCharacter?.let {
            onUnEquip(it)
        }

        essences -= essence

        equippedCharacter?.let {
            onEquip(it)
        }
    }

    open fun onEquip(char: CharacterEntity) {
        equippedCharacter = char

        runes.forEach { char.addBonus(it.attribute, it.bonus) }
        essences.forEach { char.addBonus(it.stat, it.bonus) }
    }

    open fun onUnEquip(char: CharacterEntity) {
        equippedCharacter = null

        runes.forEach { char.addBonus(it.attribute, -it.bonus) }
        essences.forEach { char.addBonus(it.stat, -it.bonus) }
    }

    /**
     * @return true if refine succeeded
     */
    fun refine(): Boolean {
        if (refineLevel.value >= 5)
            return false

        if (GameMath.checkChance(100 - refineLevel.value * itemLevel.refineChanceReduction)) {
            refineLevel.value++
            return true
        }

        if (refineLevel.value > 0) {
            refineLevel.value--
        }

        return false
    }
}

/**
 * Marks a class that stores item data.
 */
interface ItemData {
    val description: Description
}