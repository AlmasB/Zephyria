package com.almasb.zeph.data

import com.almasb.zeph.skill.SkillTargetType
import com.almasb.zeph.skill.SkillTargetType.*
import com.almasb.zeph.skill.SkillType
import com.almasb.zeph.skill.SkillType.*
import com.almasb.zeph.skill.SkillUseType
import com.almasb.zeph.skill.SkillUseType.*
import com.almasb.zeph.skill.skill
import java.util.*
import java.util.EnumSet.*

/**
 * Skills id range [7000..7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Skills {

    @JvmField
    val Warrior = Warrior()

}

class Warrior {
    val ROAR = skill {
        desc {
            id = 7010
            name = "Roar"
            description = "Increases STR and VIT for the duration."
            textureName = "skills/ic_skill_roar.png"
        }

        type = ACTIVE
        useType = EFFECT
        targetTypes = of(SELF)


        manaCost = 10
        cooldown = 14.0

        onCastScript = { caster, target, skill ->

            println("Cast $caster on $target. Skill $skill")

            //                                val effect = EffectEntity(listOf(
//                                        Description(7010, "Roar", "Roar", "effects/attr_up.png"),
//                                        EffectDataComponent(7.0)
//                                                .withRune(Rune(Attribute.STRENGTH, 3 * skill.level.value))
//                                                .withRune(Rune(Attribute.VITALITY, 2 * skill.level.value))
//                                ))

            //caster.characterComponent.addEffect()
        }
    }
}