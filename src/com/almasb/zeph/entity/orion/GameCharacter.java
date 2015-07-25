package com.almasb.zeph.entity.orion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.almasb.zeph.entity.orion.Damage.DamageCritical;
import com.almasb.zeph.entity.orion.Damage.DamageType;
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

    /**
     * ID of the object in 1 instance of the game
     */
    private int runtimeID = 0;

    /**
     * Contains native character attribute values
     */
    protected Map<Attribute, Integer> attributes = new HashMap<>();

    /**
     * Contains attribute values given by equipped items or effects
     */
    private Map<Attribute, Integer> bAttributes = new HashMap<>();

    /**
     * Contains native character stats calculated from both
     * {@link #attributes} and {@link #bAttributes}
     */
    private Map<Stat, Float> stats = new HashMap<>();

    /**
     * Contains stats given by equipped items or effects
     */
    private Map<Stat, Float> bStats = new HashMap<>();

    /**
     * Statuses currently affecting this character
     */
    private List<StatusEffect> statuses = new ArrayList<>();

    /**
     * Effects currently placed on this character
     */
    private List<Effect> effects = new ArrayList<>();

    // TODO: transient javafx properties bound to primitives ?

    protected Skill[] skills;

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

        for (Attribute attr : Attribute.values()) {
            attributes.put(attr, 1);
            bAttributes.put(attr, 1);
        }

        for (Stat stat : Stat.values()) {
            stats.put(stat, 0.0f);
            bStats.put(stat, 0.0f);
        }

        calculateStats();
        setHP((int)getTotalStat(Stat.MAX_HP));   // set current hp/sp to max
        setSP((int)getTotalStat(Stat.MAX_SP));
    }

    public int getBaseAttribute(Attribute attr) {
        return attributes.get(attr);
    }

    public float getBaseStat(Stat stat) {
        return stats.get(stat);
    }

    public int getBonusAttribute(Attribute attr) {
        return bAttributes.get(attr);
    }

    public float getBonusStat(Stat stat) {
        return bStats.get(stat);
    }

    /**
     *
     * @param attr
     * @return
     *          total value for attr, including bonuses
     */
    public int getTotalAttribute(Attribute attr) {
        return getBaseAttribute(attr) + getBonusAttribute(attr);
    }

    /**
     *
     * @param stat
     * @return
     *          total value for stat, including bonuses
     */
    public float getTotalStat(Stat stat) {
        return getBaseStat(stat) + getBonusStat(stat);
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
        bAttributes.put(attr, bAttributes.get(attr) + bonus);
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
        bStats.put(stat, bStats.get(stat) + bonus);
    }

    /**
     * Character stats are directly affected by his attributes
     * Therefore any change in attributes must be followed by
     * call to this method
     */
    public final void calculateStats() {
        int strength    = getTotalAttribute(Attribute.STRENGTH);   // calculate totals first
        int vitality    = getTotalAttribute(Attribute.VITALITY);
        int dexterity   = getTotalAttribute(Attribute.DEXTERITY);
        int agility     = getTotalAttribute(Attribute.AGILITY);
        int intellect   = getTotalAttribute(Attribute.INTELLECT);
        int wisdom      = getTotalAttribute(Attribute.WISDOM);
        int willpower   = getTotalAttribute(Attribute.WILLPOWER);
        int perception  = getTotalAttribute(Attribute.PERCEPTION);
        int luck        = getTotalAttribute(Attribute.LUCK);

        // None of these formulae are finalised yet and need to be checked for game balance
        // only calculate "native" base stats

        float MODIFIER_VERY_LOW = 0.1f,
                MODIFIER_LOW = 0.2f,
                MODIFIER_MEDIUM = 0.3f,
                MODIFIER_HIGH = 0.4f,
                MODIFIER_VERY_HIGH = 0.5f,
                MODIFIER_LEVEL = 0.25f;

        float maxHP = (vitality*MODIFIER_VERY_HIGH + strength*MODIFIER_MEDIUM + MODIFIER_LEVEL*baseLevel + (vitality/10))
                * charClass.hp;

        float maxSP = (wisdom*MODIFIER_VERY_HIGH + intellect*MODIFIER_MEDIUM + willpower*MODIFIER_VERY_LOW + MODIFIER_LEVEL*baseLevel + (wisdom/10))
                * charClass.sp;

        float hpRegen = 1 + vitality * MODIFIER_VERY_LOW;
        float spRegen = 2 + wisdom * MODIFIER_VERY_LOW;

        float atk = strength*MODIFIER_VERY_HIGH + dexterity*MODIFIER_MEDIUM + perception*MODIFIER_LOW + luck*MODIFIER_VERY_LOW
                + baseLevel + (strength/10)*( (strength/10)+1);

        float matk = intellect*MODIFIER_VERY_HIGH + wisdom*MODIFIER_HIGH + willpower*MODIFIER_HIGH + dexterity*MODIFIER_MEDIUM
                + perception*MODIFIER_LOW + luck*MODIFIER_VERY_LOW + baseLevel + (intellect/10)*( (intellect/10)+1);

        float def = vitality*MODIFIER_MEDIUM + perception*MODIFIER_LOW + strength*MODIFIER_VERY_LOW
                + MODIFIER_LEVEL*baseLevel + (vitality/20)*(charClass.hp/10);

        float mdef = willpower*MODIFIER_HIGH + wisdom*MODIFIER_MEDIUM + perception*MODIFIER_LOW + intellect*MODIFIER_VERY_LOW
                + MODIFIER_LEVEL*baseLevel + (willpower/20)*(intellect/10);

        float aspd = agility*MODIFIER_VERY_HIGH + dexterity*MODIFIER_LOW;

        float mspd = dexterity*MODIFIER_MEDIUM + willpower*MODIFIER_VERY_LOW + wisdom*MODIFIER_VERY_LOW
                + intellect*MODIFIER_VERY_LOW + perception*MODIFIER_VERY_LOW + luck*MODIFIER_VERY_LOW;

        float critChance = luck*MODIFIER_VERY_HIGH + dexterity*MODIFIER_VERY_LOW + perception*MODIFIER_VERY_LOW
                + wisdom*MODIFIER_VERY_LOW;

        float mcritChance = luck*MODIFIER_HIGH + willpower*MODIFIER_LOW + perception*MODIFIER_VERY_LOW;

        float critDmg  = 2 + luck*0.01f;
        float mcritDmg = 2 + luck*0.01f;

        stats.put(Stat.MAX_HP, maxHP);
        stats.put(Stat.MAX_SP, maxSP);
        stats.put(Stat.HP_REGEN, hpRegen);
        stats.put(Stat.SP_REGEN, spRegen);
        stats.put(Stat.ATK, atk);
        stats.put(Stat.MATK, matk);
        stats.put(Stat.DEF, def);
        stats.put(Stat.MDEF, mdef);
        stats.put(Stat.ASPD, aspd);
        stats.put(Stat.MSPD, mspd);
        stats.put(Stat.CRIT_CHANCE, critChance);
        stats.put(Stat.MCRIT_CHANCE, mcritChance);
        stats.put(Stat.CRIT_DMG, critDmg);
        stats.put(Stat.MCRIT_DMG, mcritDmg);
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
     *          true if character is under status effect,
     *          false otherwise
     */
    public boolean hasStatusEffect(Status status) {
        return statuses.contains(status);
    }

    public GameCharacterClass getGameCharacterClass() {
        return charClass;
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
                hp = Math.min((int)getTotalStat(Stat.MAX_HP), (int)(hp + getTotalStat(Stat.HP_REGEN)));
                sp = Math.min((int)getTotalStat(Stat.MAX_SP), (int)(sp + getTotalStat(Stat.SP_REGEN)));
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
        return atkTick >= 50 / (1 + getTotalStat(Stat.ASPD)/100.0f);
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
    public Damage attack(GameCharacter target) {
        atkTick = 0;
        return dealPhysicalDamage(target, getTotalStat(Stat.ATK) + 1.25f * GameMath.random(baseLevel), this.getWeaponElement());
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
    public Damage dealPhysicalDamage(GameCharacter target, float baseDamage, Element element) {
        boolean crit = false;
        if (GameMath.checkChance(getTotalStat(Stat.CRIT_CHANCE))) {
            baseDamage *= getTotalStat(Stat.CRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = element.getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(Stat.ARM)) * baseDamage / 100.0f - target.getTotalStat(Stat.DEF);

        int totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0);
        target.hp -= totalDamage;

        return new Damage(DamageType.PHYSICAL, element, totalDamage, crit ? DamageCritical.TRUE : DamageCritical.FALSE);
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
    public Damage dealPhysicalDamage(GameCharacter target, float baseDamage) {
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
    public Damage dealMagicalDamage(GameCharacter target, float baseDamage, Element element) {
        boolean crit = false;
        if (GameMath.checkChance(getTotalStat(Stat.MCRIT_CHANCE))) {
            baseDamage *= getTotalStat(Stat.MCRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = element.getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(Stat.MARM)) * baseDamage / 100.0f - target.getTotalStat(Stat.MDEF);

        int totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0);
        target.hp -= totalDamage;

        return new Damage(DamageType.MAGICAL, element, totalDamage, crit ? DamageCritical.TRUE : DamageCritical.FALSE);
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
    public Damage dealMagicalDamage(GameCharacter target, float baseDamage) {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deals the exact amount of damage to target as specified by
     * param dmg
     *
     * @param target
     * @param dmg
     */
    public Damage dealPureDamage(GameCharacter target, float dmg) {
        int damage = (int)dmg;
        target.hp -= damage;
        return new Damage(DamageType.PURE, Element.NEUTRAL, damage, DamageCritical.FALSE);
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
