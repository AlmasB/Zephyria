package com.almasb.zeph.combat

/**
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class StatusEffect(

        /**
         * The status this effect applies.
         */
        val status: Status,

        /**
         * For how long the effect lasts.
         */
        private var duration: Double) {

    fun reduceDuration(value: Double) {
        duration -= value
    }

    fun getDuration() = duration
}