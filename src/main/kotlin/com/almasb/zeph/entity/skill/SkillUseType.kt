package com.almasb.zeph.entity.skill

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
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