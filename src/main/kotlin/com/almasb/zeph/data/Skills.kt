package com.almasb.zeph.data

import com.almasb.zeph.combat.*
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element.*
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.combat.Status.*
import com.almasb.zeph.skill.SkillTargetType.*
import com.almasb.zeph.skill.SkillType.ACTIVE
import com.almasb.zeph.skill.SkillType.PASSIVE
import com.almasb.zeph.skill.SkillUseType.*
import com.almasb.zeph.skill.passiveSkill
import com.almasb.zeph.skill.skill
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
    // TODO: rename activeSkill
    val ROAR = skill {
        desc {
            id = 7010
            name = "Roar"
            description = "Increases STR and VIT for the duration."
        }

        type = ACTIVE
        useType = EFFECT
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

    val MIGHTY_SWING = skill {
        desc {
            id = 7011
            name = "Mighty Swing"
            description = "Physical attack. Damage is greater if you have more STR than your target."
        }

        manaCost = 25
        cooldown = 15.0

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

    val HOLY_LIGHT = skill {
        desc {
            id = 7110
            name = "Holy Light"
            description = "Heals and increases VIT for the duration."
        }

        type = ACTIVE
        useType = RESTORE
        targetTypes = of(ALLY)

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

    val PRECISION_STRIKE = skill {
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
        }
    }

}

class Gladiator {

    val BASH = skill {
        desc {
            id = 7210
            name = "Bash"
            description = "A powerful physical attack that stuns the target"
        }

        type = ACTIVE
        useType = DAMAGE
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

    //        addSkill(new Skill(ID.Skill.Gladiator.SHATTER_ARMOR, "Shatter Armor", Desc.Skill.Gladiator.SHATTER_ARMOR, true, 30.0f) {
    //            /**
    //             *
    //             */
    //            private static final long serialVersionUID = -4834599835655165707L;
    //
    //            @Override
    //            public int getManaCost() {
    //                return 2 + level*5;
    //            }
    //
    //            @Override
    //            protected void useImpl(GameCharacter caster, GameCharacter target) {
    //                target.addEffect(new Effect((20.0f), ID.Skill.Gladiator.SHATTER_ARMOR,
    //                        new Rune[] {
    //                }, new Essence[] {
    //                        new Essence(Stat.ARM, -2*level)
    //                }
    //                        ));
    //
    //                useResult = new SkillUseResult("ARM -" + 2*level);
    //            }
    //        });

//    ////        addSkill(new Skill(ID.Skill.Gladiator.BLOODLUST, "Bloodlust", Desc.Skill.Gladiator.BLOODLUST, false, 0.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 5844145407908548491L;
//    ////
//    ////            private int value = 0;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 0;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                caster.addBonusStat(Stat.ATK, -value);
//    ////                // div 0 shouldn't occur
//    ////                value = (int) (10*level * caster.getTotalStat(Stat.MAX_HP) / (caster.getHP() + 1));
//    ////                caster.addBonusStat(Stat.ATK, value);
//    ////            }
//    ////        });
//    ////

//    ////
//
//    ////
//    ////        addSkill(new Skill(ID.Skill.Gladiator.DOUBLE_EDGE, "Double Edge", Desc.Skill.Gladiator.DOUBLE_EDGE, true, 0.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -5670132035647752285L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 0;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = (0.1f + 0.02f * level) * caster.getHP();
//    ////                caster.setHP(Math.round(caster.getHP() - dmg));
//    ////                caster.dealPureDamage(target, 2*dmg);
//    ////
//    ////                useResult = new SkillUseResult(2*dmg + ",PURE");
//    ////            }
//    ////        });
//    ////
//

    //            fun ENDURANCE() = listOf<Component>(
//                    Description(7211, "Endurance", "Takes less damage and regenerates HP faster for the duration.", "skills/ic_skill_bash.png"),
//                    SkillDataComponent(SkillType.ACTIVE, SkillUseType.DAMAGE, EnumSet.of(SkillTargetType.ENEMY))
//                            .withMana(35)
//                            .withCooldown(15.0)
//            )
//
//            fun DOUBLE_EDGE() = listOf<Component>(
//                    Description(7212, "Double Edge", "Sacrifice % of HP to deal double that damage to target. Damage is pure.", "skills/ic_skill_bash.png"),
//                    SkillDataComponent(SkillType.ACTIVE, SkillUseType.DAMAGE, EnumSet.of(SkillTargetType.ENEMY))
//                            .withMana(35)
//                            .withCooldown(15.0)
//            )
//
//            fun BLOODLUST() = listOf<Component>(
//                    Description(7213, "Bloodlust", "Increases ATK based on the missing % HP.", "skills/ic_skill_bash.png"),
//                    SkillDataComponent(SkillType.ACTIVE, SkillUseType.DAMAGE, EnumSet.of(SkillTargetType.ENEMY))
//                            .withMana(35)
//                            .withCooldown(15.0)
//            )
//
//            fun SHATTER_ARMOR() = listOf<Component>(
//                    Description(7214, "Shatter Armor", "Decreases target's armor for the duration.", "skills/ic_skill_bash.png"),
//                    SkillDataComponent(SkillType.ACTIVE, SkillUseType.DAMAGE, EnumSet.of(SkillTargetType.ENEMY))
//                            .withMana(35)
//                            .withCooldown(15.0)
//            )
}



//    ////
//    ////        addSkill(new Skill(ID.Skill.Crusader.LAST_STAND, "Last Stand", Desc.Skill.Crusader.LAST_STAND, true, 60.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -8176078084748576113L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 2 + level*5;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                caster.addEffect(new Effect((20.0f), ID.Skill.Crusader.LAST_STAND,
//    ////                        new Rune[] {
//    ////                }, new Essence[] {
//    ////                        new Essence(Stat.ATK, Math.round(caster.getBaseStat(Stat.ATK)))
//    ////                }
//    ////                        ));
//    ////
//    ////                useResult = new SkillUseResult("ATK UP!");
//    ////            }
//    ////
//    ////            @Override
//    ////            public boolean isSelfTarget() {
//    ////                return true;
//    ////            }
//    ////        });
//    ////

class Mage {

    val FIREBALL = skill {
        desc {
            id = 7020
            name = "Fireball"
            description = "Deals magic damage with FIRE element."
        }

        useType = DAMAGE
        targetTypes = of(ENEMY)

        manaCost = 1
        cooldown = 1.0

        projectileTextureName = "projectiles/fireball.png"

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) + skill.level * 20.0

            caster.characterComponent.dealMagicalDamage(target, dmg, FIRE)
        }
    }

    val ICE_SHARD = skill {
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

    val AIR_SPEAR = skill {
        desc {
            id = 7022
            name = "Air Spear"
            description = "Deals magic damage with AIR element."
        }

        type = ACTIVE
        useType = DAMAGE
        targetTypes = of(ENEMY)

        manaCost = 25
        cooldown = 12.0

        onCastScript = { caster, target, skill ->
            val dmg = caster.getTotal(MATK) * 1.5 + skill.level * 8.0
            val result = caster.characterComponent.dealMagicalDamage(target, dmg, AIR)
        }
    }

    val EARTH_BOULDER = skill {
        desc {
            id = 7023
            name = "Earth Boulder"
            description = "Deals magic damage with EARTH element."
        }

        type = ACTIVE
        useType = DAMAGE
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

    val AMPLIFY_MAGIC = skill {
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

    val MENTAL_STRIKE = skill {
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
        }
    }

    val THUNDERBOLT_FIRESTORM = skill {
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

    val ICICLE_AVALANCHE = skill {
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
    //        addSkill(new Skill(ID.Skill.Enchanter.ASTRAL_PROTECTION, "Astral Protection", Desc.Skill.Enchanter.ASTRAL_PROTECTION, false, 0.0f) {
    //            /**
    //             *
    //             */
    //            private static final long serialVersionUID = 8691650266711866295L;
    //
    //            private int value = 0;
    //
    //            @Override
    //            public int getManaCost() {
    //                return 0;
    //            }
    //
    //            @Override
    //            protected void useImpl(GameCharacter caster, GameCharacter target) {
    //                caster.addBonusStat(Stat.MDEF, -value);
    //                value = level * 2;
    //                caster.addBonusStat(Stat.MDEF, value);
    //            }
    //        });
}

class Scout {
    val TRICK_ATTACK = skill {
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

    val POISON_ATTACK = skill {
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

}

class Ranger {

    val EAGLE_EYE = passiveSkill {
        desc {
            id = 7234
            name = "Eagle Eye"
            description = "Passively increases ATK based on DEX."
        }

        // TODO: value = (int)(caster.getTotalAttribute(Attribute.DEXTERITY) * level * 0.1f);

    }

}