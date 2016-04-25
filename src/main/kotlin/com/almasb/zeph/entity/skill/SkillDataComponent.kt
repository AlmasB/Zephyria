package com.almasb.zeph.entity.skill

import com.almasb.ents.AbstractComponent
import com.almasb.ents.Entity
import com.almasb.zeph.entity.character.CharacterEntity
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SkillDataComponent(val type: SkillType, val useType: SkillUseType, val targetTypes: EnumSet<SkillTargetType>) : AbstractComponent() {

    var mana = 0
    var cooldown = 0.0

    var areaSize = 0

    fun withMana(m: Int): SkillDataComponent {
        mana = m
        return this
    }

    fun withCooldown(c: Double): SkillDataComponent {
        cooldown = c
        return this
    }

    fun withArea(size: Int): SkillDataComponent {
        areaSize = size
        return this
    }

    lateinit var onCast: (CharacterEntity, CharacterEntity, SkillEntity) -> SkillUseResult

    fun onCast(func: (CharacterEntity, CharacterEntity, SkillEntity) -> SkillUseResult): SkillDataComponent {
        onCast = func
        return this
    }

//    lateinit var onPassive: (CharacterEntity, CharacterEntity, SkillEntity) -> Unit
//
//    fun onPassive(func: (CharacterEntity, CharacterEntity, SkillEntity) -> Unit): SkillDataComponent {
//        onPassive = func
//        return this
//    }

    /**
     * Projectile texture name (if applicable).
     */
    lateinit var textureName: String

    fun withTextureName(name: String): SkillDataComponent {
        textureName = name
        return this
    }

    // TODO: sound
    // TODO: on skill end func?

    // TODO: default noop so we can call onLearn without checks
    lateinit var onLearn: (CharacterEntity, SkillEntity) -> Unit

    fun onLearn(func: (CharacterEntity, SkillEntity) -> Unit): SkillDataComponent {
        onLearn = func
        return this
    }
}