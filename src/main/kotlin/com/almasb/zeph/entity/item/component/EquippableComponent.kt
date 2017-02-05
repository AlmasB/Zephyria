package com.almasb.zeph.entity.item.component

import com.almasb.fxgl.ecs.AbstractComponent
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.item.ItemLevel
import java.util.*

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class EquippableComponent(val itemLevel: ItemLevel) : AbstractComponent() {

    var element = Element.NEUTRAL

    val runes = ArrayList<Rune>()
    val essences = ArrayList<Essence>()

    fun addRune(rune: Rune): Boolean {
        if (runes.size < itemLevel.maxRunes) {
            return runes.add(rune)
        }

        return false
    }
}