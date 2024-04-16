package com.almasb.zeph.combat

/**
 * Handles everything to do with math and calculations.
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 */
object GameMath {

    /**
     * "Rolls" a dice and checks it against the chance percentage
     * @param chance the chance against which the roll is checked.
     * 100 will always return true, 1 - will return true approx. once in 100 rolls
     *
     * @return true if chance succeeds, false otherwise
     */
    fun checkChance(chance: Int): Boolean {
        return (Math.random() * 100).toInt() + 1 <= chance
    }

    fun checkChance(chance: Double): Boolean {
        return Math.random() * 100 + 1 <= chance
    }
}

fun runIfChance(chance: Int, action: () -> Unit) {
    if (GameMath.checkChance(chance)) {
        action.invoke()
    }
}