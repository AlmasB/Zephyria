package com.almasb.zeph.combat;

/**
 * 9 primary attributes of a game character
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 * @version 1.0
 *
 */
public enum Attribute {
    STRENGTH("Increases damage dealt by physical attacks. Also slightly increases HP and regeneration."),
    VITALITY("Reduces damage you take, increases maximum HP and regeneration."),
    DEXTERITY("Increases attack speed and slightly improves all other combat stats."),
    AGILITY("Allows you to attack a lot faster with all weapons."),
    INTELLECT("Greatly increases magic damage and defense. Also provides greater SP pool."),
    WISDOM("Significantly increases SP pool and regeneration. Slightly increases magic damage."),
    WILLPOWER("Allows you to withstand more magic damage and score critical hits with skills."),
    PERCEPTION("Slightly improves all stats, including chance of critical hits with attacks and skills."),
    LUCK("Directly affects the ability to score critical hits and deal more critical damage.");

    private final String description;

    private Attribute(String description) {
        this.description = description;
    }

    /**
     *
     * @return attribute description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name().length() > 3 ? this.name().substring(0, 3) : this.name();
    }

    /**
     * Data structure for an info object that contains
     * values of all the attributes
     */
    public static class AttributeInfo {

        public int str = 1, vit = 1, dex = 1,
                agi = 1, int_ = 1, wis = 1,
                wil = 1, per = 1, luc = 1;

        public AttributeInfo str(final int value) {
            str = value;
            return this;
        }

        public AttributeInfo vit(final int value) {
            vit = value;
            return this;
        }

        public AttributeInfo dex(final int value) {
            dex = value;
            return this;
        }

        public AttributeInfo agi(final int value) {
            agi = value;
            return this;
        }

        public AttributeInfo int_(final int value) {
            int_ = value;
            return this;
        }

        public AttributeInfo wis(final int value) {
            wis = value;
            return this;
        }

        public AttributeInfo wil(final int value) {
            wil = value;
            return this;
        }

        public AttributeInfo per(final int value) {
            per = value;
            return this;
        }

        public AttributeInfo luc(final int value) {
            luc = value;
            return this;
        }
    }
}
