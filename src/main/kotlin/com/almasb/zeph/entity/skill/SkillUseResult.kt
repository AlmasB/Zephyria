package com.almasb.zeph.entity.skill

import com.almasb.zeph.combat.Damage
import com.almasb.zeph.combat.Element

/**
 * Data structure for holding information about the result of using a skill.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SkillUseResult(val damage: Damage) {

    companion object {

        /**
         * Used when skill is not of damage/restore type.
         */
        val NONE = SkillUseResult(Damage.NULL)

        val NO_MANA = SkillUseResult(Damage.NULL)

        val ON_COOLDOWN = SkillUseResult(Damage.NULL)
    }
}