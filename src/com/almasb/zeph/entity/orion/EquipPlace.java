package com.almasb.zeph.entity.orion;

/**
 * Used to show where particular equipment goes on a character
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public enum EquipPlace {
    /**
     * Denotes a place for head item
     */
    HELM(5000),

    /**
     * Denotes a place for body item
     */
    BODY(5001),

    /**
     * Denotes a place for shoes item
     */
    SHOES(5002),

    /**
     * Denotes a place for right hand item
     */
    RIGHT_HAND(4000),

    /**
     * Denotes a place for left hand item
     */
    LEFT_HAND(4000);

    public final int emptyID;

    private EquipPlace(int emptyID) {
        this.emptyID = emptyID;
    }
}
