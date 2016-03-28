package com.almasb.zeph

import com.almasb.ents.AbstractComponent
import com.almasb.ents.Entity
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.character.AttributesComponent
import com.almasb.zeph.entity.item.ItemLevel
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class EquippableComponent(val itemLevel: ItemLevel) : AbstractComponent() {

    val refineLevel = 0
    //var essence: Essence
    val runes = ArrayList<Rune>()

    open fun onEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
    }

    open fun onUnEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }
    }
}