package com.almasb.zeph.combat

import com.almasb.ents.AbstractComponent
import javafx.beans.property.SimpleDoubleProperty
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class EffectDataComponent(duration: Double) : AbstractComponent() {

    val duration = SimpleDoubleProperty(duration)

    val runeEffects = ArrayList<Rune>()
    val essenseEffects = ArrayList<Essence>()

    fun withRune(rune: Rune): EffectDataComponent {
        runeEffects.add(rune)
        return this
    }

    fun withEssence(essence: Essence): EffectDataComponent {
        essenseEffects.add(essence)
        return this
    }
}