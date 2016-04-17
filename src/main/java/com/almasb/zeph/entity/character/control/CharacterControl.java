package com.almasb.zeph.entity.character.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.ents.component.Required;
import com.almasb.zeph.combat.*;
import com.almasb.zeph.combat.StatusEffect.Status;
import com.almasb.zeph.entity.character.CharacterEntity;
import com.almasb.zeph.entity.character.component.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Required(HPComponent.class)
@Required(SPComponent.class)
public class CharacterControl extends AbstractControl {

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

    protected CharacterClassComponent characterClass;

    protected AttributesComponent attributes;
    protected StatsComponent stats;
    protected HPComponent hp;
    protected SPComponent sp;

    protected CharacterEntity character;

    @Override
    public void onAdded(Entity entity) {
        character = (CharacterEntity) entity;

        hp = entity.getComponentUnsafe(HPComponent.class);
        sp = entity.getComponentUnsafe(SPComponent.class);

        attributes = entity.getComponentUnsafe(AttributesComponent.class);
        stats = entity.getComponentUnsafe(StatsComponent.class);

        init();
    }

    private void init() {
        bindStats();

        hp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_HP));
        hp.restorePercentageMax(100);

        sp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_SP));
        sp.restorePercentageMax(100);
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

    private int level() {
        return character.getBaseLevel().intValue();
    }

    /**
     * Bind base stats to attributes.
     */
    private void bindStats() {
        NumberBinding str = attributes.totalAttributeProperty(Attribute.STRENGTH);
        NumberBinding vit = attributes.totalAttributeProperty(Attribute.VITALITY);
        NumberBinding dex = attributes.totalAttributeProperty(Attribute.DEXTERITY);
        NumberBinding agi = attributes.totalAttributeProperty(Attribute.AGILITY);
        NumberBinding int_ = attributes.totalAttributeProperty(Attribute.INTELLECT);
        NumberBinding wis = attributes.totalAttributeProperty(Attribute.WISDOM);
        NumberBinding wil = attributes.totalAttributeProperty(Attribute.WILLPOWER);
        NumberBinding per = attributes.totalAttributeProperty(Attribute.PERCEPTION);
        NumberBinding luc = attributes.totalAttributeProperty(Attribute.LUCK);

        ReadOnlyIntegerProperty level = character.getBaseLevel();

        stats.statProperty(Stat.MAX_HP).bind(Bindings.createDoubleBinding(() ->
                1 + vit() * 0.5 + str() * 0.3 + level() * 0.25 + vit() / 10,
                vit, str, level
        ));

        stats.statProperty(Stat.MAX_SP).bind(Bindings.createDoubleBinding(() ->
                1 + wis() * 0.4 + wil() * 0.3 + level() * 0.25 + wis() / 10 + int_() * 0.3,
                wis, wil, level, int_
        ));

        stats.statProperty(Stat.HP_REGEN).bind(Bindings.createDoubleBinding(() ->
                1 + vit() * 0.1,
                vit
        ));

        stats.statProperty(Stat.SP_REGEN).bind(Bindings.createDoubleBinding(() ->
                2 + wis() * 0.1,
                wis
        ));

        stats.statProperty(Stat.ATK).bind(Bindings.createDoubleBinding(() ->
                str() * 0.5 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 + level() + str() / 10 * ((str() / 10) + 1),
                str, dex, per, luc, level
        ));

        stats.statProperty(Stat.MATK).bind(Bindings.createDoubleBinding(() ->
                int_() * 0.5 + wis() * 0.4 + wil() * 0.4 + dex() * 0.3 + per() * 0.2 + luc() * 0.1,
                int_, dex, per, luc
        ));

        stats.statProperty(Stat.DEF).bind(Bindings.createDoubleBinding(() ->
                vit() * 0.5 + per() * 0.2 + str() * 0.1 + level() * 0.25 + vit() / 20,
                vit, per, str, level
        ));

        stats.statProperty(Stat.MDEF).bind(Bindings.createDoubleBinding(() ->
                wil() * 0.5 + wis() * 0.3 + per() * 0.2 + int_() * 0.1 + level() * 0.25 + wil() / 20 * int_() / 10,
                wil, wis, per, int_, level
        ));

        stats.statProperty(Stat.ASPD).bind(Bindings.createDoubleBinding(() ->
                agi() * 0.5 + dex() * 0.2,
                agi, dex
        ));

        stats.statProperty(Stat.MSPD).bind(Bindings.createDoubleBinding(() ->
                dex() * 0.3 + wil() * 0.1 + wis() * 0.1 + int_() * 0.1 + per() * 0.1 + luc() * 0.1,
                dex, wil, wis, int_, per, luc
        ));

        stats.statProperty(Stat.CRIT_CHANCE).bind(Bindings.createDoubleBinding(() ->
                luc() * 0.5 + per() * 0.1 + wis() * 0.1,
                luc, per, wis
        ));

        stats.statProperty(Stat.MCRIT_CHANCE).bind(Bindings.createDoubleBinding(() ->
                luc() * 0.5 + wil() * 0.2 + per() * 0.1,
                luc, wil, per
        ));

        stats.statProperty(Stat.CRIT_DMG).bind(Bindings.createDoubleBinding(() ->
                2 + luc() * 0.01,
                luc
        ));

        stats.statProperty(Stat.MCRIT_DMG).bind(Bindings.createDoubleBinding(() ->
                2 + luc() * 0.01,
                luc
        ));
    }

    private double regenTick = 0.0f;

    /**
     * Regeneration tick. HP/SP.
     */
    private void updateRegen(double tpf) {
        regenTick += tpf;

        if (regenTick >= 2.0f) { // 2 secs
            if (!hasStatus(Status.POISONED)) {
                hp.restore(stats.getTotalStat(Stat.HP_REGEN));
                sp.restore(stats.getTotalStat(Stat.SP_REGEN));
            }
            regenTick = 0.0f;
        }
    }

    private void updateSkills() {
//        for (Skill sk : skills) {
//            if (sk.active) {
//                if (sk.getCurrentCooldown() > 0) {
//                    sk.reduceCurrentCooldown(0.016f);
//                }
//            }
//            else { // reapply passive skills
//                if (sk.getLevel() > 0)
//                    sk.use(this, null);
//            }
//        }
    }

    private void updateEffects(double tpf) {
        for (Iterator<Effect> it = effects.iterator(); it.hasNext();) {
            Effect e = it.next();
            e.reduceDuration(0.016f);
            if (e.getDuration() <= 0) {
                e.onEnd(this);
                it.remove();
            }
        }
    }

    private void updateStatusEffects(double tpf) {
        for (Iterator<StatusEffect> it = statuses.iterator(); it.hasNext();) {
            StatusEffect e = it.next();
            e.reduceDuration(0.016f);
            if (e.getDuration() <= 0) {
                it.remove();
            }
        }
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
        updateRegen(tpf);

        if (!canAttack())
            atkTick++;

        updateSkills();
        // check buffs
        updateEffects(tpf);
        updateStatusEffects(tpf);
    }

//    public abstract Element getWeaponElement();
//
//    public abstract Element getArmorElement();

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
    public Damage attack(Entity target) {
        //CharacterControl other = target.getControlUnsafe(CharacterControl.class);

        CharacterEntity other = (CharacterEntity) target;

        return dealPhysicalDamage(other, other.getStats().getTotalStat(Stat.ATK) + 2f * GameMath.random(other.getBaseLevel().get()), Element.NEUTRAL);
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
    public Damage dealPhysicalDamage(Entity target, float baseDamage, Element element) {
        CharacterEntity otherEntity = (CharacterEntity) target;
        CharacterControl other = target.getControlUnsafe(CharacterControl.class);

        boolean crit = false;

        if (GameMath.checkChance(stats.getTotalStat(Stat.CRIT_CHANCE))) {
            baseDamage *= stats.getTotalStat(Stat.CRIT_DMG);
            crit = true;
        }

        float elementalDamageModifier = 1.0f;
                //element.getDamageModifierAgainst(target.getArmorElement());
        float damageAfterReduction = (100 - other.stats.getTotalStat(Stat.ARM)) * baseDamage / 100.0f - other.stats.getTotalStat(Stat.DEF);

        int totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0);
        otherEntity.getHp().damage(totalDamage);

        return new Damage(Damage.DamageType.PHYSICAL, element, totalDamage,
                crit ? Damage.DamageCritical.TRUE : Damage.DamageCritical.FALSE);
    }
//
//    /**
//     * Deals physical damage of type NEUTRAL to target. The damage is reduced by
//     * target's armor and DEF
//     *
//     * @param target
//     * @param baseDamage
//     *
//     * @return damage dealt
//     */
//    public Damage dealPhysicalDamage(CharacterControl target, float baseDamage) {
//        return dealPhysicalDamage(target, baseDamage, Element.NEUTRAL);
//    }
//
//    /**
//     * Deal magical damage of type param element to target. The damage is
//     * reduced by target's magical armor and MDEF
//     *
//     * @param target
//     * @param baseDamage
//     *
//     * @return damage dealt
//     */
//    public Damage dealMagicalDamage(CharacterControl target, float baseDamage,
//                                    Element element) {
//        boolean crit = false;
//        if (GameMath.checkChance(getTotalStat(Stat.MCRIT_CHANCE))) {
//            baseDamage *= getTotalStat(Stat.MCRIT_DMG);
//            crit = true;
//        }
//
//        float elementalDamageModifier = element
//                .getDamageModifierAgainst(target.getArmorElement());
//        float damageAfterReduction = (100 - target.getTotalStat(Stat.MARM))
//                * baseDamage / 100.0f - target.getTotalStat(Stat.MDEF);
//
//        int totalDamage = Math.max(
//                Math.round(elementalDamageModifier * damageAfterReduction),
//                    0);
//        target.damageHP(totalDamage);
//
//        return new Damage(DamageType.MAGICAL, element, totalDamage,
//                crit ? DamageCritical.TRUE : DamageCritical.FALSE);
//    }
//
//    /**
//     * Deal magical damage of type NEUTRAL to target. The damage is reduced by
//     * target's magical armor and MDEF
//     *
//     * @param target
//     * @param baseDamage
//     *
//     * @return damage dealt
//     */
//    public Damage dealMagicalDamage(CharacterControl target, float baseDamage) {
//        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL);
//    }
//
//    /**
//     * Deals the exact amount of damage to target as specified by param dmg
//     *
//     * @param target
//     * @param dmg
//     */
//    public Damage dealPureDamage(CharacterControl target, float dmg) {
//        int damage = (int) dmg;
//        target.damageHP(damage);
//        return new Damage(DamageType.PURE, Element.NEUTRAL, damage,
//                DamageCritical.FALSE);
//    }

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
