package com.almasb.zeph.entity.orion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.almasb.zeph.entity.Element;

/**
 * Represents an item that can be equipped.
 * In other words, a weapon or a type of armor
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public abstract class EquippableItem extends GameEntity {

    private static final long serialVersionUID = -1091033469035972887L;

    public enum ItemLevel {
        NORMAL(3, 10, 2),  // refinement chance 100/90/80/70/60
        UNIQUE(5, 15, 3),  // 100/85/70/55/40
        EPIC (10, 20, 4);  // 100/80/60/40/20

        public final int bonus;
        public final int refineChanceReduction;
        public final int maxRunes;

        private ItemLevel(int bonus, int chance, int maxRunes) {
            this.bonus = bonus;
            this.refineChanceReduction = chance;
            this.maxRunes = maxRunes;
        }
    }

    protected static final int MAX_REFINE_LEVEL = 5;

    public final ItemLevel level;

    protected Essence essence;
    protected Element element;
    protected int refineLevel = 0;
    protected List<Rune> defaultRunes = new ArrayList<>();
    protected List<Rune> runes = new ArrayList<>();

    public EquippableItem(int id, String name, String description, String textureName,
            ItemLevel level, Element element, Rune... defRunes) {
        super(id, name, description, textureName);
        this.level = level;
        this.element = element;
        this.defaultRunes.addAll(Arrays.asList(defRunes));
    }

    public Element getElement() {
        return element;
    }

    public boolean addRune(Rune rune) {
        if (runes.size() < level.maxRunes) {
            return runes.add(rune);
        }
        return false;
    }

    public boolean addEssence(Essence e) {
        if (this.essence == null) {
            this.essence = e;
            return true;
        }
        return false;
    }

    public void onEquip(Player ch) {
        for (Rune r : defaultRunes)
            ch.addBonusAttribute(r.attribute, r.bonus);
        for (Rune r : runes)
            ch.addBonusAttribute(r.attribute, r.bonus);
        if (essence != null)
            ch.addBonusStat(essence.stat, essence.bonus);
    }

    public void onUnEquip(Player ch) {
        for (Rune r : runes)
            ch.addBonusAttribute(r.attribute, -r.bonus);
        for (Rune r : defaultRunes)
            ch.addBonusAttribute(r.attribute, -r.bonus);
        if (essence != null)
            ch.addBonusStat(essence.stat, -essence.bonus);
    }

    public void refine() {
        if (refineLevel >= MAX_REFINE_LEVEL) {
            return;
        }

        if (GameMath.checkChance(100 - refineLevel * level.refineChanceReduction))
            refineLevel++;
        else if (refineLevel > 0)
            refineLevel--;
    }
}
