package com.almasb.zeph.skill

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import javafx.util.Duration
import java.util.*


@DataDSL
class SkillDataBuilder(
        var description: Description = Description(),
        var type: SkillType = SkillType.ACTIVE,
        var useType: SkillUseType = SkillUseType.DAMAGE,
        var targetTypes: EnumSet<SkillTargetType> = EnumSet.of(SkillTargetType.ENEMY),
        var manaCost: Int = 0,
        var cooldown: Double = 0.0,
        var hasProjectile: Boolean = false,
        var projectileTextureName: String = "null_object.png",
        var soundEffectName: String = "null_object.wav",
        var onCastScript: (CharacterEntity, CharacterEntity, Skill) -> Unit = { caster, target, skill -> }

) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun Int.sec(): Duration {
        return Duration.seconds(this.toDouble())
    }

    fun Double.sec(): Duration {
        return Duration.seconds(this)
    }

    fun build(): SkillData {
        return SkillData(
                description,
                type,
                useType,
                targetTypes,
                manaCost,
                cooldown,
                hasProjectile,
                projectileTextureName,
                soundEffectName,
                onCastScript
        )
    }
}

@DataDSL
fun skill(setup: SkillDataBuilder.() -> Unit): SkillData {
    val builder = SkillDataBuilder()
    builder.setup()
    return builder.build()
}

data class SkillData(
        val description: Description,
        val type: SkillType,
        val useType: SkillUseType,
        val targetTypes: EnumSet<SkillTargetType>,
        val manaCost: Int,
        val cooldown: Double,
        val hasProjectile: Boolean,
        val projectileTextureName: String,
        val soundEffectName: String,
        val onCastScript: (CharacterEntity, CharacterEntity, Skill) -> Unit
)