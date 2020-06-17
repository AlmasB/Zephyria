package com.almasb.zeph.combat

import com.almasb.zeph.Description
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty

enum class Status {

    /**
     * Stunned character is not able to perform any actions.
     */
    STUNNED,

    /**
     * Silenced character is not able to use any skills.
     */
    SILENCED,

    /**
     * Poisoned character doesn't regenerate hp/sp
     * and loses 1% of max hp/sp instead.
     */
    POISONED
}

/**
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class StatusEffect(data: StatusEffectData) {

    val desc: Description = data.description

    val duration: DoubleProperty = SimpleDoubleProperty(data.duration)

    init {
        //desc.description = "${desc.name}\n${desc.description}\n$data"
    }

    fun getID() = desc.id
}

data class StatusEffectData(
        val description: Description,
        val status: Status,
        val duration: Double
)