package com.almasb.zeph.combat

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class Element
private constructor(vararg modifiers: Double) {
    NEUTRAL(1.00, 0.75, 0.75, 0.75, 0.75),
    FIRE   (1.25, 0.00, 0.25, 0.50, 2.00),
    WATER  (1.25, 2.00, 0.00, 0.25, 0.50),
    AIR    (1.25, 0.50, 2.00, 0.00, 0.25),
    EARTH  (1.25, 0.25, 0.50, 2.00, 0.00);

    private val modifiers: DoubleArray

    fun getDamageModifierAgainst(element: Element): Double {
        return this.modifiers[element.ordinal]
    }

    init {
        this.modifiers = modifiers
    }
}