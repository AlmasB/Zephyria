package com.almasb.zeph.entity.character.control

import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.ents.component.Required
import com.almasb.zeph.combat.*
import com.almasb.zeph.entity.character.CharacterEntity
import com.almasb.zeph.entity.character.component.*
import javafx.beans.binding.Bindings
import java.util.*
import java.util.concurrent.Callable

open class CharacterControl : AbstractControl() {

    /**
     * Statuses currently affecting this character.
     */
    private val statuses = ArrayList<StatusEffect>()

    /**
     * @param status status
     *
     * @return true if character is under status, false otherwise
     */
    fun hasStatus(status: StatusEffect.Status) = statuses.any { it.status == status }

    /**
     * Apply status effect.

     * @param e effect
     */
    fun addStatusEffect(e: StatusEffect) {
        statuses.add(e)
    }

    /**
     * Effects currently placed on this character.
     */
    private val effects = ArrayList<Effect>()

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).

     * @param e effect
     */
    fun addEffect(e: Effect) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val eff = it.next()
            if (eff.sourceID == e.sourceID) {
                eff.onEnd(this)
                it.remove()
                break
            }
        }

        e.onBegin(this)
        effects.add(e)
    }

    //protected var characterClass: CharacterClassComponent

    protected lateinit var attributes: AttributesComponent
    protected lateinit var stats: StatsComponent
    protected lateinit var hp: HPComponent
    protected lateinit var sp: SPComponent

    protected lateinit var character: CharacterEntity

    override fun onAdded(entity: Entity) {
        character = entity as CharacterEntity

        attributes = character.attributes
        stats = character.stats
        hp = character.hp
        sp = character.sp

        init()
    }

    private fun init() {
        bindStats()

        hp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_HP))
        hp.restorePercentageMax(100.0)

        sp.maxValueProperty().bind(stats.totalStatProperty(Stat.MAX_SP))
        sp.restorePercentageMax(100.0)
    }

    private fun str(): Int {
        return attributes.getTotalAttribute(Attribute.STRENGTH)
    }

    private fun vit(): Int {
        return attributes.getTotalAttribute(Attribute.VITALITY)
    }

    private fun dex(): Int {
        return attributes.getTotalAttribute(Attribute.DEXTERITY)
    }

    private fun agi(): Int {
        return attributes.getTotalAttribute(Attribute.AGILITY)
    }

    private fun int_(): Int {
        return attributes.getTotalAttribute(Attribute.INTELLECT)
    }

    private fun wis(): Int {
        return attributes.getTotalAttribute(Attribute.WISDOM)
    }

    private fun wil(): Int {
        return attributes.getTotalAttribute(Attribute.WILLPOWER)
    }

    private fun per(): Int {
        return attributes.getTotalAttribute(Attribute.PERCEPTION)
    }

    private fun luc(): Int {
        return attributes.getTotalAttribute(Attribute.LUCK)
    }

    private fun level(): Int {
        return character.baseLevel.intValue()
    }

    /**
     * Bind base stats to attributes.
     */
    private fun bindStats() {
        val str = attributes.totalAttributeProperty(Attribute.STRENGTH)
        val vit = attributes.totalAttributeProperty(Attribute.VITALITY)
        val dex = attributes.totalAttributeProperty(Attribute.DEXTERITY)
        val agi = attributes.totalAttributeProperty(Attribute.AGILITY)
        val int_ = attributes.totalAttributeProperty(Attribute.INTELLECT)
        val wis = attributes.totalAttributeProperty(Attribute.WISDOM)
        val wil = attributes.totalAttributeProperty(Attribute.WILLPOWER)
        val per = attributes.totalAttributeProperty(Attribute.PERCEPTION)
        val luc = attributes.totalAttributeProperty(Attribute.LUCK)

        val level = character.baseLevel

        stats.statProperty(Stat.MAX_HP).bind(Bindings.createDoubleBinding(Callable {
            1.0 + vit() * 0.5 + str() * 0.3 + level() * 0.25 + (vit() / 10)
        }, vit, str, level))

        stats.statProperty(Stat.MAX_SP).bind(Bindings.createDoubleBinding(Callable{ 1.0 + wis() * 0.4 + wil() * 0.3 + level() * 0.25 + (wis() / 10).toDouble() + int_() * 0.3 },
                wis, wil, level, int_))

        stats.statProperty(Stat.HP_REGEN).bind(Bindings.createDoubleBinding(Callable{ 1 + vit() * 0.1 },
                vit))

        stats.statProperty(Stat.SP_REGEN).bind(Bindings.createDoubleBinding(Callable{ 2 + wis() * 0.1 },
                wis))

        stats.statProperty(Stat.ATK).bind(Bindings.createDoubleBinding(Callable{ str() * 0.5 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 + level().toDouble() + (str() / 10 * (str() / 10 + 1)).toDouble() },
                str, dex, per, luc, level))

        stats.statProperty(Stat.MATK).bind(Bindings.createDoubleBinding(Callable{ int_() * 0.5 + wis() * 0.4 + wil() * 0.4 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 },
                int_, dex, per, luc))

        stats.statProperty(Stat.DEF).bind(Bindings.createDoubleBinding(Callable{ vit() * 0.5 + per() * 0.2 + str() * 0.1 + level() * 0.25 + (vit() / 20).toDouble() },
                vit, per, str, level))

        stats.statProperty(Stat.MDEF).bind(Bindings.createDoubleBinding(Callable{ wil() * 0.5 + wis() * 0.3 + per() * 0.2 + int_() * 0.1 + level() * 0.25 + (wil() / 20 * int_() / 10).toDouble() },
                wil, wis, per, int_, level))

        stats.statProperty(Stat.ASPD).bind(Bindings.createDoubleBinding(Callable{ agi() * 0.5 + dex() * 0.2 },
                agi, dex))

        stats.statProperty(Stat.MSPD).bind(Bindings.createDoubleBinding(Callable{ dex() * 0.3 + wil() * 0.1 + wis() * 0.1 + int_() * 0.1 + per() * 0.1 + luc() * 0.1 },
                dex, wil, wis, int_, per, luc))

        stats.statProperty(Stat.CRIT_CHANCE).bind(Bindings.createDoubleBinding(Callable{ luc() * 0.5 + per() * 0.1 + wis() * 0.1 },
                luc, per, wis))

        stats.statProperty(Stat.MCRIT_CHANCE).bind(Bindings.createDoubleBinding(Callable{ luc() * 0.5 + wil() * 0.2 + per() * 0.1 },
                luc, wil, per))

        stats.statProperty(Stat.CRIT_DMG).bind(Bindings.createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))

        stats.statProperty(Stat.MCRIT_DMG).bind(Bindings.createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))
    }

    private var regenTick = 0.0

    /**
     * Regeneration tick. HP/SP.
     */
    private fun updateRegen(tpf: Double) {
        regenTick += tpf

        if (regenTick >= 2.0f) {
            // 2 secs
            if (!hasStatus(StatusEffect.Status.POISONED)) {
                hp.restore(stats.getTotalStat(Stat.HP_REGEN).toDouble())
                sp.restore(stats.getTotalStat(Stat.SP_REGEN).toDouble())
            }
            regenTick = 0.0
        }
    }

    private fun updateSkills() {
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

    private fun updateEffects(tpf: Double) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.reduceDuration(0.016f)
            if (e.duration <= 0) {
                e.onEnd(this)
                it.remove()
            }
        }
    }

    private fun updateStatusEffects(tpf: Double) {
        val it = statuses.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.reduceDuration(0.016f)
            if (e.duration <= 0) {
                it.remove()
            }
        }
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        updateRegen(tpf)

        if (!canAttack())
            atkTick++

        updateSkills()
        // check buffs
        updateEffects(tpf)
        updateStatusEffects(tpf)
    }

    //    public abstract Element getWeaponElement();
    //
    //    public abstract Element getArmorElement();

    /**
     * Attack tick that decides if character can attack
     */
    /**

     * @return attack tick
     */
    protected var atkTick = 0
        private set

    fun resetAtkTick() {
        atkTick = 0
    }

    /**
     * @return if character is ready to perform basic attack based on his ASPD
     */
    fun canAttack(): Boolean {
        return atkTick >= 50 / (1 + stats.getTotalStat(Stat.ASPD) / 100.0f)
    }

    /**
     * Performs basic attack with equipped weapon Damage is physical and element
     * depends on weapon element

     * @param target
     * *            target being attacked
     * *
     * @return damage dealt
     */
    fun attack(target: Entity): Damage {
        //CharacterControl other = target.getControlUnsafe(CharacterControl.class);

        val other = target as CharacterEntity

        return dealPhysicalDamage(other, stats.getTotalStat(Stat.ATK) + 2 * GameMath.random(level()), Element.NEUTRAL)
    }

    /**
     * Deals physical damage to target. The damage is reduced by armor and
     * defense The damage is affected by attacker's weapon element and by
     * target's armor element

     * @param target
     * *
     * @param baseDamage
     * *
     * @param element
     * *
     * @return
     */
    fun dealPhysicalDamage(target: Entity, baseDamage: Float, element: Element): Damage {
        var baseDamage = baseDamage
        val otherEntity = target as CharacterEntity
        val other = target.getControlUnsafe(CharacterControl::class.java)

        var crit = false

        if (GameMath.checkChance(stats.getTotalStat(Stat.CRIT_CHANCE))) {
            baseDamage *= stats.getTotalStat(Stat.CRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = 1.0f
        //element.getDamageModifierAgainst(target.getArmorElement());
        val damageAfterReduction = (100 - other.stats.getTotalStat(Stat.ARM)) * baseDamage / 100.0f - other.stats.getTotalStat(Stat.DEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0)
        otherEntity.hp.damage(totalDamage.toDouble())

        return Damage(Damage.DamageType.PHYSICAL, element, totalDamage,
                if (crit) Damage.DamageCritical.TRUE else Damage.DamageCritical.FALSE)
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

    /**
     * Deals the exact amount of damage to target as specified by param dmg
     *
     * @param target
     * @param dmg
     */
    fun dealPureDamage(target: Entity, value: Double): Damage {
        val amount = value.toInt()
        (target as CharacterEntity).hp.damage(amount.toDouble())

        return Damage(Damage.DamageType.PURE, Element.NEUTRAL, amount, Damage.DamageCritical.FALSE)
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
