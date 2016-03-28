package com.almasb.zeph.entity.item;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum ItemLevel {
    NORMAL(3, 10, 2),  // refinement chance 100/90/80/70/60
    UNIQUE(5, 15, 3),  // 100/85/70/55/40
    EPIC (10, 20, 4);  // 100/80/60/40/20

    public final int bonus;
    public final int refineChanceReduction;
    public final int maxRunes;

    ItemLevel(int bonus, int chance, int maxRunes) {
        this.bonus = bonus;
        this.refineChanceReduction = chance;
        this.maxRunes = maxRunes;
    }
}