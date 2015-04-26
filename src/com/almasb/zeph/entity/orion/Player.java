package com.almasb.zeph.entity.orion;

import uk.ac.brighton.uni.ab607.mmorpg.common.object.Armor;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.ID;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.ObjectManager;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Weapon;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Weapon.WeaponType;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.Element;

/**
 * Actual user
 *
 * @author Almas Baimagambetov
 *
 */
public class Player extends GameCharacter {

    private static final long serialVersionUID = 7025558302171610110L;

    /**
     * Gameplay constants
     */
    private static final int MAX_LEVEL_BASE = 100,
            MAX_LEVEL_STAT = 100,
            MAX_LEVEL_JOB = 60,
            MAX_ATTRIBUTE = 100,
            ATTRIBUTE_POINTS_PER_LEVEL = 3,
            EXP_NEEDED_FOR_LEVEL2 = 10;

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

    private int statLevel = 1, jobLevel = 1;

    private byte attributePoints = 0,
            skillPoints = 0;

    private int money = 0;
    private Inventory inventory = new Inventory();

    public static final int HELM = 0,
            BODY = 1,
            SHOES = 2,
            RIGHT_HAND = 3,
            LEFT_HAND = 4;

    private EquippableItem[] equip = new EquippableItem[5];

    public Player(String name, GameCharacterClass charClass) {
        super(1000, name, "Player", "player.png", charClass);

        for (int i = HELM; i <= LEFT_HAND; i++) {   // helm 0, body 1, shoes 2 so we get 5000, 5001, 5002
            equip[i] = i >= RIGHT_HAND ? ObjectManager.getWeaponByID(ID.Weapon.HANDS) : ObjectManager.getArmorByID(5000 + i);
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
        if (xp.base >= EXP_NEEDED_BASE[baseLevel-1]) {
            baseLevelUp();
            xp.base = 0;
            return true;
        }
        return false;
    }

    public void baseLevelUp() {
        baseLevel++;
        calculateStats();
        this.hp = (int)this.getTotalStat(Stat.MAX_HP);
        this.sp = (int)this.getTotalStat(Stat.MAX_SP);
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

    public void increaseAttr(int attr) {
        if (attributes[attr] < MAX_ATTRIBUTE) {
            attributes[attr]++;
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
        Weapon w1 = (Weapon) this.getEquip(RIGHT_HAND);
        Weapon w2 = (Weapon) this.getEquip(LEFT_HAND);

        return atkTick >= 50 / (1 + getTotalStat(GameCharacter.ASPD)
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
            if (Inventory.MAX_SIZE - inventory.getSize() == 1 && !isFree(RIGHT_HAND) && !isFree(LEFT_HAND)) {
                // ex case, when inventory is full and player tries to equip 2H weapon
                // but holds two 1H weapons
                inventory.addItem(w);
                return;
            }
            unEquipItem(RIGHT_HAND);
            unEquipItem(LEFT_HAND);
            equip[RIGHT_HAND] = w;
            equip[LEFT_HAND] = w;
        }
        else if (w.type == WeaponType.SHIELD || !isFree(RIGHT_HAND)) {
            unEquipItem(LEFT_HAND);
            equip[LEFT_HAND] = w;
        }
        else {  // normal 1H weapon
            unEquipItem(RIGHT_HAND);
            equip[RIGHT_HAND] = w;
        }

        w.onEquip(this);            // put it on
    }

    public void equipArmor(Armor a) {
        inventory.removeItem(a);    // remove it first, so we can unequip our armor
        unEquipItem(a.type.ordinal());  // just because place number made to match ArmorType enum
        equip[a.type.ordinal()] = a;
        a.onEquip(this);
    }

    public void unEquipItem(int itemPlace) {
        if (isFree(itemPlace) || inventory.isFull())
            return; // no item at this place

        if (equip[itemPlace] instanceof Weapon) {
            Weapon w = (Weapon) equip[itemPlace];
            if (w.type.ordinal() >= WeaponType.TWO_H_SWORD.ordinal()) { // if 2 handed
                if (itemPlace == RIGHT_HAND)
                    equip[LEFT_HAND]  = ObjectManager.getWeaponByID(ID.Weapon.HANDS);
                else
                    equip[RIGHT_HAND] = ObjectManager.getWeaponByID(ID.Weapon.HANDS);
            }
        }

        equip[itemPlace].onUnEquip(this);   // take item off
        inventory.addItem(equip[itemPlace]);    // put it in inventory
        equip[itemPlace] = itemPlace >= RIGHT_HAND ? ObjectManager.getWeaponByID(ID.Weapon.HANDS) : ObjectManager.getArmorByID(5000 + itemPlace);    // replace with default
    }

    public boolean isFree(int itemPlace) {
        return equip[itemPlace].id == ID.Weapon.HANDS || equip[itemPlace].id == ID.Armor.HAT
                || equip[itemPlace].id == ID.Armor.CLOTHES || equip[itemPlace].id == ID.Armor.SHOES;
    }

    public EquippableItem getEquip(int place) {
        return equip[place];
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Element getWeaponElement() {
        return getEquip(RIGHT_HAND).getElement();
    }

    @Override
    public Element getArmorElement() {
        return getEquip(BODY).getElement();
    }

    public void onDeath() {
        hp = (int)(0.25f*getTotalStat(MAX_HP));
        sp = (int)(0.25f*getTotalStat(MAX_SP));
    }

    public int getJobLevel() {
        return jobLevel;
    }

    @Override
    public Entity toEntity() {
        Entity e = new Entity("player");
        e.setProperty("player_data", this);
        return e;
    }
}
