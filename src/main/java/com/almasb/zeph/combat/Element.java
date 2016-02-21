package com.almasb.zeph.combat;

public enum Element {
    NEUTRAL(1.00f, 0.75f, 0.75f, 0.75f, 0.75f),
    FIRE   (1.25f, 0.00f, 0.25f, 0.50f, 2.00f),
    WATER  (1.25f, 2.00f, 0.00f, 0.25f, 0.50f),
    AIR    (1.25f, 0.50f, 2.00f, 0.00f, 0.25f),
    EARTH  (1.25f, 0.25f, 0.50f, 2.00f, 0.00f);

    private float[] modifiers;

    public float getDamageModifierAgainst(Element element) {
        return this.modifiers[element.ordinal()];
    }

    Element(float... modifiers) {
        this.modifiers = modifiers;
    }
}
