package com.almasb.zeph.entity.character

/**
 * Used to show where particular equipment goes on a character.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
enum class EquipPlace
private constructor(val emptyID: Int) {
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

    val isWeapon: Boolean
        get() = ordinal >= RIGHT_HAND.ordinal
}