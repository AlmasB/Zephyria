package com.almasb.zeph.entity.orion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.almasb.zeph.entity.orion.StatusEffect.Status;

/**
 * Essentially alive game object
 * Enemies/NPCs/players
 *
 * @author Almas Baimagambetov
 *
 */
public abstract class GameCharacter extends GameEntity implements java.io.Serializable {
    private static final long serialVersionUID = -4840633591092062960L;

    public static class Experience implements java.io.Serializable {
        private static final long serialVersionUID = 2762180993708324531L;
        public int base, stat, job;
        public Experience(int base, int stat, int job) {
            this.base = base;
            this.stat = stat;
            this.job = job;
        }
        public void add(Experience xp) {
            this.base += xp.base;
            this.stat += xp.stat;
            this.job += xp.job;
        }
    }

    /**
     * Stats of a game character
     *
     */
    public enum Stat {
        MAX_HP, MAX_SP, ATK, MATK, DEF, MDEF, ARM, MARM, ASPD, MSPD, CRIT_CHANCE, MCRIT_CHANCE, CRIT_DMG, MCRIT_DMG, HP_REGEN, SP_REGEN
    }

    /**
     * ID of the object in 1 instance of the game
     */
    private int runtimeID = 0;

    /**
     * These allow convenient access to {@link #attributes} and {@link #stats} arrays
     * to retrieve particular attribute value
     * These essentially represent indexes
     *
     * Note: the order is based on the order of attributes in
     * {@code Attribute} and {@code Stat} enums, so they have to match
     */
    public static final int STR = 0,    // ATTRIBUTES
            VIT = 1,
            DEX = 2,
            AGI = 3,
            INT = 4,
            WIS = 5,
            WIL = 6,
            PER = 7,
            LUC = 8,
            // STATS
            MAX_HP = 0,
            MAX_SP = 1,
            ATK = 2,
            MATK = 3,
            DEF = 4,    // flat damage reduction
            MDEF = 5,
            ARM = 6,    // % damage reduction
            MARM = 7,
            ASPD = 8,   // attack speed
            MSPD = 9,
            CRIT_CHANCE = 10,
            MCRIT_CHANCE = 11,
            CRIT_DMG = 12,
            MCRIT_DMG = 13,
            HP_REGEN = 14,
            SP_REGEN = 15;

    protected byte[] attributes = new byte[9];    // we have 9 attributes
    protected byte[] bAttributes = new byte[9];   // on top of native attributes items can give bonuses
    protected float[] stats = new float[16];        // 16 stats
    protected float[] bStats = new float[16];       // bonus stats given by item

    protected Skill[] skills;

    private List<StatusEffect> statuses = new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();

    protected int baseLevel = 1, atkTick = 0,
            hp = 0, sp = 0; // these are current hp/sp

    protected GameCharacterClass charClass;

    protected Experience xp = new Experience(0, 0, 0);

    public GameCharacter(int id, String name, String description, String textureName, GameCharacterClass charClass) {
        super(id, name, description, textureName);
        this.charClass = charClass;
        init();
    }

    public void init() {
        this.skills = new Skill[charClass.skillIDs.length];

        for (int i = 0; i < skills.length; i++)
            skills[i] = ObjectManager.getSkillByID(charClass.skillIDs[i]);

        Arrays.fill(attributes, (byte)1); // set all attributes to 1, that's the minimum

        calculateStats();
        setHP((int)getTotalStat(MAX_HP));   // set current hp/sp to max
        setSP((int)getTotalStat(MAX_SP));
    }

    public int getBaseAttribute(int attr) {
        return attributes[attr];
    }

    public float getBaseStat(Stat stat) {
        return stats[stat.ordinal()];
    }

    public int getBonusAttribute(int attr) {
        return bAttributes[attr];
    }

    public float getBonusStat(Stat stat) {
        return bStats[stat.ordinal()];
    }

    /**
     *
     * @param attr
     *              one of the constants for attr, STR = 0, LUC = 8
     * @return
     *          total value for attr, including bonuses
     */
    public int getTotalAttribute(int attr) {
        return attributes[attr] + bAttributes[attr];
    }

    public int getTotalAttribute(Attribute attr) {
        return attributes[attr.ordinal()] + bAttributes[attr.ordinal()];
    }

    /**
     *
     * @param stat
     *              one of the constants for stat, MAX_HP = 0, SP_REGEN = 15
     * @return
     *          total value for stat, including bonuses
     */
    public float getTotalStat(int stat) {
        return stats[stat] + bStats[stat];
    }

    public float getTotalStat(Stat stat) {
        return stats[stat.ordinal()] + bStats[stat.ordinal()];
    }

    /**
     * Character stats are directly affected by his attributes
     * Therefore any change in attributes must be followed by
     * call to this method
     */
    public final void calculateStats() {
        int strength    = getTotalAttribute(STR);   // calculate totals first
        int vitality    = getTotalAttribute(VIT);
        int dexterity   = getTotalAttribute(DEX);
        int agility     = getTotalAttribute(AGI);
        int intellect   = getTotalAttribute(INT);
        int wisdom      = getTotalAttribute(WIS);
        int willpower   = getTotalAttribute(WIL);
        int perception  = getTotalAttribute(PER);
        int luck        = getTotalAttribute(LUC);

        // None of these formulae are finalised yet and need to be checked for game balance
        // only calculate "native" base stats

        float MODIFIER_VERY_LOW = 0.1f,
                MODIFIER_LOW = 0.2f,
                MODIFIER_MEDIUM = 0.3f,
                MODIFIER_HIGH = 0.4f,
                MODIFIER_VERY_HIGH = 0.5f,
                MODIFIER_LEVEL = 0.25f;

        stats[MAX_HP] = (vitality*MODIFIER_VERY_HIGH + strength*MODIFIER_MEDIUM + MODIFIER_LEVEL*baseLevel + (vitality/10))
                * charClass.hp;

        stats[MAX_SP] = (wisdom*MODIFIER_VERY_HIGH + intellect*MODIFIER_MEDIUM + willpower*MODIFIER_VERY_LOW + MODIFIER_LEVEL*baseLevel + (wisdom/10))
                * charClass.sp;

        stats[ATK] = strength*MODIFIER_VERY_HIGH + dexterity*MODIFIER_MEDIUM + perception*MODIFIER_LOW + luck*MODIFIER_VERY_LOW
                + baseLevel + (strength/10)*( (strength/10)+1);

        stats[MATK] = intellect*MODIFIER_VERY_HIGH + wisdom*MODIFIER_HIGH + willpower*MODIFIER_HIGH + dexterity*MODIFIER_MEDIUM
                + perception*MODIFIER_LOW + luck*MODIFIER_VERY_LOW + baseLevel + (intellect/10)*( (intellect/10)+1);

        stats[DEF] = vitality*MODIFIER_MEDIUM + perception*MODIFIER_LOW + strength*MODIFIER_VERY_LOW
                + MODIFIER_LEVEL*baseLevel + (vitality/20)*(charClass.hp/10);

        stats[MDEF] = willpower*MODIFIER_HIGH + wisdom*MODIFIER_MEDIUM + perception*MODIFIER_LOW + intellect*MODIFIER_VERY_LOW
                + MODIFIER_LEVEL*baseLevel + (willpower/20)*(intellect/10);

        stats[ASPD] = agility*MODIFIER_VERY_HIGH + dexterity*MODIFIER_LOW;

        stats[MSPD] = dexterity*MODIFIER_MEDIUM + willpower*MODIFIER_VERY_LOW + wisdom*MODIFIER_VERY_LOW
                + intellect*MODIFIER_VERY_LOW + perception*MODIFIER_VERY_LOW + luck*MODIFIER_VERY_LOW;

        stats[CRIT_CHANCE] = luck*MODIFIER_VERY_HIGH + dexterity*MODIFIER_VERY_LOW + perception*MODIFIER_VERY_LOW
                + wisdom*MODIFIER_VERY_LOW;

        stats[MCRIT_CHANCE] = luck*MODIFIER_HIGH + willpower*MODIFIER_LOW + perception*MODIFIER_VERY_LOW;

        stats[CRIT_DMG]  = 2 + luck*0.01f;
        stats[MCRIT_DMG] = 2 + luck*0.01f;

        stats[HP_REGEN] = 1 + vitality * MODIFIER_VERY_LOW;
        stats[SP_REGEN] = 2 + wisdom * MODIFIER_VERY_LOW;
    }

    /**
     * Apply bonus attr that comes from item for example
     *
     * @param attr
     *              attr
     * @param bonus
     *              value
     */
    public void addBonusAttribute(Attribute attr, int bonus) {
        bAttributes[attr.ordinal()] += bonus;
    }

    /**
     * Apply bonus stat that comes from item for example
     *
     * @param stat
     *              stat
     * @param bonus
     *              value
     */
    public void addBonusStat(Stat stat, int bonus) {
        bStats[stat.ordinal()] += bonus;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setSP(int sp) {
        this.sp = sp;
    }

    /**
     *
     * @return
     *          current HP
     */
    public int getHP() {
        return hp;
    }

    /**
     *
     * @return
     *          current SP
     */
    public int getSP() {
        return sp;
    }

    /**
     *
     * @param status
     * @return
     *          true if character is under "@param status" status effect
     *          false otherwise
     */
    public boolean hasStatusEffect(Status status) {
        return statuses.contains(status);
    }

    /**
     *
     * @return
     *          a skill array for this character
     */
    public Skill[] getSkills() {
        return skills;
    }

    public void setRuntimeID(int id) {
        runtimeID = id;
    }

    public int getRuntimeID() {
        return runtimeID;
    }

    public abstract Element getWeaponElement();
    public abstract Element getArmorElement();

    public void addEffect(Effect e) {
        for (Iterator<Effect> it = effects.iterator(); it.hasNext(); ) {
            Effect eff = it.next();
            if (eff.sourceID == e.sourceID) {
                eff.onEnd(this);
                it.remove();
                break;
            }
        }

        e.onBegin(this);
        effects.add(e);
    }

    public void addStatusEffect(StatusEffect e) {
        statuses.add(e);
    }

    protected void updateEffects() {
        for (Iterator<Effect> it = effects.iterator(); it.hasNext(); ) {
            Effect e = it.next();
            e.reduceDuration(0.02f);
            if (e.getDuration() <= 0) {
                e.onEnd(this);
                it.remove();
            }
        }
    }

    private void updateStatusEffects() {
        for (Iterator<StatusEffect> it = statuses.iterator(); it.hasNext(); ) {
            StatusEffect e = it.next();
            e.reduceDuration(0.02f);
            if (e.getDuration() <= 0) {
                it.remove();
            }
        }
    }

    protected float regenTick = 0.0f;

    public void update() {
        // HP/SP regen
        regenTick += 0.02f;

        if (regenTick >= 2.0f) {    // 2 secs
            if (!hasStatusEffect(Status.POISONED)) {
                hp = Math.min((int)getTotalStat(MAX_HP), (int)(hp + getTotalStat(HP_REGEN)));
                sp = Math.min((int)getTotalStat(MAX_SP), (int)(sp + getTotalStat(SP_REGEN)));
            }
            regenTick = 0.0f;
        }

        if (!canAttack()) atkTick++;

        // skill cooldowns

        for (Skill sk : skills) {
            if (sk.active) {
                if (sk.getCurrentCooldown() > 0) {
                    sk.reduceCurrentCooldown(0.02f);
                }
            }
            else {  // reapply passive skills
                if (sk.getLevel() > 0)
                    sk.use(this, null);
            }
        }

        // check buffs
        updateEffects();
        updateStatusEffects();

        calculateStats();
    }

    /**
     * @return
     *          if character is ready to perform basic attack
     *          based on his ASPD
     */
    public boolean canAttack() {
        return atkTick >= 50 / (1 + getTotalStat(GameCharacter.ASPD)/100.0f);
    }

    /**
     * Change this characters game class to @param cl
     *
     * @param cl
     *          game character class to change to
     */
    public void changeClass(GameCharacterClass cl) {
        this.charClass = cl;
        Skill[] tmpSkills = new Skill[skills.length + charClass.skillIDs.length];

        int j = 0;
        for (j = 0; j < skills.length; j++)
            tmpSkills[j] = skills[j];

        for (int i = 0; i < charClass.skillIDs.length; i++)
            tmpSkills[j++] = ObjectManager.getSkillByID(charClass.skillIDs[i]);

        this.skills = tmpSkills;
    }

    /**
     * Performs basic attack with equipped weapon
     * Damage is physical and element depends on weapon element
     *
     * @param target
     *               target being attacked
     * @return
     *          damage dealt
     */
    public int attack(GameCharacter target) {
        atkTick = 0;
        return dealPhysicalDamage(target, this.getTotalStat(ATK) + 1.25f * GameMath.random(baseLevel), this.getWeaponElement());
    }

    /**
     * Deals physical damage to target. The damage is reduced by armor and defense
     * The damage is affected by attacker's weapon element and by target's armor element
     *
     * @param target
     * @param baseDamage
     * @param element
     * @return
     */
    public int dealPhysicalDamage(GameCharacter target, float baseDamage, Element element) {
        boolean crit = false;
        if (GameMath.checkChance(getTotalStat(CRIT_CHANCE))) {
            baseDamage *= getTotalStat(CRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = element.getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(ARM)) * baseDamage / 100.0f - target.getTotalStat(DEF);

        int totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0);
        target.hp -= totalDamage;

        // set the negative bit on to indicate crit
        if (crit)
            totalDamage = totalDamage | (1 << 31);
        return totalDamage;
    }

    /**
     * Deals physical damage of type NEUTRAL to target.
     * The damage is reduced by target's armor and DEF
     *
     * @param target
     * @param baseDamage
     *
     * @return
     *          damage dealt
     */
    public int dealPhysicalDamage(GameCharacter target, float baseDamage) {
        return dealPhysicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deal magical damage of type param element to target. The damage is reduced by target's
     * magical armor and MDEF
     *
     * @param target
     * @param baseDamage
     *
     * @return
     *          damage dealt
     */
    public int dealMagicalDamage(GameCharacter target, float baseDamage, Element element) {
        if (GameMath.checkChance(getTotalStat(MCRIT_CHANCE))) {
            baseDamage *= getTotalStat(MCRIT_DMG);
        }

        float elementalDamageModifier = element.getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(MARM)) * baseDamage / 100.0f - target.getTotalStat(MDEF);

        int totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0);
        target.hp -= totalDamage;

        return totalDamage;
    }

    /**
     * Deal magical damage of type NEUTRAL to target. The damage is reduced by target's
     * magical armor and MDEF
     *
     * @param target
     * @param baseDamage
     *
     * @return
     *          damage dealt
     */
    public int dealMagicalDamage(GameCharacter target, float baseDamage) {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deals the exact amount of damage to target as specified by
     * param dmg
     *
     * @param target
     * @param dmg
     */
    public void dealPureDamage(GameCharacter target, float dmg) {
        target.hp -= dmg;
    }

    /**
     *
     *
     * @param skillCode
     * @param target
     * @return
     */
//    public SkillUseResult useSkill(int skillCode, GameCharacter target) {
//        if (skillCode >= skills.length || hasStatusEffect(Status.SILENCED))
//            return SkillUseResult.DEFAULT_FALSE;
//
//        Skill sk = skills[skillCode];
//        if (sk != null && sk.active && sk.getLevel() > 0 && sk.getCurrentCooldown() == 0) {
//            if (this.sp >= sk.getManaCost()) {
//                this.sp -= sk.getManaCost();
//                sk.use(this, target);
//                // successful use of skill
//                return sk.getUseResult();
//            }
//        }
//
//        return SkillUseResult.DEFAULT_FALSE;
//    }
}
