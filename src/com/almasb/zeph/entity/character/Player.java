package com.almasb.zeph.entity.character;

import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.Experience;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.ID;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.item.Armor;
import com.almasb.zeph.entity.item.EquippableItem;
import com.almasb.zeph.entity.item.Weapon;
import com.almasb.zeph.entity.item.Weapon.WeaponType;

/**
 * Actual user
 *
 * @author Almas Baimagambetov
 *
 */
public final class Player extends GameCharacter {

    private static final long serialVersionUID = 7025558302171610110L;

    /**
     * Gameplay constants
     */
    private static final int MAX_LEVEL_BASE = 100,
            MAX_LEVEL_STAT = 100,
            MAX_LEVEL_JOB = 60,
            MAX_ATTRIBUTE = 100,
            ATTRIBUTE_POINTS_PER_LEVEL = 3;

    /**
     * Holds experience needed for each level
     */
    private static final int[] EXP_NEEDED_BASE = new int[MAX_LEVEL_BASE];
    private static final int[] EXP_NEEDED_STAT = new int[MAX_LEVEL_STAT];
    private static final int[] EXP_NEEDED_JOB = new int[MAX_LEVEL_JOB];

    static {
        /**
         * By what value should experience needed for next level
         * increase per level
         */
        float EXP_NEEDED_INC_BASE = 1.75f;
        float EXP_NEEDED_INC_STAT = 1.5f;
        float EXP_NEEDED_INC_JOB  = 2.25f;

        int EXP_NEEDED_FOR_LEVEL2 = 10;

        EXP_NEEDED_BASE[0] = EXP_NEEDED_FOR_LEVEL2;
        EXP_NEEDED_STAT[0] = EXP_NEEDED_FOR_LEVEL2;
        EXP_NEEDED_JOB[0]  = EXP_NEEDED_FOR_LEVEL2;
        for (int i = 1; i < EXP_NEEDED_BASE.length; i++) {
            EXP_NEEDED_BASE[i] = (int) (EXP_NEEDED_BASE[i-1] * EXP_NEEDED_INC_BASE) + 2 * i;

            if (i < EXP_NEEDED_STAT.length)
                EXP_NEEDED_STAT[i] = (int) (EXP_NEEDED_STAT[i-1] * EXP_NEEDED_INC_STAT) + i;

            if (i < EXP_NEEDED_JOB.length)
                EXP_NEEDED_JOB[i]  = (int) (EXP_NEEDED_JOB[i-1] * EXP_NEEDED_INC_JOB) + 3 * i;
        }
    }

    /**
     * Current stat level
     */
    private int statLevel = 1;

    /**
     * Current job level
     */
    private int jobLevel = 1;

    private byte attributePoints = 0,
            skillPoints = 0;

    private int money = 0;
    private Inventory inventory = new Inventory();

    private Map<EquipPlace, EquippableItem> equip = new HashMap<>();

    public Player(String name, GameCharacterClass charClass) {
        super(1000, name, "Player", "player.png", charClass);

        for (EquipPlace p : EquipPlace.values()) {
            equip.put(p, (EquippableItem) EntityManager.getItemByID(p.emptyID));
        }
    }

    /**
     * Increases player's experience
     *
     * @param gainedXP
     * @return
     *          true if player gained new base level
     */
    public boolean gainXP(final Experience gainedXP) {
        xp.add(gainedXP);
        if (xp.stat >= EXP_NEEDED_STAT[statLevel-1]) {
            statLevelUp();
            xp.stat = 0;
        }
        if (xp.job >= EXP_NEEDED_JOB[jobLevel-1]) {
            jobLevelUp();
            xp.job = 0;
        }
        if (xp.base >= EXP_NEEDED_BASE[getBaseLevel()-1]) {
            baseLevelUp();
            xp.base = 0;
            return true;
        }
        return false;
    }

    public void baseLevelUp() {
        setBaseLevel(getBaseLevel() + 1);
        calculateStats();
        restoreHP(getTotalStat(Stat.MAX_HP));
        restoreSP(getTotalStat(Stat.MAX_SP));
    }

    public void statLevelUp() {
        statLevel++;
        attributePoints += ATTRIBUTE_POINTS_PER_LEVEL;
    }

    public void jobLevelUp() {
        if (++jobLevel > 1) // 10
            skillPoints++;
    }

    public boolean hasAttributePoints() {
        return attributePoints > 0;
    }

    public boolean hasSkillPoints() {
        return skillPoints > 0;
    }

    public void increaseAttr(Attribute attr) {
        int value = getBaseAttribute(attr);
        if (value < MAX_ATTRIBUTE) {
            setAttribute(attr, value + 1);
            attributePoints--;
        }
    }

    public void increaseSkillLevel(int skillCode) {
        if (skillCode >= skills.length)
            return;

        if (skills[skillCode].levelUp())
            skillPoints--;
    }

    @Override
    public boolean canAttack() {
        Weapon w1 = (Weapon) getEquip(EquipPlace.RIGHT_HAND);
        Weapon w2 = (Weapon) getEquip(EquipPlace.LEFT_HAND);

        return getAtkTick() >= 50 / (1 + getTotalStat(Stat.ASPD)
                *w1.type.aspdFactor*w2.type.aspdFactor/100.0f);
    }

    public int getMoney() {
        return money;
    }

    public void incMoney(int value) {
        money += value;
    }

    public void equipWeapon(Weapon w) {
        inventory.removeItem(w);    // remove item from inventory to clear space

        if (w.type.ordinal() >= WeaponType.TWO_H_SWORD.ordinal()) {
            if (Inventory.MAX_SIZE - inventory.getSize() == 1
                    && !isFree(EquipPlace.RIGHT_HAND)
                    && !isFree(EquipPlace.LEFT_HAND)) {
                // ex case, when inventory is full and player tries to equip 2H weapon
                // but holds two 1H weapons
                inventory.addItem(w);
                return;
            }
            unEquipItem(EquipPlace.RIGHT_HAND);
            unEquipItem(EquipPlace.LEFT_HAND);
            equip.put(EquipPlace.RIGHT_HAND, w);
            equip.put(EquipPlace.LEFT_HAND, w);
        }
        else if (w.type == WeaponType.SHIELD || !isFree(EquipPlace.RIGHT_HAND)) {
            unEquipItem(EquipPlace.LEFT_HAND);
            equip.put(EquipPlace.LEFT_HAND, w);
        }
        else {  // normal 1H weapon
            unEquipItem(EquipPlace.RIGHT_HAND);
            equip.put(EquipPlace.RIGHT_HAND, w);
        }

        w.onEquip(this);            // put it on
    }

    public void equipArmor(Armor a) {
        inventory.removeItem(a);    // remove it first, so we can unequip our armor

        EquipPlace place;
        switch (a.type) {
            case BODY:
                place = EquipPlace.BODY;
                break;
            case HELM:
                place = EquipPlace.HELM;
                break;
            case SHOES:
            default:
                place = EquipPlace.SHOES;
                break;
        }

        unEquipItem(place);
        equip.put(place, a);
        a.onEquip(this);
    }

    public void unEquipItem(EquipPlace itemPlace) {
        if (isFree(itemPlace) || inventory.isFull())
            return; // no item at this place or inventory is full

        EquippableItem item = getEquip(itemPlace);

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            if (w.type.ordinal() >= WeaponType.TWO_H_SWORD.ordinal()) { // if 2 handed
                if (itemPlace == EquipPlace.RIGHT_HAND)
                    equip.put(EquipPlace.LEFT_HAND, EntityManager.getWeaponByID(ID.Weapon.HANDS));
                else
                    equip.put(EquipPlace.RIGHT_HAND, EntityManager.getWeaponByID(ID.Weapon.HANDS));
            }
        }

        item.onUnEquip(this);   // take item off
        inventory.addItem(item);    // put it in inventory
        equip.put(itemPlace, (EquippableItem) EntityManager.getItemByID(itemPlace.emptyID));    // replace with default
    }

    public boolean isFree(EquipPlace place) {
        return getEquip(place).getID() == place.emptyID;
    }

    public EquippableItem getEquip(EquipPlace place) {
        return equip.get(place);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Element getWeaponElement() {
        return getEquip(EquipPlace.RIGHT_HAND).getElement();
    }

    @Override
    public Element getArmorElement() {
        return getEquip(EquipPlace.BODY).getElement();
    }

    public int getJobLevel() {
        return jobLevel;
    }

    @Override
    public Entity toEntity() {
        Entity e = Entity.noType();
        e.setProperty("player_data", this);
        return e;
    }
}
