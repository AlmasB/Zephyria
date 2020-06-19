package com.almasb.zeph.skill

import com.almasb.zeph.combat.DamageResult

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */



enum class SkillType {

    /**
     * Active skills require a target to be selected.
     */
    ACTIVE,

    /**
     * Passive skills are always "on" and do not require any target.
     */
    PASSIVE
}

enum class SkillTargetType {

    /**
     * Skill can only be used on the character himself.
     * This skill does not need to select the target.
     * Activating this skill will cause cast on the caster immediately.
     */
    SELF,

    ENEMY,

    ALLY,

    AREA
}

enum class SkillUseType {

    /**
     * Offensive skills.
     */
    DAMAGE,

    /**
     * Skills that heal, restore mana.
     */
    RESTORE,

    /**
     * Skills that apply an effect on the target.
     */
    EFFECT,

    /**
     * Skills that apply a status effect on the target.
     */
    STATUS_EFFECT
}

/**
 * Data structure for holding information about the result of using a skill.
 */
class SkillUseResult(val damage: DamageResult) {

    companion object {

        /**
         * Used when skill is not of damage/restore type.
         */
        val NONE = SkillUseResult(DamageResult.NONE)

        val NO_MANA = SkillUseResult(DamageResult.NONE)

        val ON_COOLDOWN = SkillUseResult(DamageResult.NONE)
    }
}