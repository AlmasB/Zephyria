package com.almasb.zeph.combat

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class CombatMove(vararg modifiers: Double) {
    FAST_WEAPON_ATTACK     (1.00, 0.75, 1.00, 1.25, 2.00, 0.00, 1.25, 0.75),
    POWERFUL_WEAPON_ATTACK (0.75, 1.00, 1.25, 1.00, 0.00, 2.00, 0.75, 1.25),
    FAST_SKILL_ATTACK      (1.25, 0.75, 1.00, 0.75, 1.25, 0.75, 2.00, 0.00),
    POWERFUL_SKILL_ATTACK  (0.75, 1.00, 0.75, 1.00, 0.75, 1.25, 0.00, 2.00),

    BLOCK                  (0.75, 3.00, 0.50, 2.00, 1.00, 0.25, 0.00, 0.25),
    PARRY                  (3.00, 0.75, 2.00, 0.50, 0.25, 1.00, 0.25, 0.00),
    DODGE                  (0.50, 2.00, 0.75, 3.00, 0.00, 0.25, 1.00, 0.25),
    DEFLECT                (2.00, 0.50, 3.00, 0.75, 0.25, 0.00, 0.25, 1.00);

    // block - stop the hit
    // parry - redirect the hit elsewhere
    // dodge - evade the (skill) hit
    // deflect - redirect the (skill) hit back at the attacker

    private val modifiers: DoubleArray = modifiers

    fun getDamageModifierAgainst(move: CombatMove): Double {
        return this.modifiers[move.ordinal]
    }
}