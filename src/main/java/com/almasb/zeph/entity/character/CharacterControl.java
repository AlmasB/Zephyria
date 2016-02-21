package com.almasb.zeph.entity.character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.ents.component.Required;
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

import com.almasb.zeph.entity.component.HPComponent;
import com.almasb.zeph.entity.component.SPComponent;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 * Essentially alive game object Enemies/NPCs/players.
 *
 * @author Almas Baimagambetov
 */
@Required(HPComponent.class)
public abstract class CharacterControl extends AbstractControl {

    /**
     * Statuses currently affecting this character.
     */
    private List<StatusEffect> statuses = new ArrayList<>();

    /**
     * @param status status
     * @return true if character is under status, false otherwise
     */
    public boolean hasStatus(Status status) {
        return statuses.stream().anyMatch(s -> s.getStatus() == status);
    }

    /**
     * Apply status effect.
     *
     * @param e effect
     */
    public void addStatusEffect(StatusEffect e) {
        statuses.add(e);
    }

    /**
     * Effects currently placed on this character.
     */
    private List<Effect> effects = new ArrayList<>();

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).
     *
     * @param e effect
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

//    /**
//     * TODO: Change this characters game class to @param cl
//     *
//     * @param cl
//     *            game character class to change to
//     */
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

    private AttributesComponent attributes;
    private StatsComponent stats;
    private HPComponent hp;
    private SPComponent sp;

    @Override
    public void onAdded(Entity entity) {
        hp = entity.getComponentUnsafe(HPComponent.class);
        sp = entity.getComponentUnsafe(SPComponent.class);

        attributes = entity.getComponentUnsafe(AttributesComponent.class);
        stats = entity.getComponentUnsafe(StatsComponent.class);

        init();
    }

    private void init() {
        //this.skills = new Skill[charClass.skillIDs.length];

        // for (int i = 0; i < skills.length; i++)
        // skills[i] = EntityManager.getSkillByID(charClass.skillIDs[i]);

//        for (Attribute attr : Attribute.values()) {
//            attributeProperties.put(attr, new ReadOnlyIntegerWrapper(1));
//            bAttributeProperties.put(attr, new ReadOnlyIntegerWrapper(0));
//            setAttribute(attr, 1);
//            bAttributes.put(attr, 0);
//        }
//
//        for (Stat stat : Stat.values()) {
//            statProperties.put(stat, new ReadOnlyIntegerWrapper(0));
//            bStatProperties.put(stat, new ReadOnlyIntegerWrapper(0));
//            stats.put(stat, 0.0f);
//            bStats.put(stat, 0.0f);
//        }

        updateStats();

        hp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_HP));
        hp.restorePercentageMax(100);

        sp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_SP));
        sp.restorePercentageMax(100);

//        setHP((int) getTotalStat(Stat.MAX_HP)); // set current hp/sp to max
//        setSP((int) getTotalStat(Stat.MAX_SP));
    }

    private int str() {
        return attributes.getTotalAttribute(Attribute.STRENGTH);
    }

    private int vit() {
        return attributes.getTotalAttribute(Attribute.VITALITY);
    }

    private int dex() {
        return attributes.getTotalAttribute(Attribute.DEXTERITY);
    }

    private int agi() {
        return attributes.getTotalAttribute(Attribute.AGILITY);
    }

    private int int_() {
        return attributes.getTotalAttribute(Attribute.INTELLECT);
    }

    private int wis() {
        return attributes.getTotalAttribute(Attribute.WISDOM);
    }

    private int wil() {
        return attributes.getTotalAttribute(Attribute.WILLPOWER);
    }

    private int per() {
        return attributes.getTotalAttribute(Attribute.PERCEPTION);
    }

    private int luc() {
        return attributes.getTotalAttribute(Attribute.LUCK);
    }

    /**
     * Character stats are directly affected by his attributes. Therefore any
     * change in attributes must be followed by call to this method
     */
    public final void updateStats() {

        // bind base stats to attributes

        NumberBinding str = attributes.totalAttributeProperty(Attribute.STRENGTH);
        NumberBinding vit = attributes.totalAttributeProperty(Attribute.VITALITY);
        NumberBinding dex = attributes.totalAttributeProperty(Attribute.DEXTERITY);
        NumberBinding agi = attributes.totalAttributeProperty(Attribute.AGILITY);
        NumberBinding int_ = attributes.totalAttributeProperty(Attribute.INTELLECT);
        NumberBinding wis = attributes.totalAttributeProperty(Attribute.WISDOM);
        NumberBinding wil = attributes.totalAttributeProperty(Attribute.WILLPOWER);
        NumberBinding per = attributes.totalAttributeProperty(Attribute.PERCEPTION);
        NumberBinding luc = attributes.totalAttributeProperty(Attribute.LUCK);

        stats.statProperty(Stat.ATK).bind(Bindings.createDoubleBinding(() ->
                str() * 0.5 + dex() * 0.3 + per() * 0.2 + luc() * 0.1,
                str, dex, per, luc));





//        // calculate totals first
//        int strength = getTotalAttribute(Attribute.STRENGTH);
//        int vitality = getTotalAttribute(Attribute.VITALITY);
//        int dexterity = getTotalAttribute(Attribute.DEXTERITY);
//        int agility = getTotalAttribute(Attribute.AGILITY);
//        int intellect = getTotalAttribute(Attribute.INTELLECT);
//        int wisdom = getTotalAttribute(Attribute.WISDOM);
//        int willpower = getTotalAttribute(Attribute.WILLPOWER);
//        int perception = getTotalAttribute(Attribute.PERCEPTION);
//        int luck = getTotalAttribute(Attribute.LUCK);
//
//        // None of these formulae are finalised yet and need to be checked for
//        // game balance
//        // only calculate "native" base stats
//
//        float MODIFIER_VERY_LOW = 0.1f, MODIFIER_LOW = 0.2f,
//                MODIFIER_MEDIUM = 0.3f, MODIFIER_HIGH = 0.4f,
//                MODIFIER_VERY_HIGH = 0.5f, MODIFIER_LEVEL = 0.25f;
//
//        float maxHP = (vitality * MODIFIER_VERY_HIGH
//                + strength * MODIFIER_MEDIUM + MODIFIER_LEVEL * baseLevel
//                + (vitality / 10)) * charClass.hp;
//
//        float maxSP = (wisdom * MODIFIER_VERY_HIGH + intellect * MODIFIER_MEDIUM
//                + willpower * MODIFIER_VERY_LOW + MODIFIER_LEVEL * baseLevel
//                + (wisdom / 10)) * charClass.sp;
//
//        float hpRegen = 1 + vitality * MODIFIER_VERY_LOW;
//        float spRegen = 2 + wisdom * MODIFIER_VERY_LOW;
//
//        float atk = strength * MODIFIER_VERY_HIGH + dexterity * MODIFIER_MEDIUM
//                + perception * MODIFIER_LOW + luck * MODIFIER_VERY_LOW
//                + baseLevel + (strength / 10) * ((strength / 10) + 1);
//
//        float matk = intellect * MODIFIER_VERY_HIGH + wisdom * MODIFIER_HIGH
//                + willpower * MODIFIER_HIGH + dexterity * MODIFIER_MEDIUM
//                + perception * MODIFIER_LOW + luck * MODIFIER_VERY_LOW
//                + baseLevel + (intellect / 10) * ((intellect / 10) + 1);
//
//        float def = vitality * MODIFIER_MEDIUM + perception * MODIFIER_LOW
//                + strength * MODIFIER_VERY_LOW + MODIFIER_LEVEL * baseLevel
//                + (vitality / 20) * (charClass.hp / 10);
//
//        float mdef = willpower * MODIFIER_HIGH + wisdom * MODIFIER_MEDIUM
//                + perception * MODIFIER_LOW + intellect * MODIFIER_VERY_LOW
//                + MODIFIER_LEVEL * baseLevel
//                + (willpower / 20) * (intellect / 10);
//
//        float aspd = agility * MODIFIER_VERY_HIGH + dexterity * MODIFIER_LOW;
//
//        float mspd = dexterity * MODIFIER_MEDIUM + willpower * MODIFIER_VERY_LOW
//                + wisdom * MODIFIER_VERY_LOW + intellect * MODIFIER_VERY_LOW
//                + perception * MODIFIER_VERY_LOW + luck * MODIFIER_VERY_LOW;
//
//        float critChance = luck * MODIFIER_VERY_HIGH
//                + dexterity * MODIFIER_VERY_LOW + perception * MODIFIER_VERY_LOW
//                + wisdom * MODIFIER_VERY_LOW;
//
//        float mcritChance = luck * MODIFIER_HIGH + willpower * MODIFIER_LOW
//                + perception * MODIFIER_VERY_LOW;
//
//        float critDmg = 2 + luck * 0.01f;
//        float mcritDmg = 2 + luck * 0.01f;
//
//        setBaseStat(Stat.MAX_HP, maxHP);
//        setBaseStat(Stat.MAX_SP, maxSP);
//        setBaseStat(Stat.HP_REGEN, hpRegen);
//        setBaseStat(Stat.SP_REGEN, spRegen);
//        setBaseStat(Stat.ATK, atk);
//        setBaseStat(Stat.MATK, matk);
//        setBaseStat(Stat.DEF, def);
//        setBaseStat(Stat.MDEF, mdef);
//        setBaseStat(Stat.ASPD, aspd);
//        setBaseStat(Stat.MSPD, mspd);
//        setBaseStat(Stat.CRIT_CHANCE, critChance);
//        setBaseStat(Stat.MCRIT_CHANCE, mcritChance);
//        setBaseStat(Stat.CRIT_DMG, critDmg);
//        setBaseStat(Stat.MCRIT_DMG, mcritDmg);
    }

    private double regenTick = 0.0f;

    /**
     * Regeneration tick. HP/SP.
     */
    private void updateRegen() {
        regenTick += 0.016f;

        if (regenTick >= 2.0f) { // 2 secs
            if (!hasStatus(Status.POISONED)) {
                hp.restore(stats.getTotalStat(Stat.HP_REGEN));
                restoreSP(stats.getTotalStat(Stat.SP_REGEN));
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

    @Override
    public void onUpdate(Entity entity, double tpf) {
        update();

//        if (getHP() <= 0)
//            entity.fireFXGLEvent(new FXGLEvent(Event.DEATH));
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
        return atkTick >= 50 / (1 + stats.getTotalStat(Stat.ASPD) / 100.0f);
    }

    /**
     * Performs basic attack with equipped weapon Damage is physical and element
     * depends on weapon element
     *
     * @param target
     *            target being attacked
     * @return damage dealt
     */
    public Damage attack(CharacterControl target) {
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
    public Damage dealPhysicalDamage(CharacterControl target, float baseDamage, Element element) {
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
    public Damage dealPhysicalDamage(CharacterControl target, float baseDamage) {
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
    public Damage dealMagicalDamage(CharacterControl target, float baseDamage,
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
    public Damage dealMagicalDamage(CharacterControl target, float baseDamage) {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL);
    }

    /**
     * Deals the exact amount of damage to target as specified by param dmg
     *
     * @param target
     * @param dmg
     */
    public Damage dealPureDamage(CharacterControl target, float dmg) {
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
