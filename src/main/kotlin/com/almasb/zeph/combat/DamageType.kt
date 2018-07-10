package com.almasb.zeph.combat

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