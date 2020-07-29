package com.almasb.zeph.data

import com.almasb.zeph.combat.*
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element.*
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.combat.Status.*
import com.almasb.zeph.skill.SkillTargetType.*
import com.almasb.zeph.skill.passiveSkill
import com.almasb.zeph.skill.activeSkill
import java.lang.Math.*
import java.util.EnumSet.of

/**
 * Skills id range [7000..7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Skills {

    @JvmField
    val Novice = Novice()

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

class Novice {
    val LEARNER = passiveSkill {
        // TODO:
    }
}

class Warrior {
    val ROAR = activeSkill {
        desc {
            id = 7010
            name = "Roar"
            description = "Increases STR and VIT for the duration."
        }

        targetTypes = of(SELF)

        manaCost = 10
        cooldown = 14.0

        onCastScript = { caster, target, skill ->

            caster.addEffect(effect(description) {
                duration = 7.0

                STRENGTH + 3*skill.level
                VITALITY + 2*skill.level
            })
        }
    }

    val MIGHTY_SWING = activeSkill {
        desc {
            id = 7011
            name = "Mighty Swing"
            description = "Physical attack. Damage is greater if you have more STR than your target."
        }

        manaCost = 25
        cooldown = 15.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            val diff = caster.getTotal(STRENGTH) - target.getTotal(STRENGTH)
            val dmg = (max(diff, 0) + 10 * skill.level) * 2.0

            caster.dealPhysicalDamage(target, dmg)
        }
    }

    val WARRIOR_HEART = passiveSkill {
        desc {
            id = 7012
            name = "Warrior's Heart"
            description = "Passively increases max HP."
            textureName = "skills/warrior_heart.png"
        }

        MAX_HP +30
    }

    val ARMOR_MASTERY = passiveSkill {
        desc {
            id = 7013
            name = "Armor Mastery"
            description = "Increases ARM."
        }

        ARM +1
    }
}

class Crusader {

    val HOLY_LIGHT = activeSkill {
        desc {
            id = 7110
            name = "Holy Light"
            description = "Heals and increases VIT for the duration."
        }

        targetTypes = of(SELF, ALLY)

        manaCost = 35
        cooldown = 15.0

        onCastScript = { caster, target, skill ->
            target.hp.restore(30 + skill.level*10.0)

            caster.addEffect(effect(description) {
                duration = 20.0

                VITALITY + 2*skill.level
            })
        }
    }

    val FAITH = passiveSkill {
        desc {
            id = 7111
            name = "Faith"
            description = "Increases STR. Further increases bonus given by Warrior's Heart skill."
        }

        STRENGTH +1
        MAX_HP +30
    }

    val DIVINE_ARMOR = passiveSkill {
        desc {
            id = 7112
            name = "Divine Armor"
            description = "Increases VIT. Further increases bonus given by Armor Mastery skill."
        }

        VITALITY +1
        ARM +1
    }

    val PRECISION_STRIKE = activeSkill {
        desc {
            id = 7113
            name = "Precision Strike"
            description = "Deals armor ignoring damage based on STR."
        }

        manaCost = 35
        cooldown = 20.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            // TODO: Deals armor ignoring damage based on STR.

            //                float dmg = caster.getTotalStat(Stat.ATK) + (caster.getTotalAttribute(Attribute.STRENGTH) / 10) * level;
            //                caster.dealPureDamage(target, dmg);
        }
    }

    val LAST_STAND = activeSkill {
        desc {
            id = 7114
            name = "Last Stand"
            description = "Significantly increases STR and MAX_HP for the duration. Consumes all available SP."
        }

        manaCost = 0
        cooldown = 120.0

        targetTypes = of(SELF)

        onCastScript = { caster, target, skill ->

            caster.sp.damageFully()

            caster.addEffect(effect(description) {
                duration = 30.0

                STRENGTH +5*skill.level
                MAX_HP +50*skill.level
            })
        }
    }
}

class Gladiator {

    val BASH = activeSkill {
        desc {
            id = 7210
            name = "Bash"
            description = "A powerful physical attack that stuns the target"
        }

        targetTypes = of(ENEMY)

        manaCost = 40
        cooldown = 15.0

        onCastScript = { caster, target, skill ->
            val dmg = (1 + (15 + 5*skill.level) / 100.0) * caster.getTotal(ATK)
            val result = caster.characterComponent.dealPhysicalDamage(target, dmg)

            target.addEffect(effect(description) {
                status = STUNNED
                duration = 5.0
            })
        }
    }

    val ENDURANCE = activeSkill {
        desc {
            id = 7211
            name = "Endurance"
            description = "Takes less damage and regenerates HP faster for the duration."
        }

        manaCost = 35
        cooldown = 25.0

        targetTypes = of(SELF)

        onCastScript = { caster, target, skill ->

            target.addEffect(effect(description) {
                duration = 15.0

                ARM +3*skill.level
                DEF +1*skill.level
                HP_REGEN +2*skill.level
            })
        }
    }

    val DOUBLE_EDGE = activeSkill {
        desc {
            id = 7212
            name = "Double Edge"
            description = "Sacrifice % of HP to deal double that damage to target. Damage is pure."
        }

        manaCost = 45
        cooldown = 35.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            // float dmg = (0.1f + 0.02f * level) * caster.getHP();
//                    caster.setHP(Math.round(caster.getHP() - dmg));
//                    caster.dealPureDamage(target, 2*dmg);
//
//                    useResult = new SkillUseResult(2*dmg + ",PURE");
        }
    }

    val BLOODLUST = activeSkill {
        desc {
            id = 7213
            name = "Bloodlust"
            description = "Increases ATK based on the missing % HP for the duration."
        }

        manaCost = 35
        cooldown = 25.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            caster.addEffect(effect(description) {
                val value = (10*skill.level * caster.getTotal(MAX_HP) / (caster.hp.value + 1)).toInt()

                duration = 10.0

                ATK +value
            })
        }
    }

    val SHATTER_ARMOR = activeSkill {
        desc {
            id = 7214
            name = "Shatter Armor"
            description = "Decreases target's ARM for the duration."
        }

        manaCost = 45
        cooldown = 15.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            target.addEffect(effect(description) {
                ARM +-(3*skill.level)
            })
        }
    }
}

class Mage {

    val FIREBALL = activeSkill {
        desc {
            id = 7020
            name = "Fireball"
            description = "Deals magic damage with FIRE element."
        }

        targetTypes = of(ENEMY)

        manaCost = 1
        cooldown = 1.0

        projectileTextureName = "projectiles/fireball.png"

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) + skill.level * 20.0

            caster.characterComponent.dealMagicalDamage(target, dmg, FIRE)
        }
    }

    val ICE_SHARD = activeSkill {
        desc {
            id = 7021
            name = "Ice Shard"
            description = "Deals magic damage with WATER element."
        }

        targetTypes = of(ENEMY)

        manaCost = 30
        cooldown = 11.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) * 2 + skill.level * 10.0
            val result = caster.characterComponent.dealMagicalDamage(target, dmg, WATER)
        }
    }

    val AIR_SPEAR = activeSkill {
        desc {
            id = 7022
            name = "Air Spear"
            description = "Deals magic damage with AIR element."
        }

        targetTypes = of(ENEMY)

        manaCost = 25
        cooldown = 12.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) * 1.5 + skill.level * 8.0
            val result = caster.characterComponent.dealMagicalDamage(target, dmg, AIR)
        }
    }

    val EARTH_BOULDER = activeSkill {
        desc {
            id = 7023
            name = "Earth Boulder"
            description = "Deals magic damage with EARTH element."
        }

        targetTypes = of(ENEMY)

        manaCost = 25
        cooldown = 7.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) + skill.level * 25.0
            val result = caster.characterComponent.dealMagicalDamage(target, dmg, EARTH)
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

    val AMPLIFY_MAGIC = activeSkill {
        desc {
            id = 7121
            name = "Amplify Magic"
            description = "Increases MATK for the duration."
        }

        targetTypes = of(SELF)

        manaCost = 25
        cooldown = 10.0

        onCastScript = { caster, target, skill ->
            caster.addEffect(effect(description) {

                duration = 10.0

                MATK +10*skill.level
            })
        }
    }

    val MENTAL_STRIKE = activeSkill {
        desc {
            id = 7122
            name = "Mental Strike"
            description = "Deals pure damage based on MATK."
        }

        targetTypes = of(ENEMY)

        manaCost = 40
        cooldown = 10.0

        onCastScript = { caster, target, skill ->

            // TODO: deal pure damage based on MATK

            //                float dmg = caster.getTotalStat(Stat.MATK) * (1 + level*0.1f);
            //                caster.dealPureDamage(target, dmg);
        }
    }

    val THUNDERBOLT_FIRESTORM = activeSkill {
        desc {
            id = 7123
            name = "Thunderbolt Firestorm"
            description = "Deals magic damage with AIR and FIRE element."
        }

        targetTypes = of(ENEMY)

        manaCost = 50
        cooldown = 20.0

        onCastScript = { caster, target, skill ->

            // TODO:
            //                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
            //                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.FIRE);
            //                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.AIR);
            //
            //                useResult = new SkillUseResult(dmg1 + "," + dmg2);
        }
    }

    val ICICLE_AVALANCHE = activeSkill {
        desc {
            id = 7124
            name = "Icicle Avalanche"
            description = "Deals magic damage with WATER and EARTH element."
        }

        targetTypes = of(ENEMY)

        manaCost = 50
        cooldown = 20.0

        onCastScript = { caster, target, skill ->

            // TODO:
            //                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
            //                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.WATER);
            //                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.EARTH);
            //
            //                useResult = new SkillUseResult(dmg1 + "," + dmg2);
        }
    }
}

class Enchanter {

    val MAGIC_SHIELD = activeSkill {
        desc {
            id = 7220
            name = "Magic Shield"
            description = "Increases ARM for the duration based on WIL."
        }

        manaCost = 55
        cooldown = 45.0

        targetTypes = of(SELF)

        onCastScript = { caster, target, skill ->

            caster.addEffect(effect(description) {
                duration = 30.0

                // TODO:
            })
        }
    }

    val ASTRAL_PROTECTION = passiveSkill {
        desc {
            id = 7221
            name = "Astral Protection"
            description = "Passively increases MDEF."
        }

        MDEF +2
    }

    val MIND_BLAST = activeSkill {
        desc {
            id = 7222
            name = "Mind Blast"
            description = "Drains % of target's SP. Decreases target's MDEF for the duration."
        }

        manaCost = 55
        cooldown = 45.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            caster.addEffect(effect(description) {
                duration = 30.0

                // TODO:
            })
        }
    }

    val CURSE_OF_KNOWLEDGE = activeSkill {
        desc {
            id = 7223
            name = "Curse of Knowledge"
            description = "Deals damage based on target's INT, WIS and WIL."
        }

        manaCost = 65
        cooldown = 25.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            // TODO:
        }
    }

    val MANA_BURN = activeSkill {
        desc {
            id = 7224
            name = "Mana Burn"
            description = "Burns target's SP and deals damage based on the SP burnt."
        }

        manaCost = 65
        cooldown = 10.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->

            // TODO:
            //                int oldSP = target.getSP();
            //                target.setSP(Math.max(oldSP - 50 * level, 0));
            //                int dmg = caster.dealMagicalDamage(target, oldSP-target.getSP(), Element.NEUTRAL);
            //
            //                useResult = new SkillUseResult(dmg);
        }
    }
}

class Scout {
    val TRICK_ATTACK = activeSkill {
        desc {
            id = 7030
            name = "Trick Attack"
            description = "Deals physical damage and steals gold equal to damage dealt."
        }

        targetTypes = of(ENEMY)

        manaCost = 1
        cooldown = 1.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(ATK) + skill.level * 2 * GameMath.random(2)

            caster.playerComponent?.rewardMoney(dmg)

            caster.dealPhysicalDamage(target, dmg.toDouble())
        }
    }

    val POISON_ATTACK = activeSkill {
        desc {
            id = 7031
            name = "Poison Attack"
            description = "Attacks the target with high chance to poison it."
        }

        targetTypes = of(ENEMY)

        manaCost = 25
        cooldown = 6.0

        onCastScript = { caster, target, skill ->
            runIfChance(skill.level * 7) {
                // TODO: apply poison to target
            }

            val dmg = caster.getTotal(ATK) + 2 * skill.level

            caster.dealPhysicalDamage(target, dmg.toDouble())
        }
    }

    val WEAPON_MASTERY = passiveSkill {
        desc {
            id = 7032
            name = "Weapon Mastery"
            description = "Passively increases ATK."
        }

        ATK +3
    }

    val EXPERIENCED_FIGHTER = passiveSkill {
        desc {
            id = 7033
            name = "Experienced Fighter"
            description = "Passively increases AGI and DEX."
        }

        AGILITY +2
        DEXTERITY +2
    }
}

class Rogue {
    val SHAMELESS = activeSkill {
        desc {
            id = 7130
            name = "Shameless"
            description = "Deals more damage if target's % HP is lower than yours. No cooldown but consumes a large portion of mana."
        }

        manaCost = 50
        cooldown = 0.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            //                float dmg = caster.getTotalStat(Stat.ATK);
            //                float casterHPFactor = caster.getHP() / caster.getTotalStat(Stat.MAX_HP);
            //                float targetHPFactor = target.getHP() / target.getTotalStat(Stat.MAX_HP);
            //                if (casterHPFactor > targetHPFactor) {
            //                    dmg += level * 0.1f * (casterHPFactor - targetHPFactor) * dmg;
            //                }
            //
            //                int d = caster.dealPhysicalDamage(target, dmg);
            //                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
        }
    }

    val DOUBLE_STRIKE = activeSkill {
        desc {
            id = 7131
            name = "Double Strike"
            description = "Quickly performs two attacks with a chance to stun the target."
        }

        manaCost = 25
        cooldown = 13.0

        targetTypes = of(ENEMY)

        //                int dmg1 = caster.attack(target);
        //                int dmg2 = caster.attack(target);
        //                boolean stun = false;
        //                if (GameMath.checkChance(level*5)) {
        //                    target.addStatusEffect(new StatusEffect(Status.STUNNED, 2.5f));
        //                    stun = true;
        //                }
        //
        //                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg1) + "," + GameMath.normalizeDamage(dmg2)
        //                        + (stun ? ",STUNNED" : ",X2"));
    }

    val TRIPLE_STRIKE = activeSkill {
        desc {
            id = 7132
            name = "Triple Strike"
            description = "Quickly performs three attacks. Deals more damage if the target is stunned."
        }

        manaCost = 35
        cooldown = 25.0

        targetTypes = of(ENEMY)

        //                float dmg = caster.getTotalStat(Stat.ATK);
        //                if (target.hasStatusEffect(Status.STUNNED)) {
        //                    dmg += level * 15;
        //                }
        //
        //                int dmg1 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
        //                int dmg2 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
        //                int dmg3 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
        //
        //                useResult = new SkillUseResult(dmg1 + "," + dmg2 + "," + dmg3 + ",X3");
    }

    val CRITICAL_STRIKE = activeSkill {
        desc {
            id = 7133
            name = "Critical Strike"
            description = "Strikes the target with high chance of crit. Crit damage is greater for this skill."
        }

        manaCost = 30
        cooldown = 15.0

        targetTypes = of(ENEMY)

        //                float dmg = caster.getTotalStat(Stat.ATK) + 15 + 5 * level;
        //                caster.addBonusStat(Stat.CRIT_CHANCE, 50 + level * 3);
        //                int d = caster.dealPhysicalDamage(target, dmg);
        //                caster.addBonusStat(Stat.CRIT_CHANCE, -(50 + level * 3));
        //
        //                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    }

    val PIERCING_STRIKE = activeSkill {
        desc {
            id = 7134
            name = "Piercing Strike"
            description = "Deals devastating damage to targets with low ARM."
        }

        manaCost = 35
        cooldown = 7.0

        targetTypes = of(ENEMY)

        //                float dmg = 20 + level*30 - target.getTotalStat(Stat.ARM);
        //                int d = caster.dealPhysicalDamage(target, dmg);
        //
        //                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    }
}

class Ranger {

    val PINPOINT_WEAKNESS = activeSkill {
        desc {
            id = 7230
            name = "Pinpoint Weakness"
            description = "Decreases target's DEF for the duration."
        }

        manaCost = 25
        cooldown = 15.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            target.addEffect(effect(description) {
                duration = 10.0

                DEF +(-3*skill.level)
            })
        }
    }

    val BULLSEYE = activeSkill {
        desc {
            id = 7231
            name = "Bullseye"
            description = "Deals armor ignoring damage to target. Target's defense is not ignored. Damage is based on caster's DEX."
        }

        manaCost = 25
        cooldown = 15.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            // TODO:
            //                float dmg = 100 + 0.2f*level * caster.getTotalAttribute(Attribute.DEXTERITY) - target.getTotalStat(GameCharacter.DEF);
            //                caster.dealPureDamage(target, dmg);
            //
            //                useResult = new SkillUseResult(dmg + ",PURE");
        }
    }

    val FAST_REFLEXES = activeSkill {
        desc {
            id = 7232
            name = "Fast Reflexes"
            description = "Increases AGI and DEX for the duration."
        }

        manaCost = 45
        cooldown = 20.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            target.addEffect(effect(description) {
                duration = 20.0

                AGILITY +2*skill.level
                DEXTERITY +2*skill.level
            })
        }
    }

    val ENCHANTED_ARROW = activeSkill {
        desc {
            id = 7233
            name = "Enchanted Arrow"
            description = "Stuns target. Stun lasts longer for targets with high armor rating."
        }

        manaCost = 35
        cooldown = 11.0

        targetTypes = of(ENEMY)

        onCastScript = { caster, target, skill ->
            // TODO:
            //                float duration = target.getTotalStat(Stat.ARM) * 0.1f;
            //                target.addStatusEffect(new StatusEffect(Status.STUNNED, duration));
            //
            //                useResult = new SkillUseResult("STUNNED");
        }
    }

    val EAGLE_EYE = passiveSkill {
        desc {
            id = 7234
            name = "Eagle Eye"
            description = "Passively increases ATK based on DEX."
        }

        // TODO: value = (int)(caster.getTotalAttribute(Attribute.DEXTERITY) * level * 0.1f);
    }
}