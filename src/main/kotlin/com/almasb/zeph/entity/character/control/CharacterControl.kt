package com.almasb.zeph.entity.character.control

import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.ents.component.Required
import com.almasb.zeph.combat.*
import com.almasb.zeph.entity.character.CharacterEntity
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.skill.SkillEntity
import com.almasb.zeph.entity.skill.SkillType
import com.almasb.zeph.entity.skill.SkillUseResult
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.geometry.Point2D
import java.util.*
import java.util.concurrent.Callable

open class CharacterControl : AbstractControl() {

    /**
     * Statuses currently affecting this character.
     */
    val statuses = FXCollections.observableArrayList<StatusEffect>()

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
    val effects = FXCollections.observableArrayList<EffectEntity>()

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).

     * @param e effect
     */
    fun addEffect(e: EffectEntity) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val eff = it.next()
            if (eff.getID() == e.getID()) {
                eff.onEnd(char)
                it.remove()
                break
            }
        }

        e.onBegin(char)
        effects.add(e)
    }

    protected lateinit var attributes: AttributesComponent
    protected lateinit var stats: StatsComponent
    protected lateinit var hp: HPComponent
    protected lateinit var sp: SPComponent

    protected lateinit var char: CharacterEntity

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity

        attributes = char.attributes
        stats = char.stats
        hp = char.hp
        sp = char.sp

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
        return char.baseLevel.intValue()
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

        val level = char.baseLevel

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
     * TODO: when max HP is reduced, changes are delayed atm
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

    private fun updateSkills(tpf: Double) {
        char.skills.forEach {
            it.onUpdate(tpf)
            if (it.data.type == SkillType.PASSIVE) {
                //it.data.onCast(char, char, it.level.value)
            }
        }
    }

    private fun updateEffects(tpf: Double) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.duration.value -= tpf
            if (e.duration.value <= 0) {
                e.onEnd(char)
                it.remove()
            }
        }
    }

    private fun updateStatusEffects(tpf: Double) {
        val it = statuses.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.reduceDuration(tpf)
            if (e.duration <= 0) {
                it.remove()
            }
        }
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        if (hp.isZero)
            return

        updateRegen(tpf)

        if (!canAttack())
            atkTick += tpf

        updateSkills(tpf)
        // check buffs
        updateEffects(tpf)
        updateStatusEffects(tpf)
    }

    /**
     * Attack tick that decides if character can attack.
     */
    protected var atkTick = 0.0
        private set

    fun resetAtkTick() {
        atkTick = 0.0
    }

    /**
     * @return if character is ready to perform basic attack based on his ASPD
     */
    open fun canAttack(): Boolean {
        return atkTick >= 3.0 - stats.getTotalStat(Stat.ASPD) / 100.0
    }

    /**
     * Performs basic attack with equipped weapon on the [target].
     * Damage is physical and element depends on weapon element.
     *
     * @return damage dealt
     */
    fun attack(target: CharacterEntity): Damage {
        return dealPhysicalDamage(target, stats.getTotalStat(Stat.ATK).toDouble() + 2 * GameMath.random(level()), char.weaponElement.value)
    }

    /**
     * Deals physical damage with [baseDamage] amount to [target]. The damage is reduced by armor and defense.
     * The damage will be of [element] element.
     *
     * @return damage dealt
     */
    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): Damage {
        var baseDamage = baseDamage

        var crit = false

        if (GameMath.checkChance(stats.getTotalStat(Stat.CRIT_CHANCE))) {
            baseDamage *= stats.getTotalStat(Stat.CRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = element.getDamageModifierAgainst(target.armorElement.value);

        val damageAfterReduction = (100 - target.stats.getTotalStat(Stat.ARM)) * baseDamage / 100.0 - target.stats.getTotalStat(Stat.DEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
        target.hp.damage(totalDamage.toDouble())

        return Damage(Damage.DamageType.PHYSICAL, element, totalDamage,
                if (crit) Damage.DamageCritical.TRUE else Damage.DamageCritical.FALSE)
    }

    /**
     * Deals physical damage of type NEUTRAL to target. The damage is reduced by
     * target's armor and DEF.
     *
     * @param target
     * @param baseDamage
     *
     * @return damage dealt
     */
    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double): Damage {
        return dealPhysicalDamage(target, baseDamage, Element.NEUTRAL)
    }

    /**
     * Deal magical [baseDamage] of type [element] to [target]. The damage is
     * reduced by target's magical armor and MDEF.
     *
     * @return damage dealt
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): Damage {
        var baseDamage = baseDamage

        var crit = false

        if (GameMath.checkChance(stats.getTotalStat(Stat.MCRIT_CHANCE))) {
            baseDamage *= stats.getTotalStat(Stat.MCRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = element.getDamageModifierAgainst(target.armorElement.value);

        val damageAfterReduction = (100 - target.stats.getTotalStat(Stat.MARM)) * baseDamage / 100.0 - target.stats.getTotalStat(Stat.MDEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
        target.hp.damage(totalDamage.toDouble())

        return Damage(Damage.DamageType.MAGICAL, element, totalDamage,
                if (crit) Damage.DamageCritical.TRUE else Damage.DamageCritical.FALSE)
    }

    /**
     * Deal magical [baseDamage] of type NEUTRAL to [target].
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double): Damage {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL)
    }

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

    fun useSelfSkill(index: Int): SkillUseResult {
        val skill = char.skills[index]

        if (skill.level.value == 0)
            return SkillUseResult.NONE

        if (skill.currentCooldown.value > 0)
            return SkillUseResult.NONE

        if (skill.data.mana > sp.value)
            return SkillUseResult.NONE

        sp.value -= skill.data.mana
        skill.putOnCooldown()

        skill.data.onCast(char, char, skill)

        return SkillUseResult.NONE
    }

    fun useTargetSkill(index: Int, target: CharacterEntity): SkillUseResult {
        // TODO: complete
        return char.skills[index].data.onCast(char, target, char.skills[index])
    }

    fun useTargetSkill(skill: SkillEntity, target: CharacterEntity): SkillUseResult {
        if (skill.level.value == 0)
            return SkillUseResult.NONE

        if (skill.currentCooldown.value > 0)
            return SkillUseResult.NONE

        if (skill.data.mana > sp.value)
            return SkillUseResult.NONE

        sp.value -= skill.data.mana
        skill.putOnCooldown()

        return skill.data.onCast(char, target, skill)
    }

    fun useAreaSkill(index: Int, target: Point2D): SkillUseResult {
        // TODO: complete
        return char.skills[index].data.onCast(char, char, char.skills[index])
    }
}
