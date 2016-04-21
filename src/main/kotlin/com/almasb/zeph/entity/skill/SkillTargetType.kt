package com.almasb.zeph.entity.skill

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
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