package com.almasb.zeph.skill

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Essence
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.emptyDescription
import javafx.util.Duration
import java.util.*


@DataDSL
class SkillDataBuilder(
        var description: Description = emptyDescription,
        var type: SkillType = SkillType.ACTIVE,
        var targetTypes: EnumSet<SkillTargetType> = EnumSet.of(SkillTargetType.ENEMY),
        var manaCost: Int = 0,
        var cooldown: Double = 0.0,
        var hasProjectile: Boolean = false,
        var projectileTextureName: String = "",
        var soundEffectName: String = "null_object.wav",
        var passiveRunes: MutableList<Rune> = arrayListOf(),
        var passiveEssences: MutableList<Essence> = arrayListOf(),
        var onCastScript: (CharacterEntity, CharacterEntity, Skill) -> Unit = { caster, target, skill -> },
        var onLearnScript: (CharacterEntity, Skill) -> Unit = { char, skill -> }

) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    operator fun Attribute.plus(value: Int) {
        passiveRunes.add(Rune(this, value))
    }

    operator fun Stat.plus(value: Int) {
        passiveEssences.add(Essence(this, value))
    }

    fun build(): SkillData {
        if (description.textureName.isEmpty()) {
            val fileName = description.name.toLowerCase().replace(" ", "_") + ".png"

            description = description.copy(textureName = "skills/$fileName")
        }

        return SkillData(
                description,
                type,
                targetTypes,
                manaCost,
                cooldown,
                hasProjectile,
                projectileTextureName,
                soundEffectName,
                passiveRunes,
                passiveEssences,
                onCastScript,
                onLearnScript
        )
    }
}

@DataDSL
fun activeSkill(setup: SkillDataBuilder.() -> Unit): SkillData {
    val builder = SkillDataBuilder()
    builder.setup()
    return builder.build()
}

@DataDSL
fun passiveSkill(setup: SkillDataBuilder.() -> Unit): SkillData {
    val builder = SkillDataBuilder()
    builder.setup()
    builder.type = SkillType.PASSIVE
    return builder.build()
}

data class SkillData(
        val description: Description,
        val type: SkillType,
        val targetTypes: EnumSet<SkillTargetType>,
        val manaCost: Int,
        val cooldown: Double,
        val hasProjectile: Boolean,
        val projectileTextureName: String,
        val soundEffectName: String,

        // these passives ones are per level
        val passiveRunes: List<Rune>,
        val passiveEssences: List<Essence>,
        val onCastScript: (CharacterEntity, CharacterEntity, Skill) -> Unit,
        val onLearnScript: (CharacterEntity, Skill) -> Unit
)