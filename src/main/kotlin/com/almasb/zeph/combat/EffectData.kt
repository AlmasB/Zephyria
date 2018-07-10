package com.almasb.zeph.combat

import com.almasb.zeph.Description

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