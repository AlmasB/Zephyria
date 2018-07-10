package com.almasb.zeph.entity.item

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.combat.Element
import com.almasb.zeph.entity.Description
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponComponent(data: WeaponData) : Component() {

    val desc: Description = Description()

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

    fun onEquip(entity: Entity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ATK, pureDamage.value)
    }

    fun onUnEquip(entity: Entity) {
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
