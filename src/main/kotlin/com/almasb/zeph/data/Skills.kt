package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Effect
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.combat.effect
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

    @JvmField
    val Crusader = Crusader()

    @JvmField
    val Gladiator = Gladiator()

    @JvmField
    val Mage = Mage()

    @JvmField
    val Wizard = Wizard()

    @JvmField
    val Enchanter = Enchanter()

    @JvmField
    val Scout = Scout()

    @JvmField
    val Rogue = Rogue()

    @JvmField
    val Ranger = Ranger()
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

            caster.addEffect(Effect(effect {
                description = this@skill.description

                duration = 7.0

                STRENGTH + 3*skill.level.value
                VITALITY + 2*skill.level.value
            }))
        }
    }
}

class Crusader {

    val HOLY_LIGHT = skill {
        desc {
            id = 7110
            name = "Holy Light"
            description = "Heals and increases VIT for the duration."
            textureName = "skills/ic_skill_holy_light.png"
        }

        type = ACTIVE
        useType = RESTORE
        targetTypes = of(ALLY)

        manaCost = 35
        cooldown = 15.0

        onCastScript = { caster, target, skill ->
            target.hp.restore(30 + skill.level.value*10.0)

            // 20 seconds with new Rune(Attribute.VITALITY, level*2)

            caster.addEffect(Effect(effect {
                description = this@skill.description

                duration = 20.0

                VITALITY + 2*skill.level.value
            }))
        }
    }

}

class Gladiator {

    val BASH = skill {
        desc {
            id = 7210
            name = "Bash"
            description = "A powerful physical attack that stuns the target"
            textureName = "skills/ic_skill_bash.png"
        }

        type = ACTIVE
        useType = DAMAGE
        targetTypes = of(ENEMY)

        manaCost = 35
        cooldown = 15.0

        onCastScript = { caster, target, skill ->

//            float dmg = (1 + (15 + 5*level) / 100.0f) * caster.getTotalStat(Stat.ATK);
//            int d = caster.dealPhysicalDamage(target, dmg);
//            target.addStatusEffect(new StatusEffect(Status.STUNNED, 5.0f));
//
//            useResult = new SkillUseResult(GameMath.normalizeDamage(d) + ",STUNNED");
        }
    }
}

class Mage {

}

class Wizard {

}

class Enchanter {

}

class Scout {

}

class Rogue {

}

class Ranger {

}