package com.almasb.zeph.entity.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.Essence;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.combat.Rune;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerControl;

/**
 * Represents an item that can be equipped.
 * In other words, a weapon or a type of armor
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public abstract class EquippableItem extends DescriptionComponent {

    private static final long serialVersionUID = -1091033469035972887L;

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

    public void onEquip(PlayerControl ch) {
        for (Rune r : defaultRunes)
            ch.addBonusAttribute(r.attribute, r.bonus);
        for (Rune r : runes)
            ch.addBonusAttribute(r.attribute, r.bonus);
        if (essence != null)
            ch.addBonusStat(essence.stat, essence.bonus);
    }

    public void onUnEquip(PlayerControl ch) {
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
