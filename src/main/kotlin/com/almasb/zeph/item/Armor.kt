package com.almasb.zeph.item

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import java.util.concurrent.Callable

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

        dynamicTextFlow.children.addAll(
                getUIFactoryService().newText("Element: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", 16.0).also {
                    it.fillProperty().bind(Bindings.createObjectBinding(Callable { element.value.color }, element))
                    it.textProperty().bind(element.asString("%s\n"))
                },
                getUIFactoryService().newText("Armor: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.GREEN, 16.0).also {
                    it.textProperty().bind(armor.asString("%d\n"))
                },
                getUIFactoryService().newText("MArmor: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.BLUE, 16.0).also {
                    it.textProperty().bind(marmor.asString("%d\n"))
                }
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