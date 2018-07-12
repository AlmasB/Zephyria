package com.almasb.zeph.item

import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

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

    val type = data.armorType

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

        dynamicDescription.bind(SimpleStringProperty("")
                .concat(description.name + "\n")
                .concat(description.description + "\n")
                .concat(element.asString("Element: %s").concat("\n"))
                .concat(armor.asString("Armor: %d").concat("%\n"))
                .concat(marmor.asString("MArmor: %d").concat("%\n"))
                .concat("${data.runes}")
        )
    }

    fun onEquip(char: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        // TODO: must use actual runes not data
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
//

        char.stats.addBonusStat(Stat.ARM, armor.value)
        char.stats.addBonusStat(Stat.MARM, marmor.value)
    }

    fun onUnEquip(char: PlayerEntity) {
//        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)
//
//        data.runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }
//

        char.stats.addBonusStat(Stat.ARM, -armor.value)
        char.stats.addBonusStat(Stat.MARM, -marmor.value)
    }
}