package com.almasb.zeph.entity.item

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.character.component.AttributesComponent
import com.almasb.zeph.entity.character.component.StatsComponent
import com.almasb.zeph.entity.item.component.ArmorDataComponent
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ArmorEntity(dataComponents: List<Component>) : Entity() {

    val desc: DescriptionComponent
    val data: ArmorDataComponent

    val refineLevel = SimpleIntegerProperty()
    val element = SimpleObjectProperty<Element>()

    val armor = SimpleIntegerProperty()
    val marmor = SimpleIntegerProperty()

    init {
        dataComponents.forEach { addComponent(it) }

        desc = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(ArmorDataComponent::class.java)

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

        val rawDescription = desc.description.value
        desc.description.bind(desc.name.concat("\n")
                .concat(rawDescription + "\n")
                .concat(element.asString("Element: %s").concat("\n"))
                .concat(armor.asString("Armor: %d").concat("%\n"))
                .concat(marmor.asString("MArmor: %d").concat("%\n"))
                .concat("${data.runes}"))
    }

    fun onEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        // TODO: must use actual runes not data
        data.runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }

        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, armor.value)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, marmor.value)
    }

    fun onUnEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        data.runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }

        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, -armor.value)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, -marmor.value)
    }
}