package com.almasb.zeph.item

import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Weapon item.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapon(data: WeaponData) : Item(data.description) {

    val refineLevel = SimpleIntegerProperty()
    val element = SimpleObjectProperty<Element>(data.element)

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
                .concat("${data.runes}")
        )
    }

    fun onEquip(char: PlayerEntity) {
        // TODO: dynamic runes, not data runes

//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }

        char.stats.addBonusStat(Stat.ATK, pureDamage.value)
    }

    fun onUnEquip(char: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }

        char.stats.addBonusStat(Stat.ATK, -pureDamage.value)
    }

    // TODO:
//    fun refine() {
//        if (refineLevel >= 5) {
//            return
//        }
//
//        if (GameMath.checkChance(100 - refineLevel * itemLevel.refineChanceReduction))
//            refineLevel++
//        else if (refineLevel > 0)
//            refineLevel--
//    }
}
