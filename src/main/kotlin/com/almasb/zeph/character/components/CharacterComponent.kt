package com.almasb.zeph.character.components

import com.almasb.fxgl.core.collection.PropertyMap
import com.almasb.fxgl.dsl.components.HealthDoubleComponent
import com.almasb.fxgl.dsl.components.ManaDoubleComponent
import com.almasb.fxgl.dsl.fire
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.ViewComponent
import com.almasb.zeph.Config
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.*
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.events.*
import com.almasb.zeph.fxglinventory.Inventory
import com.almasb.zeph.item.Item
import com.almasb.zeph.item.UsableItem
import com.almasb.zeph.skill.Skill
import com.almasb.zeph.skill.SkillUseResult
import javafx.beans.binding.Bindings.createDoubleBinding
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Point2D
import java.util.concurrent.Callable

open class CharacterComponent(val data: CharacterData) : Component() {

    private lateinit var char: CharacterEntity

    val charClass = SimpleObjectProperty(data.charClass)

    val baseLevel = SimpleIntegerProperty(data.baseLevel)
    val statLevel = SimpleIntegerProperty(1)
    val jobLevel = SimpleIntegerProperty(1)

    val hp = HealthDoubleComponent(1.0)
    val sp = ManaDoubleComponent(1.0)

    private val attributes = PropertyMap()
    private val stats = PropertyMap()

    val weaponElement = SimpleObjectProperty(data.element)
    val armorElement = SimpleObjectProperty(data.element)

    val inventory = Inventory<Item>(Config.MAX_INVENTORY_SIZE).also { it.isRemoveItemsIfQty0 = true }
    val skills = FXCollections.observableArrayList<Skill>()

    // what XP does this char give on death
    val baseXP = SimpleIntegerProperty()
    val attrXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    /**
     * In cells.
     */
    val attackRange: Int = data.attackRange

    init {
        data.attributes.attrs.forEach { attr, value ->
            setBase(attr, value)
        }

        Stat.values().forEach { setBase(it, 0) }
        
        Attribute.values().forEach { 
            setBonus(it, 0) 
            initTotal(it)
        }
        Stat.values().forEach { 
            setBonus(it, 0) 
            initTotal(it)
        }
    }

    override fun onAdded() {
        char = entity as CharacterEntity

        init()
    }

    private fun init() {
        bindStats()

        hp.maxValueProperty().bind(totalProperty(MAX_HP))
        hp.restorePercentageMax(100.0)

        sp.maxValueProperty().bind(totalProperty(MAX_SP))
        sp.restorePercentageMax(100.0)
    }

    fun setBase(attribute: Attribute, value: Int) {
        attributes.setValue("base$attribute", value)
    }

    fun setBase(stat: Stat, value: Int) {
        stats.setValue("base$stat", value)
    }

    private fun setBonus(attribute: Attribute, value: Int) {
        attributes.setValue("bonus$attribute", value)
    }

    private fun setBonus(stat: Stat, value: Int) {
        stats.setValue("bonus$stat", value)
    }

    private fun initTotal(attribute: Attribute) {
        attributes.setValue("total$attribute", 0)
        totalProperty(attribute).bind(baseProperty(attribute).add(bonusProperty(attribute)))
    }

    private fun initTotal(stat: Stat) {
        stats.setValue("total$stat", 0)
        totalProperty(stat).bind(baseProperty(stat).add(bonusProperty(stat)))
    }

    fun getBase(attribute: Attribute): Int = baseProperty(attribute).value
    fun getBase(stat: Stat): Int = baseProperty(stat).value
    fun getTotal(attribute: Attribute): Int = totalProperty(attribute).value
    fun getTotal(stat: Stat): Int = totalProperty(stat).value

    fun addBonus(attribute: Attribute, value: Int) {
        attributes.increment("bonus$attribute", value)
    }

    fun addBonus(stat: Stat, value: Int) {
        stats.increment("bonus$stat", value)
    }

    fun baseProperty(attribute: Attribute): IntegerProperty = attributes.intProperty("base$attribute")
    fun baseProperty(stat: Stat): IntegerProperty = stats.intProperty("base$stat")
    fun bonusProperty(attribute: Attribute): IntegerProperty = attributes.intProperty("bonus$attribute")
    fun bonusProperty(stat: Stat): IntegerProperty = stats.intProperty("bonus$stat")
    fun totalProperty(attribute: Attribute): IntegerProperty = attributes.intProperty("total$attribute")
    fun totalProperty(stat: Stat): IntegerProperty = stats.intProperty("total$stat")

    private fun str() = getTotal(STRENGTH)

    private fun vit() = getTotal(VITALITY)

    private fun dex() = getTotal(DEXTERITY)

    private fun agi() = getTotal(AGILITY)

    private fun int() = getTotal(INTELLECT)

    private fun wis() = getTotal(WISDOM)

    private fun wil() = getTotal(WILLPOWER)

    private fun per() = getTotal(PERCEPTION)

    private fun luc() = getTotal(LUCK)

    private fun lvl() = baseLevel.intValue()

    /**
     * Bind base stats to attributes.
     */
    private fun bindStats() {
        val str = totalProperty(STRENGTH)
        val vit = totalProperty(VITALITY)
        val dex = totalProperty(DEXTERITY)
        val agi = totalProperty(AGILITY)
        val int = totalProperty(INTELLECT)
        val wis = totalProperty(WISDOM)
        val wil = totalProperty(WILLPOWER)
        val per = totalProperty(PERCEPTION)
        val luc = totalProperty(LUCK)

        val level = baseLevel

        // TODO: recheck rounding errors and formulae
        // TODO: autoparse from Attribute?
        
        baseProperty(MAX_HP).bind(createDoubleBinding(Callable {
            1.0 + vit() * 0.5 + str() * 0.3 + lvl() * 0.25 + (vit() / 10) + charClass.value.hp * lvl()
        }, vit, str, level))

        baseProperty(MAX_SP).bind(createDoubleBinding(Callable {
            1.0 + wis() * 0.4 + wil() * 0.3 + lvl() * 0.25 + (wis() / 10).toDouble() + int() * 0.3 + charClass.value.sp * lvl()
        }, wis, wil, level, int))

        baseProperty(HP_REGEN).bind(createDoubleBinding(Callable{ 1 + vit() * 0.1 },
                vit))

        baseProperty(SP_REGEN).bind(createDoubleBinding(Callable{ 2 + wis() * 0.1 },
                wis))

        baseProperty(ATK).bind(createDoubleBinding(Callable{ str() * 0.5 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 + lvl() + (str() / 10 * (str() / 10 + 1)).toDouble() },
                str, dex, per, luc, level))

        baseProperty(MATK).bind(createDoubleBinding(Callable{ int() * 0.5 + wis() * 0.4 + wil() * 0.4 + dex() * 0.3 + per() * 0.2 + luc() * 0.1 },
                int, dex, per, luc))

        baseProperty(DEF).bind(createDoubleBinding(Callable{ vit() * 0.5 + per() * 0.2 + str() * 0.1 + lvl() * 0.25 + vit() / 20 },
                vit, per, str, level))

        baseProperty(MDEF).bind(createDoubleBinding(Callable{ wil() * 0.5 + wis() * 0.3 + per() * 0.2 + int() * 0.1 + lvl() * 0.25 + (wil() / 20 * int() / 10) },
                wil, wis, per, int, level))

        baseProperty(ASPD).bind(createDoubleBinding(Callable{ agi() * 0.5 + dex() * 0.2 },
                agi, dex))

        baseProperty(MSPD).bind(createDoubleBinding(Callable{ dex() * 0.3 + wil() * 0.1 + wis() * 0.1 + int() * 0.1 + per() * 0.1 + luc() * 0.1 },
                dex, wil, wis, int, per, luc))

        baseProperty(CRIT_CHANCE).bind(createDoubleBinding(Callable{ luc() * 0.5 + per() * 0.1 + wis() * 0.1 },
                luc, per, wis))

        baseProperty(MCRIT_CHANCE).bind(createDoubleBinding(Callable{ luc() * 0.5 + wil() * 0.2 + per() * 0.1 },
                luc, wil, per))

        baseProperty(CRIT_DMG).bind(createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))

        baseProperty(MCRIT_DMG).bind(createDoubleBinding(Callable{ 2 + luc() * 0.01 },
                luc))
    }

    override fun onUpdate(tpf: Double) {
        if (hp.isZero)
            return

        updateRegen(tpf)

        if (!canAttack())
            atkTick += tpf

        updateSkills(tpf)
    }

    private var regenTick = 0.0

    /**
     * Regeneration tick. HP/SP.
     */
    private fun updateRegen(tpf: Double) {
        regenTick += tpf

        if (regenTick >= Config.REGEN_INTERVAL) {

            if (!char.hasStatus(Status.POISONED)) {
                hp.restore(getTotal(HP_REGEN).toDouble())
                sp.restore(getTotal(SP_REGEN).toDouble())
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
        }
    }

    /**
     * @return true if [target] is in weapon range of this character
     */
    fun isInWeaponRange(target: CharacterEntity): Boolean =
            char.distance(target.cellX, target.cellY) <= attackRange

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
            atkTick >= Config.SLOWEST_ATTACK_INTERVAL - getTotal(ASPD) / 100.0

    /**
     * Performs basic attack with equipped weapon on the [target].
     * Damage is physical and element depends on weapon element.
     *
     * @return damage dealt
     */
    fun attack(target: CharacterEntity): DamageResult {
        fire(OnAttackEvent(char, target))

        return dealPhysicalDamage(target, getTotal(ATK).toDouble() + 2 * GameMath.random(lvl()), weaponElement.value)
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
     * Deals physical damage with [baseDamage] amount to [target].
     * The damage is reduced by armor and defense.
     * The damage will be of [element] element.
     *
     * @return damage dealt
     */
    fun dealPhysicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): DamageResult {
        var baseDamage = baseDamage

        var crit = false

        if (GameMath.checkChance(getTotal(CRIT_CHANCE))) {
            baseDamage *= getTotal(CRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = element.getDamageModifierAgainst(target.characterComponent.armorElement.value);

        val damageAfterReduction = (100 - target.getTotal(ARM)) * baseDamage / 100.0 - target.getTotal(DEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
        target.hp.damage(totalDamage.toDouble())

        fire(OnPhysicalDamageDealtEvent(char, target, totalDamage, crit))

        if (target.hp.isZero) {
            fire(OnBeingKilledEvent(char, target))

            char.actionComponent.orderIdle()

            if (!target.isPlayer)
                target.characterComponent.kill()
        }

        return DamageResult(DamageType.PHYSICAL, element, totalDamage, crit)
    }

    /**
     * Deal magical [baseDamage] of type NEUTRAL to [target].
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double): DamageResult {
        return dealMagicalDamage(target, baseDamage, Element.NEUTRAL)
    }

    /**
     * Deal magical [baseDamage] of type [element] to [target].
     * The damage is reduced by target's magical armor and MDEF.
     *
     * @return damage dealt
     */
    fun dealMagicalDamage(target: CharacterEntity, baseDamage: Double, element: Element): DamageResult {
        var baseDamage = baseDamage

        var crit = false

        if (GameMath.checkChance(getTotal(MCRIT_CHANCE))) {
            baseDamage *= getTotal(MCRIT_DMG)
            crit = true
        }

        val elementalDamageModifier = element.getDamageModifierAgainst(target.armorElement.value);

        val damageAfterReduction = (100 - target.getTotal(MARM)) * baseDamage / 100.0 - target.getTotal(MDEF)

        val totalDamage = Math.max(Math.round(elementalDamageModifier * damageAfterReduction), 0).toInt()
        target.hp.damage(totalDamage.toDouble())

        fire(OnMagicalDamageDealtEvent(char, target, totalDamage, crit))

        if (target.hp.isZero) {
            fire(OnBeingKilledEvent(char, target))

            char.actionComponent.orderIdle()

            if (!target.isPlayer)
                target.characterComponent.kill()
        }

        return DamageResult(DamageType.MAGICAL, element, totalDamage, crit)
    }

    /**
     * Deals the exact amount of [value] damage to [target].
     */
    fun dealPureDamage(target: CharacterEntity, value: Double) {
        target.hp.damage(value)

        // TODO:
        //fire(OnPhysicalDamageDealtEvent(char, target, totalDamage, crit))
    }

    fun useSelfSkill(index: Int): SkillUseResult {
        val skill = skills[index]

        if (skill.levelProperty.value == 0)
            return SkillUseResult.NONE

        if (skill.currentCooldown.value > 0)
            return SkillUseResult.ON_COOLDOWN

        if (skill.manaCost.value > sp.value)
            return SkillUseResult.NO_MANA

        useTargetSkill(skill, char)

        return SkillUseResult.NONE
    }

    fun useTargetSkill(index: Int, target: CharacterEntity): SkillUseResult {
        return useTargetSkill(skills[index], target)
    }

    fun useTargetSkill(skill: Skill, target: CharacterEntity): SkillUseResult {
        fire(OnBeforeSkillCastEvent(char, skill))

        sp.value -= skill.manaCost.intValue()
        skill.putOnCooldown()

        skill.onCast(char, target)

        return SkillUseResult.NONE
    }

    fun useAreaSkill(index: Int, target: Point2D): SkillUseResult {
        // TODO: complete
        //return skills[index].data.onCast(char, char, skills[index])

        return SkillUseResult.NONE
    }

    private fun kill() {
        char.components
                .filter { it !is AnimationComponent && it !is ViewComponent }
                .forEach {
                    it.pause()
                }

        char.animationComponent.playDeath(Runnable { char.removeFromWorld() })
    }

    fun useItem(item: UsableItem) {
        item.onUse(char)
        inventory.incrementQuantity(item, -1)

        fire(OnItemUsedEvent(char, item))
    }
}
