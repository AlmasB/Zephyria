package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Element.*
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.combat.Status
import com.almasb.zeph.combat.Status.*
import com.almasb.zeph.combat.effect
import com.almasb.zeph.skill.SkillTargetType.*
import com.almasb.zeph.skill.SkillType.ACTIVE
import com.almasb.zeph.skill.SkillType.PASSIVE
import com.almasb.zeph.skill.SkillUseType.*
import com.almasb.zeph.skill.passiveSkill
import com.almasb.zeph.skill.skill
import java.util.EnumSet.of

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

            caster.addEffect(effect(this) {
                duration = 7.0

                STRENGTH + 3*skill.level
                VITALITY + 2*skill.level
            })
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
            target.hp.restore(30 + skill.level*10.0)

            caster.addEffect(effect(this) {
                duration = 20.0

                VITALITY + 2*skill.level
            })
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

        manaCost = 40
        cooldown = 15.0

        onCastScript = { caster, target, skill ->
            val dmg = (1 + (15 + 5*skill.level) / 100.0) * caster.getTotal(ATK)
            val result = caster.characterComponent.dealPhysicalDamage(target, dmg)

            target.addEffect(effect(this) {
                status = STUNNED
                duration = 5.0
            })

//            useResult = new SkillUseResult(GameMath.normalizeDamage(d) + ",STUNNED");
        }
    }
}

class Mage {

    val FIREBALL = skill {
        desc {
            id = 7020
            name = "Fireball"
            description = "Deals magic damage with FIRE element."
            textureName = "skills/ic_skill_fireball.png"
        }

        useType = DAMAGE
        targetTypes = of(ENEMY)

        manaCost = 1
        cooldown = 1.0

        projectileTextureName = "projectiles/fireball.png"

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) + skill.level * 20.0

            caster.characterComponent.dealMagicalDamage(target, dmg, FIRE)

            // SkillUseResult(caster.charConrol.dealMagicalDamage(target, dmg, Element.FIRE))
        }
    }

    val EARTH_BOULDER = skill {
        desc {
            id = 7023
            name = "Earth Boulder"
            description = "Deals magic damage with EARTH element."
            textureName = "skills/ic_skill_earth_boulder.png"
        }

        type = ACTIVE
        useType = DAMAGE
        targetTypes = of(ENEMY)

        manaCost = 25
        cooldown = 7.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) + skill.level * 25.0
            val result = caster.characterComponent.dealMagicalDamage(target, dmg, EARTH)

            // useResult = new SkillUseResult(d);
        }
    }
}

class Wizard {

    val MAGIC_MASTERY = passiveSkill {
        desc {
            id = 7120
            name = "Magic Mastery"
            description = "Passively increases INT and WIL."
        }

        INTELLECT +2
        WILLPOWER +2
    }

    val AMPLIFY_MAGIC = skill {
        desc {
            id = 7121
            name = "Amplify Magic"
            description = "Increases MATK for the duration."
            //textureName =
        }

        targetTypes = of(SELF)

        manaCost = 1
        cooldown = 1.0

        onCastScript = { caster, target, skill ->
            caster.addEffect(effect(this) {

                duration = 10.0

                MATK +10*skill.level
            })

            //                useResult = new SkillUseResult("MATK +" + 10*level);
        }
    }





}

class Enchanter {

}

class Scout {

}

class Rogue {

}

class Ranger {

}