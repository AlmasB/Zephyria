package com.almasb.zeph.entity.character

import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.AttributesComponent
import com.almasb.zeph.entity.character.component.HPComponent
import com.almasb.zeph.entity.character.component.SPComponent
import com.almasb.zeph.entity.character.component.StatsComponent
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity : GameEntity() {

    val hp = HPComponent()
    val sp = SPComponent()

    val baseLevel = SimpleIntegerProperty(1)
    val attributes = AttributesComponent()
    val stats = StatsComponent()

    val inventory = Inventory()

    val baseXP = SimpleIntegerProperty()
    val statXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    init {
        addComponent(attributes)
        addComponent(stats)

        addComponent(hp)
        addComponent(sp)
    }

    //TODO: fun getControl() = getControlUnsafe(PlayerControl::class.java)

    fun getDescription() = getComponentUnsafe(DescriptionComponent::class.java)
}