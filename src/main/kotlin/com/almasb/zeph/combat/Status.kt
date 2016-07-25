package com.almasb.zeph.combat

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
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