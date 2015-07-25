package com.almasb.zeph.combat;

/**
 * Stats of a game character
 *
 */
public enum Stat {
    /**
     * Maximum HP
     */
    MAX_HP,

    /**
     * Maximum SP
     */
    MAX_SP,

    /**
     * Amount of HP regenerated per regen tick
     */
    HP_REGEN,

    /**
     * Amount of SP regenerated per regen tick
     */
    SP_REGEN,

    /**
     * Physical attack damage
     */
    ATK,

    /**
     * Magical attack damage
     */
    MATK,

    /**
     * Physical defense
     * This is applied as flat damage reduction after {@link #ARM}
     */
    DEF,

    /**
     * Magical defense
     * This is applied as flat damage reduction after {@link #MARM}
     */
    MDEF,

    /**
     * Physical armor value
     * This is applied as percentage damage reduction before {@link #DEF}
     */
    ARM,

    /**
     * Magical armor value
     * This is applied as percentage damage reduction before {@link #MDEF}
     */
    MARM,

    /**
     * Physical attack speed
     * Affects how fast a character can perform basic attacks
     */
    ASPD,

    /**
     * Magical attack speed
     * Affects how fast a character can perform skill based attacks
     */
    MSPD,

    /**
     * Chance to strike critically on each physical attack
     */
    CRIT_CHANCE,

    /**
     * Chance to land a critical hit on each magic attack
     */
    MCRIT_CHANCE,

    /**
     * Critical damage modifier for physical attacks
     */
    CRIT_DMG,

    /**
     * Critical damage modifier for magical attacks
     */
    MCRIT_DMG
}
