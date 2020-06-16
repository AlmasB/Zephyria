package com.almasb.zeph.item

import com.almasb.zeph.Description
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.GameMath
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.combat.Stat
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections

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
    val dynamicDescription = SimpleStringProperty("")
}

abstract class EquipItem(
        description: Description,
        val itemLevel: ItemLevel,
        dataRunes: List<Rune>
) : Item(description) {

    val runes = FXCollections.observableArrayList(dataRunes)
    var equippedCharacter: CharacterEntity? = null

    val refineLevel = SimpleIntegerProperty(0)

    /**
     * @return whether rune addition succeeded
     */
    fun addRune(rune: Rune): Boolean {
        if (runes.size == itemLevel.maxRunes)
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

    open fun onEquip(char: CharacterEntity) {
        equippedCharacter = char

        runes.forEach { char.attributes.addBonusAttribute(it.attribute, it.bonus) }
    }

    open fun onUnEquip(char: CharacterEntity) {
        equippedCharacter = null

        runes.forEach { char.attributes.addBonusAttribute(it.attribute, -it.bonus) }
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