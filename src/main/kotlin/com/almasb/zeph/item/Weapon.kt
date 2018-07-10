package com.almasb.zeph.item

import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.combat.Element
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

/**
 * Weapon item.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapon(data: WeaponData) : Item(data.description) {

    val refineLevel = SimpleIntegerProperty()
    val element = SimpleObjectProperty<Element>()

    val pureDamage = SimpleIntegerProperty()

    val range: Int

    init {
        // TODO: desc = data.description

        element.value = data.element

        range = data.type.range

        pureDamage.bind(refineLevel.multiply(Bindings
                .`when`(refineLevel.greaterThan(2))
                .then(data.itemLevel.bonus + 1)
                .otherwise(data.itemLevel.bonus))
                .add(data.pureDamage))

//        val rawDescription = desc.description.value
//        desc.description.bind(desc.name.concat("\n")
//                .concat(rawDescription + "\n")
//                .concat(element.asString("Element: %s").concat("\n"))
//                .concat(pureDamage.asString("Damage: %d").concat("\n"))
//                .concat("${data.runes}"))
    }

    fun onEquip(entity: PlayerEntity) {
        // TODO: dynamic runes, not data runes

//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ATK, pureDamage.value)
    }

    fun onUnEquip(entity: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ATK, -pureDamage.value)
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
