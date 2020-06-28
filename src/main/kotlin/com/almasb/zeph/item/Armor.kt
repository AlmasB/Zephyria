package com.almasb.zeph.item

import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

enum class ArmorType {
    HELM, BODY, SHOES
}

/**
 * Armor item.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Armor(data: ArmorData) : EquipItem(data.description, data.itemLevel, data.runes, data.essences) {

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
                .concat(runes)
                .concat(essences)
        )
    }

    override fun onEquip(char: CharacterEntity) {
        super.onEquip(char)

        char.addBonus(Stat.ARM, armor.value)
        char.addBonus(Stat.MARM, marmor.value)
    }

    override fun onUnEquip(char: CharacterEntity) {
        super.onUnEquip(char)

        char.addBonus(Stat.ARM, -armor.value)
        char.addBonus(Stat.MARM, -marmor.value)
    }
}