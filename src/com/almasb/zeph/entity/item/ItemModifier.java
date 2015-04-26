package com.almasb.zeph.entity.item;

public class ItemModifier {

    public enum ModifierType {
        HP_MAX, SP_MAX, HP_REGEN, SP_REGEN,
        ATK, MATK, DEF, MDEF, ARM, MARM, ASPD, MSPD, CRIT_CHANCE, MCRIT_CHANCE, CRIT_DMG, MCRIT_DMG
    }

    private ModifierType type;
    private int value;

    public ModifierType getType() {
        return type;
    }

    public void setType(ModifierType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
