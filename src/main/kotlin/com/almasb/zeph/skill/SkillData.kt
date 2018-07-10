package com.almasb.zeph.skill

import com.almasb.fxgl.entity.Entity
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class SkillData(val type: SkillType, val useType: SkillUseType, val targetTypes: EnumSet<SkillTargetType>) {

    var mana = 0
    var cooldown = 0.0

    var areaSize = 0

    fun withMana(m: Int): SkillData {
        mana = m
        return this
    }

    fun withCooldown(c: Double): SkillData {
        cooldown = c
        return this
    }

    fun withArea(size: Int): SkillData {
        areaSize = size
        return this
    }

    lateinit var onCast: (Entity, Entity, SkillComponent) -> SkillUseResult

    fun onCast(func: (Entity, Entity, SkillComponent) -> SkillUseResult): SkillData {
        onCast = func
        return this
    }

//    lateinit var onPassive: (CharacterEntity, CharacterEntity, SkillEntity) -> Unit
//
//    fun onPassive(func: (CharacterEntity, CharacterEntity, SkillEntity) -> Unit): SkillDataComponent {
//        onPassive = func
//        return this
//    }

    // allows us to check if we need to create a projectile or do a simple attack
    var hasProjectile = false

    /**
     * Projectile texture name (if applicable).
     */
    lateinit var textureName: String

    fun withTextureName(name: String): SkillData {
        textureName = name
        hasProjectile = true
        return this
    }

    // TODO: sound
    // TODO: on skill end func?

    // TODO: default noop so we can call onLearn without checks
    lateinit var onLearn: (Entity, SkillComponent) -> Unit

    fun onLearn(func: (Entity, SkillComponent) -> Unit): SkillData {
        onLearn = func
        return this
    }
}