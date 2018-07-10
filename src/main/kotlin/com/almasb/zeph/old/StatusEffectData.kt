package com.almasb.zeph.old

import com.almasb.zeph.combat.Status
import com.almasb.zeph.entity.Description
import javafx.beans.property.SimpleDoubleProperty

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