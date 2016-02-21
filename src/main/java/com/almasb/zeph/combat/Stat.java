package com.almasb.zeph.combat;

/**
 * Stats of a game character.
 */
public enum Stat {
    // TODO: externalize description

    /**
     * Maximum HP
     */
    MAX_HP("Maximum hit points. How much damage you can withstand before falling dead.", ""),

    /**
     * Maximum SP
     */
    MAX_SP("Maximum skill points. SP is used to perform skill based attacks.", ""),

    /**
     * Amount of HP regenerated per regen tick
     */
    HP_REGEN("Amount of HP regenerated.", "p/sec"),

    /**
     * Amount of SP regenerated per regen tick
     */
    SP_REGEN("Amount of SP regenerated.", "p/sec"),

    /**
     * Physical attack damage
     */
    ATK("Physical attack damage. Determines how much damage you deal with physical attacks (i.e. weapons).", ""),

    /**
     * Magical attack damage
     */
    MATK("Magical attack damage. Determines how much damage you deal with magical attacks (i.e. skills).", ""),

    /**
     * Physical defense
     * This is applied as flat damage reduction after {@link #ARM}
     */
    DEF("Reduces damage from physical attacks.", ""),

    /**
     * Magical defense
     * This is applied as flat damage reduction after {@link #MARM}
     */
    MDEF("Reduces damage from magical attacks.", ""),

    /**
     * Physical armor value
     * This is applied as percentage damage reduction before {@link #DEF}
     */
    ARM("Reduces damage from physical attacks.", "%"),

    /**
     * Magical armor value
     * This is applied as percentage damage reduction before {@link #MDEF}
     */
    MARM("Reduces damage from magical attacks.", "%"),

    /**
     * Physical attack speed
     * Affects how fast a character can perform basic attacks
     */
    ASPD("How fast you can attack.", "%"),

    /**
     * Magical attack speed
     * Affects how fast a character can perform skill based attacks
     */
    MSPD("How often you can use skills.", "%"),

    /**
     * Chance to strike critically on each physical attack
     */
    CRIT_CHANCE("Increases chance to strike critically on each physical attack.", "%"),

    /**
     * Chance to land a critical hit on each magic attack
     */
    MCRIT_CHANCE("Increases chance to strike critically on each magical attack.", "%"),

    /**
     * Critical damage modifier for physical attacks
     */
    CRIT_DMG("Increases critical damage for physical attacks.", "x"),

    /**
     * Critical damage modifier for magical attacks
     */
    MCRIT_DMG("Increases critical damage for magical attacks", "x");

    private final String description;
    private final String measureUnit;

    Stat(String description, String unit) {
        this.description = description;
        this.measureUnit = unit;
    }

    public String getDescription() {
        return description;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }
}
