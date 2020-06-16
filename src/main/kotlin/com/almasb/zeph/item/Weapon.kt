package com.almasb.zeph.item

import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

enum class WeaponType(val range: Int, val aspdFactor: Float) {

    ONE_H_SWORD(2, 0.85f), ONE_H_AXE(2, 0.95f), DAGGER(1, 1.25f), SPEAR(3, 0.85f), MACE(2, 1.0f), ROD(5, 0.9f), SHIELD(0, 0.9f), // 1H, shield only left-hand
    TWO_H_SWORD(2, 0.7f), TWO_H_AXE(2, 0.65f), KATAR(1, 0.85f), BOW(5, 0.75f);

    fun isTwoHanded() = this.ordinal >= TWO_H_SWORD.ordinal
}

/**
 * Weapon item.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapon(data: WeaponData) : EquipItem(data.description, data.itemLevel, data.runes) {

    val element = SimpleObjectProperty(data.element)

    val pureDamage = SimpleIntegerProperty()

    val range = data.type.range

    val type = data.type

    init {
        pureDamage.bind(refineLevel.multiply(Bindings
                .`when`(refineLevel.greaterThan(2))
                .then(data.itemLevel.bonus + 1)
                .otherwise(data.itemLevel.bonus))
                .add(data.pureDamage))

        dynamicDescription.bind(
                SimpleStringProperty("")
                .concat(description.name + "\n")
                .concat(description.description + "\n")
                .concat(element.asString("Element: %s").concat("\n"))
                .concat(pureDamage.asString("Damage: %d").concat("\n"))
                .concat(runes)
        )
    }

    override fun onEquip(char: CharacterEntity) {
        super.onEquip(char)

        char.stats.addBonusStat(Stat.ATK, pureDamage.value)
    }

    override fun onUnEquip(char: CharacterEntity) {
        super.onUnEquip(char)

        char.stats.addBonusStat(Stat.ATK, -pureDamage.value)
    }
}
