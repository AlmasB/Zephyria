package com.almasb.zeph.entity.orion;

/**
 * 9 primary attributes of a game character
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 * @version 1.0
 *
 */
public enum Attribute {
    STRENGTH, VITALITY, DEXTERITY, AGILITY, INTELLECT, WISDOM, WILLPOWER, PERCEPTION, LUCK;

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
