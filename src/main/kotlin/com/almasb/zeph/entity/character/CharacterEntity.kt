package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.character.control.CharacterControl
import com.almasb.zeph.entity.item.component.WeaponDataComponent
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(dataComponents: List<Component>) : GameEntity() {

    val hp = HPComponent()
    val sp = SPComponent()

    val baseLevel: SimpleIntegerProperty
    val attributes: AttributesComponent
    val stats = StatsComponent()

    val inventory = Inventory()

    val baseXP = SimpleIntegerProperty()
    val statXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    val description: DescriptionComponent
    val data: CharacterDataComponent

    init {
        addComponent(stats)

        addComponent(hp)
        addComponent(sp)

        dataComponents.forEach { addComponent(it) }

        description = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(CharacterDataComponent::class.java)

        attributes = data.attributes
        baseLevel = data.baseLevel

        addComponent(attributes)

        addControl(CharacterControl())
    }

    //TODO: fun getControl() = getControlUnsafe(PlayerControl::class.java)
}