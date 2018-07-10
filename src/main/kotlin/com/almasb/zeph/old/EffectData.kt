package com.almasb.zeph.old

import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.Description
import javafx.beans.property.SimpleDoubleProperty
import javafx.util.Duration
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class EffectData(
        val description: Description,
        val duration: Double,
        val runes: List<Rune>,
        val essences: List<Essence>
)