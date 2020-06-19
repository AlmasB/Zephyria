package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.components.HealthDoubleComponent
import com.almasb.fxgl.dsl.components.ManaDoubleComponent
import com.almasb.fxgl.dsl.fire
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.ViewComponent
import com.almasb.zeph.Config
import com.almasb.zeph.Inventory
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.*
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.entity.character.component.Attributes
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
import com.almasb.zeph.entity.character.component.Stats
import com.almasb.zeph.events.OnAttackEvent
import com.almasb.zeph.item.UsableItem
import com.almasb.zeph.skill.SkillComponent
import com.almasb.zeph.skill.SkillUseResult
import javafx.beans.binding.Bindings.createDoubleBinding
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Point2D
import java.util.concurrent.Callable

open class CharacterComponent(data: CharacterData) : Component() {

    private lateinit var char: CharacterEntity

    val charClass = SimpleObjectProperty<CharacterClass>(CharacterClass.MONSTER)

    val baseLevel = SimpleIntegerProperty(1)
    val hp = HealthDoubleComponent(1.0)
    val sp = ManaDoubleComponent(1.0)

    val attributes = Attributes()
    val stats = Stats()

    /**
     * Statuses currently affecting this character.
     */
    val statuses = FXCollections.observableArrayList<StatusEffect>()

    /**
     * Effects currently placed on this character.
     */
    val effects = FXCollections.observableArrayList<Effect>()

    val weaponElement = SimpleObjectProperty<Element>()
    val armorElement = SimpleObjectProperty<Element>()

    val inventory = Inventory()
    val skills = FXCollections.observableArrayList<SkillComponent>()

    // what XP does this char give on death
    val baseXP = SimpleIntegerProperty()
    val statXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    val attackRange: Int = data.attackRange

    init {
        charClass.value = data.charClass

        //data.attributes.forEach { attribute, value ->  attributes.setAttribute(attribute, value)}
        baseLevel.value = data.baseLevel

        weaponElement.value = data.element
        armorElement.value = data.element
    }

    override fun onAdded() {
        char = entity as CharacterEntity

        init()
    }

    private fun init() {
        bindStats()

        hp.maxValueProperty().bind(stats.totalStatProperty(MAX_HP))
        hp.restorePercentageMax(100.0)

        sp.maxValueProperty().bind(stats.totalStatProperty(MAX_SP))
        sp.restorePercentageMax(100.0)
    }

    fun getTileX() = entity.getComponent(NewCellMoveComponent::class.java).cellX

    fun getTileY() = entity.getComponent(NewCellMoveComponent::class.java).cellY

    /**
     * @return true if [target] is in weapon range of this character
     */
    fun isInWeaponRange(target: Entity) = entity.distance(target) <= attackRange * Config.spriteSize

    /**
     * @param status status
     *
     * @return true if character is under status, false otherwise
     */
    fun hasStatus(status: Status): Boolean {
        return false

        // TODO:
        //return statuses.any { it.data.status == status }
    }

    /**
     * Apply status effect.
     *
     * @param e effect
     */
    fun addStatusEffect(e: StatusEffect) {
        statuses.add(e)
    }

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).
     *
     * @param e effect
     */
    fun addEffect(e: Effect) {
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

    private fun str() = attributes.getTotalAttribute(STRENGTH)

    private fun vit() = attributes.getTotalAttribute(VITALITY)

    private fun dex() = attributes.getTotalAttribute(DEXTERITY)

    private fun agi() = attributes.getTotalAttribute(AGILITY)

    private fun int() = attributes.getTotalAttribute(INTELLECT)

    private fun wis() = attributes.getTotalAttribute(WISDOM)

    private fun wil() = attributes.getTotalAttribute(WILLPOWER)

    private fun per() = attributes.getTotalAttribute(PERCEPTION)

    private fun luc() = attributes.getTotalAttribute(LUCK)

    private fun lvl() = baseLevel.intValue()

    /**
     * Bind base stats to attributes.
     */
    private fun bindStats() {
        val str = attributes.totalAttributeProperty(STRENGTH)
        val vit = attributes.totalAttributeProperty(VITALITY)
        val dex = attributes.totalAttributeProperty(DEXTERITY)
        val agi = attributes.totalAttributeProperty(AGILITY)
        val int = attributes.totalAttributeProperty(INTELLECT)
        val wis = attributes.totalAttributeProperty(WISDOM)
        val wil = attributes.totalAttributeProperty(WILLPOWER)
        val per = attributes.totalAttributeProperty(PERCEPTION)
        val luc = attributes.totalAttributeProperty(LUCK)

        val level = baseLevel

        // TODO: recheck rounding errors and formulae
        // TODO: autoparse from Attribute?
        stats.statProperty(MAX_HP).bind(createDoubleBinding(Callable {
            1.0 + vit() * 0.5 + str() * 0.3 + lvl() * 0.25 + (vit() / 10) + charClass.value.hp * lvl()
        }, vit, str, level))

        stats.statProperty(MAX_SP).bind(createDoubleBinding(Callable {
            1.0 + wis() * 0.4 + wil() * 0.3 + lvl() * 0.25 + (wis() / 10).toDouble() + int() * 0.3 + charClass.value.sp * lvl()
        }, wis, wil, level, int))

        stats.statProperty(HP_REGEN).bind(createDoubleBinding(Callable{ 1 + vit() * 0.1 },
                vit))

        stats.statProperty(SP_REGEN).bind(createDoubleBinding(Callable{ 2 + wis() * 0.1 },
                wis))

        stats.statProperty(ATK).bind(createDoubleBinding(Callable{ str() * 0.5 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 + lvl() + (str() / 10 * (str() / 10 + 1)).toDouble() },
                str, dex, per, luc, level))

        stats.statProperty(MATK).bind(createDoubleBinding(Callable{ int() * 0.5 + wis() * 0.4 + wil() * 0.4 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 },
                int, dex, per, luc))

        stats.statProperty(DEF).bind(createDoubleBinding(Callable{ vit() * 0.5 + per() * 0.2 + str() * 0.1 + lvl() * 0.25 + vit() / 20 },
                vit, per, str, level))

        stats.statProperty(MDEF).bind(createDoubleBinding(Callable{ wil() * 0.5 + wis() * 0.3 + per() * 0.2 + int() * 0.1 + lvl() * 0.25 + (wil() / 20 * int() / 10) },
                wil, wis, per, int, level))

        stats.statProperty(ASPD).bind(createDoubleBinding(Callable{ agi() * 0.5 + dex() * 0.2 },
                agi, dex))

        stats.statProperty(MSPD).bind(createDoubleBinding(Callable{ dex() * 0.3 + wil() * 0.1 + wis() * 0.1 + int() * 0.1 + per() * 0.1 + luc() * 0.1 },
                dex, wil, wis, int, per, luc))

        stats.statProperty(CRIT_CHANCE).bind(createDoubleBinding(Callable{ luc() * 0.5 + per() * 0.1 + wis() * 0.1 },
                luc, per, wis))

        stats.statProperty(MCRIT_CHANCE).bind(createDoubleBinding(Callable{ luc() * 0.5 + wil() * 0.2 + per() * 0.1 },
                luc, wil, per))

        stats.statProperty(CRIT_DMG).bind(createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))

        stats.statProperty(MCRIT_DMG).bind(createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))
    }

    private var regenTick = 0.0

    /**
     * Regeneration tick. HP/SP.
     */
    private fun updateRegen(tpf: Double) {
        regenTick += tpf

        if (regenTick >= Config.REGEN_INTERVAL) {

            if (!hasStatus(Status.POISONED)) {
                hp.restore(stats.getTotalStat(HP_REGEN))
                sp.restore(stats.getTotalStat(SP_REGEN))
            } else {
                hp.damagePercentageMax(1.0)
                sp.damagePercentageMax(1.0)
            }

            regenTick = 0.0
        }

        // effects may have ended, leaving us with more hp / sp than max, so limit it.
        if (hp.value > hp.maxValue) {
            hp.restorePercentageMax(100.0)
        }

        if (sp.value > sp.maxValue) {
            sp.restorePercentageMax(100.0)
        }
    }

    private fun updateSkills(tpf: Double) {
        skills.forEach {
            it.onUpdate(tpf)
            //if (it.data.type == SkillType.PASSIVE) {
                //it.data.onCast(char, char, it.level.value)
            //}
        }
    }

    private fun updateEffects(tpf: Double) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.duration -= tpf
            if (e.duration <= 0) {
                e.onEnd(char)
                it.remove()
            }
        }
    }

    private fun updateStatusEffects(tpf: Double) {
        val it = statuses.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.duration.value -= tpf
            if (e.duration.value <= 0) {
                it.remove()
            }
        }
    }

    override fun onUpdate(tpf: Double) {
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
    open fun canAttack() =
            atkTick >= Config.SLOWEST_ATTACK_INTERVAL - stats.getTotalStat(ASPD) / 100.0

    /**
     * Performs basic attack with equipped weapon on the [target].
     * Damage is physical and element depends on weapon element.
     *
     * @return damage dealt
     */
    fun attack(target: CharacterEntity): DamageResult {
        fire(OnAttackEvent(char, target))

        return dealPhysicalDamage(target, stats.getTotalStat(ATK) + 2 * GameMath.random(lvl()), weaponElement.value)
    }

    /**
     * Deals physical damage with [baseDamage] amount to [target].
     * The damage is reduced by armor and defense.
     * The damage will be of [element] element.
     *
     * @return damage dealt
     */
    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): DamageResult {
        var baseDamage = baseDamage

        var crit = false

        if (GameMath.checkChance(stats.getTotalStat(CRIT_CHANCE))) {
            baseDamage *= stats.getTotalStat(CRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = element.getDamageModifierAgainst(target.characterComponent.armorElement.value);

        val damageAfterReduction = (100 - target.stats.getTotalStat(ARM)) * baseDamage / 100.0 - target.stats.getTotalStat(DEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
        target.hp.damage(totalDamage.toDouble())

        return DamageResult(DamageType.PHYSICAL, element, totalDamage, crit)
    }

    /**
     * Deals physical [baseDamage] of type NEUTRAL to [target].
     * The damage is reduced by target's armor and DEF.
     *
     * @return damage dealt
     */
    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double): DamageResult {
        return dealPhysicalDamage(target, baseDamage, Element.NEUTRAL)
    }

    /**
     * Deal magical [baseDamage] of type [element] to [target].
     * The damage is reduced by target's magical armor and MDEF.
     *
     * @return damage dealt
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): DamageResult {
//        var baseDamage = baseDamage
//
//        var crit = false
//
//        if (GameMath.checkChance(stats.getTotalStat(MCRIT_CHANCE))) {
//            baseDamage *= stats.getTotalStat(MCRIT_DMG)
//            crit = true
//        }
//
//        val elementalDamageModifier = element.getDamageModifierAgainst(target.armorElement.value);
//
//        val damageAfterReduction = (100 - target.stats.getTotalStat(MARM)) * baseDamage / 100.0 - target.stats.getTotalStat(MDEF)
//
//        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
//        target.hp.damage(totalDamage.toDouble())
//
//        return DamageResult(DamageType.MAGICAL, element, totalDamage, crit)
        return DamageResult.NONE
    }

    /**
     * Deal magical [baseDamage] of type NEUTRAL to [target].
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double): DamageResult {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL)
    }

    /**
     * Deals the exact amount of [value] damage to [target].
     */
    fun dealPureDamage(target: CharacterEntity, value: Double): DamageResult {
        val amount = value.toInt()
        (target).hp.damage(amount.toDouble())

        return DamageResult(DamageType.PURE, Element.NEUTRAL, amount, false)
    }

    fun useSelfSkill(index: Int): SkillUseResult {
//        val skill = skills[index]
//
//        if (skill.level.value == 0)
//            return SkillUseResult.NONE
//
//        if (skill.currentCooldown.value > 0)
//            return SkillUseResult.ON_COOLDOWN
//
//        if (skill.data.mana > sp.value)
//            return SkillUseResult.NO_MANA
//
//        sp.value -= skill.data.mana
//        skill.putOnCooldown()
//
//        skill.data.onCast(char, char, skill)

        return SkillUseResult.NONE
    }

    fun useTargetSkill(index: Int, target: Entity): SkillUseResult {
        return useTargetSkill(skills[index], target)
    }

    fun useTargetSkill(skill: SkillComponent, target: Entity): SkillUseResult {
//        if (skill.level.value == 0)
//            return SkillUseResult.NONE
//
//        if (skill.currentCooldown.value > 0)
//            return SkillUseResult.ON_COOLDOWN
//
//        if (skill.data.mana > sp.value)
//            return SkillUseResult.NO_MANA
//
//        // TODO: do these checks before using skills
//
//        sp.value -= skill.manaCost.intValue()
//        skill.putOnCooldown()
//
//        return skill.data.onCast(char, target, skill)

        return SkillUseResult.NONE
    }

    fun useAreaSkill(index: Int, target: Point2D): SkillUseResult {
        // TODO: complete
        //return skills[index].data.onCast(char, char, skills[index])

        return SkillUseResult.NONE
    }

    fun kill() {
        char.components
                .filter { it !is AnimationComponent && it !is ViewComponent }
                .forEach {
                    it.pause()
                }

        char.animationComponent.playDeath(Runnable { char.removeFromWorld() })
    }

    fun useItem(item: UsableItem) {
        // TODO: allow stacks of items
        item.onUse(char)
        inventory.removeItem(item)
    }
}
