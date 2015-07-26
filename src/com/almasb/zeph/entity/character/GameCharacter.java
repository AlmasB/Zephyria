package com.almasb.zeph.entity.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Damage;
import com.almasb.zeph.combat.Damage.DamageCritical;
import com.almasb.zeph.combat.Damage.DamageType;
import com.almasb.zeph.combat.Effect;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.combat.Skill;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.combat.StatusEffect;
import com.almasb.zeph.combat.StatusEffect.Status;
import com.almasb.zeph.entity.GameEntity;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 * Essentially alive game object Enemies/NPCs/players
 *
 * @author Almas Baimagambetov
 *
 */
public abstract class GameCharacter extends GameEntity {
    private static final long serialVersionUID = -4840633591092062960L;

    /**
     * Contains native character attribute values
     */
    private Map<Attribute, Integer> attributes = new HashMap<>();
    private transient Map<Attribute, ReadOnlyIntegerWrapper> attributeProperties = new HashMap<>();

    /**
     *
     * @param attr
     * @return base (native) character attribute value
     */
    public final int getBaseAttribute(Attribute attr) {
        return attributes.get(attr);
    }

    /**
     *
     * @param attr
     * @return base attribute value property
     */
    public final ReadOnlyIntegerProperty attributeProperty(Attribute attr) {
        return attributeProperties.get(attr).getReadOnlyProperty();
    }

    /**
     * Sets given attribute value
     *
     * @param attr
     * @param value
     */
    protected final void setAttribute(Attribute attr, int value) {
        attributes.put(attr, value);
        attributeProperties.get(attr).set(value);
    }

    /**
     * Contains attribute values given by equipped items or effects
     */
    private Map<Attribute, Integer> bAttributes = new HashMap<>();
    private transient Map<Attribute, ReadOnlyIntegerWrapper> bAttributeProperties = new HashMap<>();

    /**
     *
     * @param attr
     * @return bonus attribute value
     */
    public final int getBonusAttribute(Attribute attr) {
        return bAttributes.get(attr);
    }

    /**
     *
     * @param attr
     * @return bonus attr property
     */
    public final ReadOnlyIntegerProperty bAttributeProperty(Attribute attr) {
        return bAttributeProperties.get(attr).getReadOnlyProperty();
    }

    /**
     * Apply bonus attr that comes from item for example
     *
     * @param attr
     *            attr
     * @param bonus
     *            value
     */
    public void addBonusAttribute(Attribute attr, int bonus) {
        int value = getBonusAttribute(attr) + bonus;
        bAttributes.put(attr, value);
        bAttributeProperties.get(attr).set(value);
    }

    /**
     * Contains native character stats calculated from both {@link #attributes}
     * and {@link #bAttributes}
     */
    private Map<Stat, Float> stats = new HashMap<>();
    private transient Map<Stat, ReadOnlyIntegerWrapper> statProperties = new HashMap<>();

    /**
     *
     * @param stat
     * @return base (native) character stat
     */
    public final float getBaseStat(Stat stat) {
        return stats.get(stat);
    }

    /**
     *
     * @param stat
     * @return base stat property
     */
    public final ReadOnlyIntegerProperty statProperty(Stat stat) {
        return statProperties.get(stat).getReadOnlyProperty();
    }

    /**
     * Set base stat value
     *
     * @param stat
     * @param value
     */
    private void setBaseStat(Stat stat, float value) {
        stats.put(stat, value);
        statProperties.get(stat).set((int) value);
    }

    /**
     * Contains stats given by equipped items or effects
     */
    private Map<Stat, Float> bStats = new HashMap<>();
    private transient Map<Stat, ReadOnlyIntegerWrapper> bStatProperties = new HashMap<>();

    /**
     *
     * @param stat
     * @return bonus stat value
     */
    public final float getBonusStat(Stat stat) {
        return bStats.get(stat);
    }

    /**
     *
     * @param stat
     * @return bonus stat property
     */
    public final ReadOnlyIntegerProperty bStatProperty(Stat stat) {
        return bStatProperties.get(stat).getReadOnlyProperty();
    }

    /**
     * Apply bonus stat that comes from item for example
     *
     * @param stat
     *            stat
     * @param bonus
     *            value
     */
    public final void addBonusStat(Stat stat, int bonus) {
        float value = getBonusStat(stat) + bonus;
        bStats.put(stat, value);
        bStatProperties.get(stat).set((int) value);
    }

    /**
     *
     * @param attr
     * @return total value for attr, including bonuses
     */
    public int getTotalAttribute(Attribute attr) {
        return getBaseAttribute(attr) + getBonusAttribute(attr);
    }

    /**
     *
     * @param stat
     * @return total value for stat, including bonuses
     */
    public float getTotalStat(Stat stat) {
        return getBaseStat(stat) + getBonusStat(stat);
    }

    /**
     * Statuses currently affecting this character
     */
    private List<StatusEffect> statuses = new ArrayList<>();

    /**
     *
     * @param status
     * @return true if character is under status effect, false otherwise
     */
    public boolean hasStatusEffect(Status status) {
        return statuses.contains(status);
    }

    /**
     * Apply status effect
     *
     * @param e
     */
    public void addStatusEffect(StatusEffect e) {
        statuses.add(e);
    }

    /**
     * Effects currently placed on this character
     */
    private List<Effect> effects = new ArrayList<>();

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).
     *
     * @param e
     */
    public void addEffect(Effect e) {
        for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
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

    /**
     * Current base level
     */
    private int baseLevel = 1;
    private transient ReadOnlyIntegerWrapper baseLevelProperty = new ReadOnlyIntegerWrapper(
            baseLevel);

    /**
     *
     * @return base level
     */
    public final int getBaseLevel() {
        return baseLevel;
    }

    /**
     *
     * @return base level property
     */
    public final ReadOnlyIntegerProperty baseLevelProperty() {
        return baseLevelProperty.getReadOnlyProperty();
    }

    /**
     * Sets base level. Also updates baseLevelProperty
     *
     * @param level
     */
    protected final void setBaseLevel(int level) {
        baseLevel = level;
        baseLevelProperty.set(level);
    }

    /**
     * Current hp
     */
    private int hp = 0;
    private transient ReadOnlyIntegerWrapper hpProperty = new ReadOnlyIntegerWrapper(
            hp);

    /**
     *
     * @return current hp
     */
    public final int getHP() {
        return hp;
    }

    /**
     *
     * @return current hp property
     */
    public final ReadOnlyIntegerProperty hpProperty() {
        return hpProperty.getReadOnlyProperty();
    }

    /**
     * Sets current hp. If the value is outside [0..getTotalStat(Stat.MAX_HP)],
     * the value will be clamped.
     *
     * @param value
     */
    public final void setHP(int value) {
        if (value < 0)
            value = 0;
        if (value > getTotalStat(Stat.MAX_HP))
            value = (int) getTotalStat(Stat.MAX_HP);

        hp = value;
        hpProperty.set(value);
    }

    /**
     * Restores hp. HP will not go outside getTotalStat(Stat.MAX_HP). No effect
     * if the value is negative.
     *
     * @param value
     */
    protected final void restoreHP(float value) {
        if (value <= 0)
            return;
        setHP(getHP() + (int) value);
    }

    /**
     * Takes away value from hp. HP will not drop below 0. No effect if the
     * value is negative.
     *
     * @param value
     */
    protected final void damageHP(float value) {
        if (value <= 0)
            return;
        setHP(getHP() - (int) value);
    }

    /**
     * Current sp
     */
    private int sp = 0;
    private transient ReadOnlyIntegerWrapper spProperty = new ReadOnlyIntegerWrapper(
            sp);

    /**
     *
     * @return current sp
     */
    public final int getSP() {
        return sp;
    }

    /**
     *
     * @return current sp property
     */
    public final ReadOnlyIntegerProperty spProperty() {
        return spProperty.getReadOnlyProperty();
    }

    /**
     * Sets current hp. If the value is outside [0..getTotalStat(Stat.MAX_SP)],
     * the value will be clamped.
     *
     * @param value
     */
    public final void setSP(int value) {
        if (value < 0)
            value = 0;
        if (value > getTotalStat(Stat.MAX_SP))
            value = (int) getTotalStat(Stat.MAX_SP);

        sp = value;
        spProperty.set(value);
    }

    /**
     * Restores sp. SP will not go outside getTotalStat(Stat.MAX_SP). No effect
     * if the value is negative.
     *
     * @param value
     */
    protected final void restoreSP(float value) {
        if (value <= 0)
            return;
        setSP(getSP() + (int) value);
    }

    /**
     * Takes away value from sp. SP will not drop below 0. No effect if the
     * value is negative.
     *
     * @param value
     */
    protected final void damageSP(float value) {
        if (value <= 0)
            return;
        setSP(getSP() - (int) value);
    }

    /**
     * Class of this game character
     */
    private GameCharacterClass charClass;

    /**
     *
     * @return game character class
     */
    public final GameCharacterClass getGameCharacterClass() {
        return charClass;
    }

    /**
     * TODO: Change this characters game class to @param cl
     *
     * @param cl
     *            game character class to change to
     */
    // public void changeClass(GameCharacterClass cl) {
    // this.charClass = cl;
    // Skill[] tmpSkills = new Skill[skills.length + charClass.skillIDs.length];
    //
    // int j = 0;
    // for (j = 0; j < skills.length; j++)
    // tmpSkills[j] = skills[j];
    //
    // for (int i = 0; i < charClass.skillIDs.length; i++)
    // tmpSkills[j++] = EntityManager.getSkillByID(charClass.skillIDs[i]);
    //
    // this.skills = tmpSkills;
    // }

    protected Skill[] skills;

    /**
     *
     * @return a skill array for this character
     */
    public Skill[] getSkills() {
        return skills;
    }

    public GameCharacter(int id, String name, String description,
            String textureName, GameCharacterClass charClass) {
        super(id, name, description, textureName);
        this.charClass = charClass;
        init();
    }

    public void init() {
        this.skills = new Skill[charClass.skillIDs.length];

        // for (int i = 0; i < skills.length; i++)
        // skills[i] = EntityManager.getSkillByID(charClass.skillIDs[i]);

        for (Attribute attr : Attribute.values()) {
            attributeProperties.put(attr, new ReadOnlyIntegerWrapper(1));
            bAttributeProperties.put(attr, new ReadOnlyIntegerWrapper(0));
            setAttribute(attr, 1);
            bAttributes.put(attr, 0);
        }

        for (Stat stat : Stat.values()) {
            statProperties.put(stat, new ReadOnlyIntegerWrapper(0));
            bStatProperties.put(stat, new ReadOnlyIntegerWrapper(0));
            stats.put(stat, 0.0f);
            bStats.put(stat, 0.0f);
        }

        updateStats();
        setHP((int) getTotalStat(Stat.MAX_HP)); // set current hp/sp to max
        setSP((int) getTotalStat(Stat.MAX_SP));
    }

    /**
     * Character stats are directly affected by his attributes. Therefore any
     * change in attributes must be followed by call to this method
     */
    public final void updateStats() {
        // calculate totals first
        int strength = getTotalAttribute(Attribute.STRENGTH);
        int vitality = getTotalAttribute(Attribute.VITALITY);
        int dexterity = getTotalAttribute(Attribute.DEXTERITY);
        int agility = getTotalAttribute(Attribute.AGILITY);
        int intellect = getTotalAttribute(Attribute.INTELLECT);
        int wisdom = getTotalAttribute(Attribute.WISDOM);
        int willpower = getTotalAttribute(Attribute.WILLPOWER);
        int perception = getTotalAttribute(Attribute.PERCEPTION);
        int luck = getTotalAttribute(Attribute.LUCK);

        // None of these formulae are finalised yet and need to be checked for
        // game balance
        // only calculate "native" base stats

        float MODIFIER_VERY_LOW = 0.1f, MODIFIER_LOW = 0.2f,
                MODIFIER_MEDIUM = 0.3f, MODIFIER_HIGH = 0.4f,
                MODIFIER_VERY_HIGH = 0.5f, MODIFIER_LEVEL = 0.25f;

        float maxHP = (vitality * MODIFIER_VERY_HIGH
                + strength * MODIFIER_MEDIUM + MODIFIER_LEVEL * baseLevel
                + (vitality / 10)) * charClass.hp;

        float maxSP = (wisdom * MODIFIER_VERY_HIGH + intellect * MODIFIER_MEDIUM
                + willpower * MODIFIER_VERY_LOW + MODIFIER_LEVEL * baseLevel
                + (wisdom / 10)) * charClass.sp;

        float hpRegen = 1 + vitality * MODIFIER_VERY_LOW;
        float spRegen = 2 + wisdom * MODIFIER_VERY_LOW;

        float atk = strength * MODIFIER_VERY_HIGH + dexterity * MODIFIER_MEDIUM
                + perception * MODIFIER_LOW + luck * MODIFIER_VERY_LOW
                + baseLevel + (strength / 10) * ((strength / 10) + 1);

        float matk = intellect * MODIFIER_VERY_HIGH + wisdom * MODIFIER_HIGH
                + willpower * MODIFIER_HIGH + dexterity * MODIFIER_MEDIUM
                + perception * MODIFIER_LOW + luck * MODIFIER_VERY_LOW
                + baseLevel + (intellect / 10) * ((intellect / 10) + 1);

        float def = vitality * MODIFIER_MEDIUM + perception * MODIFIER_LOW
                + strength * MODIFIER_VERY_LOW + MODIFIER_LEVEL * baseLevel
                + (vitality / 20) * (charClass.hp / 10);

        float mdef = willpower * MODIFIER_HIGH + wisdom * MODIFIER_MEDIUM
                + perception * MODIFIER_LOW + intellect * MODIFIER_VERY_LOW
                + MODIFIER_LEVEL * baseLevel
                + (willpower / 20) * (intellect / 10);

        float aspd = agility * MODIFIER_VERY_HIGH + dexterity * MODIFIER_LOW;

        float mspd = dexterity * MODIFIER_MEDIUM + willpower * MODIFIER_VERY_LOW
                + wisdom * MODIFIER_VERY_LOW + intellect * MODIFIER_VERY_LOW
                + perception * MODIFIER_VERY_LOW + luck * MODIFIER_VERY_LOW;

        float critChance = luck * MODIFIER_VERY_HIGH
                + dexterity * MODIFIER_VERY_LOW + perception * MODIFIER_VERY_LOW
                + wisdom * MODIFIER_VERY_LOW;

        float mcritChance = luck * MODIFIER_HIGH + willpower * MODIFIER_LOW
                + perception * MODIFIER_VERY_LOW;

        float critDmg = 2 + luck * 0.01f;
        float mcritDmg = 2 + luck * 0.01f;

        setBaseStat(Stat.MAX_HP, maxHP);
        setBaseStat(Stat.MAX_SP, maxSP);
        setBaseStat(Stat.HP_REGEN, hpRegen);
        setBaseStat(Stat.SP_REGEN, spRegen);
        setBaseStat(Stat.ATK, atk);
        setBaseStat(Stat.MATK, matk);
        setBaseStat(Stat.DEF, def);
        setBaseStat(Stat.MDEF, mdef);
        setBaseStat(Stat.ASPD, aspd);
        setBaseStat(Stat.MSPD, mspd);
        setBaseStat(Stat.CRIT_CHANCE, critChance);
        setBaseStat(Stat.MCRIT_CHANCE, mcritChance);
        setBaseStat(Stat.CRIT_DMG, critDmg);
        setBaseStat(Stat.MCRIT_DMG, mcritDmg);
    }

    /**
     * Regeneration tick. HP/SP.
     */
    private void updateRegen() {
        regenTick += 0.016f;

        if (regenTick >= 2.0f) { // 2 secs
            if (!hasStatusEffect(Status.POISONED)) {
                restoreHP(getTotalStat(Stat.HP_REGEN));
                restoreSP(getTotalStat(Stat.SP_REGEN));
            }
            regenTick = 0.0f;
        }
    }

    private void updateSkills() {
        for (Skill sk : skills) {
            if (sk.active) {
                if (sk.getCurrentCooldown() > 0) {
                    sk.reduceCurrentCooldown(0.016f);
                }
            }
            else { // reapply passive skills
                if (sk.getLevel() > 0)
                    sk.use(this, null);
            }
        }
    }

    private void updateEffects() {
        for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
            Effect e = it.next();
            e.reduceDuration(0.016f);
            if (e.getDuration() <= 0) {
                e.onEnd(this);
                it.remove();
            }
        }
    }

    private void updateStatusEffects() {
        for (Iterator<StatusEffect> it = statuses.iterator(); it.hasNext();) {
            StatusEffect e = it.next();
            e.reduceDuration(0.016f);
            if (e.getDuration() <= 0) {
                it.remove();
            }
        }
    }

    private float regenTick = 0.0f;

    public void update() {
        updateRegen();

        if (!canAttack())
            atkTick++;

        updateSkills();
        // check buffs
        updateEffects();
        updateStatusEffects();

        updateStats();
    }

    public abstract Element getWeaponElement();

    public abstract Element getArmorElement();

    /**
     * Attack tick that decides if character can attack
     */
    private int atkTick = 0;

    /**
     *
     * @return attack tick
     */
    protected final int getAtkTick() {
        return atkTick;
    }

    public final void resetAtkTick() {
        atkTick = 0;
    }

    /**
     * @return if character is ready to perform basic attack based on his ASPD
     */
    public boolean canAttack() {
        return atkTick >= 50 / (1 + getTotalStat(Stat.ASPD) / 100.0f);
    }

    /**
     * Performs basic attack with equipped weapon Damage is physical and element
     * depends on weapon element
     *
     * @param target
     *            target being attacked
     * @return damage dealt
     */
    public Damage attack(GameCharacter target) {
        return dealPhysicalDamage(
                target,
                    getTotalStat(Stat.ATK)
                            + 2f * GameMath.random(getBaseLevel()),
                    getWeaponElement());
    }

    /**
     * Deals physical damage to target. The damage is reduced by armor and
     * defense The damage is affected by attacker's weapon element and by
     * target's armor element
     *
     * @param target
     * @param baseDamage
     * @param element
     * @return
     */
    public Damage dealPhysicalDamage(GameCharacter target, float baseDamage,
            Element element) {
        boolean crit = false;
        if (GameMath.checkChance(getTotalStat(Stat.CRIT_CHANCE))) {
            baseDamage *= getTotalStat(Stat.CRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = element
                .getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(Stat.ARM))
                * baseDamage / 100.0f - target.getTotalStat(Stat.DEF);

        int totalDamage = Math.max(
                Math.round(elementalDamageModifier * damageAfterReduction),
                    0);
        target.damageHP(totalDamage);

        return new Damage(DamageType.PHYSICAL, element, totalDamage,
                crit ? DamageCritical.TRUE : DamageCritical.FALSE);
    }

    /**
     * Deals physical damage of type NEUTRAL to target. The damage is reduced by
     * target's armor and DEF
     *
     * @param target
     * @param baseDamage
     *
     * @return damage dealt
     */
    public Damage dealPhysicalDamage(GameCharacter target, float baseDamage) {
        return dealPhysicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deal magical damage of type param element to target. The damage is
     * reduced by target's magical armor and MDEF
     *
     * @param target
     * @param baseDamage
     *
     * @return damage dealt
     */
    public Damage dealMagicalDamage(GameCharacter target, float baseDamage,
            Element element) {
        boolean crit = false;
        if (GameMath.checkChance(getTotalStat(Stat.MCRIT_CHANCE))) {
            baseDamage *= getTotalStat(Stat.MCRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = element
                .getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - target.getTotalStat(Stat.MARM))
                * baseDamage / 100.0f - target.getTotalStat(Stat.MDEF);

        int totalDamage = Math.max(
                Math.round(elementalDamageModifier * damageAfterReduction),
                    0);
        target.damageHP(totalDamage);

        return new Damage(DamageType.MAGICAL, element, totalDamage,
                crit ? DamageCritical.TRUE : DamageCritical.FALSE);
    }

    /**
     * Deal magical damage of type NEUTRAL to target. The damage is reduced by
     * target's magical armor and MDEF
     *
     * @param target
     * @param baseDamage
     *
     * @return damage dealt
     */
    public Damage dealMagicalDamage(GameCharacter target, float baseDamage) {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deals the exact amount of damage to target as specified by param dmg
     *
     * @param target
     * @param dmg
     */
    public Damage dealPureDamage(GameCharacter target, float dmg) {
        int damage = (int) dmg;
        target.damageHP(damage);
        return new Damage(DamageType.PURE, Element.NEUTRAL, damage,
                DamageCritical.FALSE);
    }

    // public SkillUseResult useSkill(int skillCode, GameCharacter target) {
    // if (skillCode >= skills.length || hasStatusEffect(Status.SILENCED))
    // return SkillUseResult.DEFAULT_FALSE;
    //
    // Skill sk = skills[skillCode];
    // if (sk != null && sk.active && sk.getLevel() > 0 &&
    // sk.getCurrentCooldown() == 0) {
    // if (this.sp >= sk.getManaCost()) {
    // this.sp -= sk.getManaCost();
    // sk.use(this, target);
    // // successful use of skill
    // return sk.getUseResult();
    // }
    // }
    //
    // return SkillUseResult.DEFAULT_FALSE;
    // }
}
