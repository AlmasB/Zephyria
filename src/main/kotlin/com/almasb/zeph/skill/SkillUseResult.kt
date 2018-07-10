package com.almasb.zeph.skill

import com.almasb.zeph.combat.DamageResult

/**
 * Data structure for holding information about the result of using a skill.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
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