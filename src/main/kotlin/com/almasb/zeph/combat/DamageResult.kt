package com.almasb.zeph.combat

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class DamageResult(val type: DamageType, val element: Element, val value: Int, val isCritical: Boolean) {

    companion object {
        val NONE = DamageResult(DamageType.PURE, Element.NEUTRAL, 0, false)
    }

    override fun toString() = "DamageResult($value $type $element Critical:$isCritical)"
}

/**
 * There are 3 types of damage that can be dealt to a character.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class DamageType {
    /**
     * Damage dealt by weapon of any kind or a skill
     * which is based on weapon attack. Can crit.
     */
    PHYSICAL,

    /**
     * Damage dealt by a magic based skill. Can crit.
     */
    MAGICAL,

    /**
     * Pure flat damage, most likely dealt by a skill.
     * Can NOT critically strike.
     */
    PURE
}