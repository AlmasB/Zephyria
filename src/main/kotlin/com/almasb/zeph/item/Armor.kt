package com.almasb.zeph.item

import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.combat.Element
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

/**
 * Armor item.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Armor(data: ArmorData) : Item(data.description) {

    val refineLevel = SimpleIntegerProperty()
    val element = SimpleObjectProperty<Element>()

    val armor = SimpleIntegerProperty()
    val marmor = SimpleIntegerProperty()

    init {
        element.value = data.element

        armor.bind(refineLevel.multiply(Bindings
                .`when`(refineLevel.greaterThan(2))
                .then(data.itemLevel.bonus + 1)
                .otherwise(data.itemLevel.bonus))
                .add(data.armor))

        marmor.bind(refineLevel.multiply(Bindings
                .`when`(refineLevel.greaterThan(2))
                .then(data.itemLevel.bonus + 1)
                .otherwise(data.itemLevel.bonus))
                .add(data.marmor))

//        val rawDescription = desc.description.value
//        desc.description.bind(desc.name.concat("\n")
//                .concat(rawDescription + "\n")
//                .concat(element.asString("Element: %s").concat("\n"))
//                .concat(armor.asString("Armor: %d").concat("%\n"))
//                .concat(marmor.asString("MArmor: %d").concat("%\n"))
//                .concat("${data.runes}"))
    }

    fun onEquip(entity: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        // TODO: must use actual runes not data
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
//
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, armor.value)
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, marmor.value)
    }

    fun onUnEquip(entity: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }
//
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, -armor.value)
//        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, -marmor.value)
    }
}