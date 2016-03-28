package com.almasb.zeph

import com.almasb.ents.AbstractComponent
import com.almasb.ents.Entity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.GameMath
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.character.AttributesComponent
import com.almasb.zeph.entity.item.ItemLevel
import java.util.*

/**
 * TODO: add var essence: Essence
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class EquippableComponent(val itemLevel: ItemLevel) : AbstractComponent() {

    var refineLevel = 0
    var element = Element.NEUTRAL

    val runes = ArrayList<Rune>()

    open fun onEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        runes.forEach { attrs.addBonusAttribute(it.attribute, it.bonus) }
    }

    open fun onUnEquip(entity: Entity) {
        val attrs = entity.getComponentUnsafe(AttributesComponent::class.java)

        runes.forEach { attrs.addBonusAttribute(it.attribute, -it.bonus) }
    }

    fun addRune(rune: Rune): Boolean {
        if (runes.size < itemLevel.maxRunes) {
            return runes.add(rune)
        }

        return false
    }

    fun refine() {
        if (refineLevel >= 5) {
            return
        }

        if (GameMath.checkChance(100 - refineLevel * itemLevel.refineChanceReduction))
            refineLevel++
        else if (refineLevel > 0)
            refineLevel--
    }
}