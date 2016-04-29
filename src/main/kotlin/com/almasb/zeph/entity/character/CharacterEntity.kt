package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.character.control.CharacterControl
import com.almasb.zeph.entity.item.component.WeaponDataComponent
import com.almasb.zeph.entity.skill.SkillEntity
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(dataComponents: List<Component>) : GameEntity() {

    val charClass = SimpleObjectProperty<CharacterClass>(CharacterClass.MONSTER)

    val hp = HPComponent()
    val sp = SPComponent()

    val baseLevel: SimpleIntegerProperty
    val attributes: AttributesComponent
    val stats = StatsComponent()

    val weaponElement = SimpleObjectProperty<Element>()
    val armorElement = SimpleObjectProperty<Element>()

    val inventory = Inventory()
    val skills = FXCollections.observableArrayList<SkillEntity>()

    val baseXP = SimpleIntegerProperty()
    val statXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    val description: DescriptionComponent
    val data: CharacterDataComponent

    val charConrol = CharacterControl()

    init {
        addComponent(stats)

        addComponent(hp)
        addComponent(sp)

        dataComponents.forEach { addComponent(it) }

        description = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(CharacterDataComponent::class.java)

        attributes = data.attributes
        baseLevel = data.baseLevel

        weaponElement.value = data.element
        armorElement.value = data.element

        addComponent(attributes)

        addControl(charConrol)
    }
}