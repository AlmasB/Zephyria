package com.almasb.zeph.combat

import com.almasb.zeph.Description

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class StatusEffectData(
        val description: Description,
        val status: Status,
        val duration: Double
)