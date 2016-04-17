package com.almasb.zeph.entity

import com.almasb.ents.Component
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.entity.item.ArmorType
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.WeaponType
import com.almasb.zeph.entity.item.component.ArmorDataComponent
import com.almasb.zeph.entity.item.component.WeaponDataComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    object Weapon {
        fun HANDS() = listOf<Component>(
                DescriptionComponent(4000, "Hands", "That's right, go kill everyone with your bare hands", "items/weapons/hands.png"),
                WeaponDataComponent(ItemLevel.NORMAL, WeaponType.MACE, 0)
        )

        fun GUT_RIPPER() = listOf<Component>(
                DescriptionComponent(4003, "The Gut Ripper", "A fierce weapon that punctures and ruptures enemies with vicious and lightning fast blows", "items/weapons/gut_ripper.png"),
                WeaponDataComponent(ItemLevel.EPIC, WeaponType.DAGGER, 100)
                        .withRune(Rune(Attribute.AGILITY, 4))
                        .withRune(Rune(Attribute.DEXTERITY, 4))
                        .withRune(Rune(Attribute.LUCK, 1))
        )
    }

    object Armor {
        fun HAT() = listOf<Component>(
                DescriptionComponent(5000, "Hat", "Ordinary hat, already out of fashion", "items/armor/hat.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.HELM, 0, 0)
        )

        fun CLOTHES() = listOf<Component>(
                DescriptionComponent(5001, "Clothes", "Just normal clothes, don't count on any defense", "items/armor/clothes.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.BODY, 0, 0)
        )

        fun SHOES() = listOf<Component>(
                DescriptionComponent(5002, "Shoes", "Average size shoes", "items/armor/shoes.png"),
                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.SHOES, 0, 0)
        )

        fun DOMOVOI() = listOf<Component>(
                DescriptionComponent(5005, "Domovoi", "Generations of guardians have bled in this armour, imbuing it with spirits of protection. Spirits that awaken when the wearers need is greatest.", "items/armor/domovoi.png"),
                ArmorDataComponent(ItemLevel.UNIQUE, ArmorType.BODY, 15, 35)
                        .withRune(Rune(Attribute.WILLPOWER, 3))
        )
    }

    object Character {

    }

    // ID
    //        public static final int HAT = 5000;
    //        public static final int CLOTHES = 5001;
    //        public static final int SHOES = 5002;
    //        public static final int CHAINMAL = 5003;
    //        public static final int SOUL_BARRIER = 5004;
    //        public static final int DOMOVOI = 5005;
    //        public static final int SAPPHIRE_LEGION_PLATE_MAIL = 5006;
    //        public static final int THANATOS_BODY_ARMOR = 5007;
    //
    //    public class Enemy {
    //        public static final int MINOR_FIRE_SPIRIT = 2000;
    //        public static final int MINOR_EARTH_SPIRIT = 2001;
    //        public static final int MINOR_WATER_SPIRIT = 2002;
    //        public static final int MINOR_WIND_SPIRIT = 2003;
    //    }
    //
    //    public class Essence {
    //
    //    }
    //
    //    public class Skill {
    //
    //        public class Novice {
    //
    //        }
    //
    //        public class Warrior {
    //            public static final int MIGHTY_SWING = 7010;
    //            public static final int ROAR = 7011;
    //            public static final int WARRIOR_HEART = 7012;
    //            public static final int ARMOR_MASTERY = 7013;
    //        }
    //
    //        public class Crusader {
    //            public static final int HOLY_LIGHT = 7110;
    //            public static final int FAITH = 7111;
    //            public static final int DIVINE_ARMOR = 7112;
    //            public static final int PRECISION_STRIKE = 7113;
    //            public static final int LAST_STAND = 7114;
    //        }
    //
    //        public class Gladiator {
    //            public static final int BASH = 7210;
    //            public static final int ENDURANCE = 7211;
    //            public static final int DOUBLE_EDGE = 7212;
    //            public static final int BLOODLUST = 7213;
    //            public static final int SHATTER_ARMOR = 7214;
    //        }
    //
    //        public class Mage {
    //            public static final int FIREBALL = 7020;
    //            public static final int ICE_SHARD = 7021;
    //            public static final int AIR_SPEAR = 7022;
    //            public static final int EARTH_BOULDER = 7023;
    //        }
    //
    //        public class Wizard {
    //            public static final int MAGIC_MASTERY = 7120;
    //            public static final int AMPLIFY_MAGIC = 7121;
    //            public static final int MENTAL_STRIKE = 7122;
    //            public static final int THUNDERBOLT_FIRESTORM = 7123;
    //            public static final int ICICLE_AVALANCHE = 7124;
    //        }
    //
    //        public class Enchanter {
    //            public static final int MAGIC_SHIELD = 7220;
    //            public static final int ASTRAL_PROTECTION = 7221;
    //            public static final int MIND_BLAST = 7222;
    //            public static final int CURSE_OF_WITCHCRAFT = 7223;
    //            public static final int MANA_BURN = 7224;
    //        }
    //
    //        public class Scout {
    //            public static final int TRICK_ATTACK = 7030;
    //            public static final int POISON_ATTACK = 7031;
    //            public static final int WEAPON_MASTERY = 7032;
    //            public static final int EXPERIENCED_FIGHTER = 7033;
    //        }
    //
    //        public class Rogue {
    //            public static final int SHAMELESS = 7130;
    //            public static final int DOUBLE_STRIKE = 7131;
    //            public static final int TRIPLE_STRIKE = 7132;
    //            public static final int FIVE_FINGER_DEATH_PUNCH = 7133;
    //            public static final int CRITICAL_STRIKE = 7134;
    //        }
    //
    //        public class Ranger {
    //            public static final int PINPOINT_WEAKNESS = 7230;
    //            public static final int BULLSEYE = 7231;
    //            public static final int FAST_REFLEXES = 7232;
    //            public static final int ENCHANTED_ARROW = 7233;
    //            public static final int EAGLE_EYE = 7234;
    //        }
    //    }
    //
    //    public class Weapon {
    //        public static final int HANDS = 4000;
    //        public static final int GETSUGA_TENSHO = 4001;
    //        public static final int SOUL_REAPER = 4002;
    //        public static final int GUT_RIPPER = 4003;
    //        public static final int DRAGON_CLAW = 4004;
    //        public static final int FROSTMOURN = 4005;
    //
    //
    //        public static final int IRON_SWORD = 4006;
    //        public static final int KNIFE = 4007;
    //
    //        public static final int PRACTICE_SWORD = 4010;
    //        public static final int CLAYMORE = 4011;
    //        public static final int BROADSWORD = 4012;
    //        public static final int HALLSTATT_SWORD = 4013;
    //        public static final int KAMPILAN_SWORD = 4014;
    //        public static final int MACHETE = 4015;
    //        public static final int TEGA_SWORD = 4016;
    //        public static final int BATTLESWORD = 4017;
    //        public static final int LONGSWORD = 4018;
    //        public static final int SCHIAVONA_SWORD = 4019;
    //        public static final int COLICHERMARDE_SWORD = 4020;
    //
    //    }


    // DESC

    //  public class Armor {
        //        public static final String HAT = "Ordinary hat, already out of fashion";
        //        public static final String CLOTHES = "Just normal clothes, don't count on any defense";
        //        public static final String SHOES = "Average size shoes";
        //        public static final String CHAINMAL = "Armour consisting of small metal rings linked together in a pattern to form a mesh.";
        //        public static final String SOUL_BARRIER = "Protects its wearer from magic attacks";
        //        public static final String DOMOVOI = "Generations of guardians have bled in this armour, imbuing it with spirits of protection. Spirits that awaken when the wearers need is greatest.";
        //        public static final String SAPPHIRE_LEGION_PLATE_MAIL = "Produced in the Jaded Forges of the Jewelled King, strictly for use by warriors who have proved their mastery of combat through decades of service.";
        //        public static final String THANATOS_BODY_ARMOR = "A shattered piece of Thanatos' legendary armor. Grants its user great constitution";
        //    }
        //
        //    public class Enemy {
        //        public static final String MINOR_FIRE_SPIRIT = "Fire Spirit desc";
        //        public static final String MINOR_EARTH_SPIRIT = "Earth Spirit desc";
        //        public static final String MINOR_WATER_SPIRIT = "Water Spirit desc";
        //        public static final String MINOR_WIND_SPIRIT = "Wind Spirit desc";
        //    }
        //
        //    public class Essence {
        //
        //    }
        //
        //    public class Skill {
        //
        //        public class Warrior {
        //            public static final String MIGHTY_SWING = "Physical attack. Damage is greater if you have more STR than your target";
        //            public static final String ROAR = "Increases STR and VIT for the duration";
        //            public static final String WARRIOR_HEART = "Passively increases max HP";
        //            public static final String ARMOR_MASTERY = "Increases armor rating";
        //        }
        //
        //        public class Crusader {
        //            public static final String HOLY_LIGHT = "Heals and increases VIT for the duration";
        //            public static final String FAITH = "Further increases bonus given by Heart of a Warrior skill";
        //            public static final String DIVINE_ARMOR = "Further increases bonus given by Armor Mastery skill";
        //            public static final String PRECISION_STRIKE = "Deals armor ignoring damage based on STR";
        //            public static final String LAST_STAND = "Deals double base damage for the duration";
        //        }
        //
        //        public class Gladiator {
        //            public static final String BLOODLUST = "Increases ATK based on the missing % HP";
        //            public static final String BASH = "A powerful physical attack that stuns the target for 5 seconds";
        //            public static final String SHATTER_ARMOR = "Decreases target's armor for the duration";
        //            public static final String DOUBLE_EDGE = "Sacrifice % of HP to deal double that damage to target. Damage is pure";
        //            public static final String ENDURANCE = "Takes less damage and regenerates HP faster for the duration";
        //        }
        //
        //        public class Mage {
        //            public static final String FIREBALL = "Deals magic damage with fire element";
        //            public static final String ICE_SHARD = "Deals magic damage with water element";
        //            public static final String AIR_SPEAR = "Deals magic damage with air element";
        //            public static final String EARTH_BOULDER = "Deals magic damage with earth element";
        //        }
        //
        //        public class Wizard {
        //            public static final String MENTAL_STRIKE = "Deals pure damage based on MATK";
        //            public static final String AMPLIFY_MAGIC = "Increases MATK for the duration";
        //            public static final String MAGIC_MASTERY = "Passively increases INT and WIL";
        //            public static final String THUNDERBOLT_FIRESTORM = "Deals magic damage with air and fire element";
        //            public static final String ICICLE_AVALANCHE = "Deals magic damage with water and earth element";
        //        }
        //
        //        public class Enchanter {
        //            public static final String MAGIC_SHIELD = "Increases Armor rating for the duration";
        //            public static final String ASTRAL_PROTECTION = "Passively increases MDEF";
        //            public static final String MIND_BLAST = "Drains % of target's SP. Increases mana cost of all target's skills";
        //            public static final String CURSE_OF_WITCHCRAFT = "Target cannot use skills for the duration";
        //            public static final String MANA_BURN = "Burns target's SP and deals damage based on the SP burnt";
        //        }
        //
        //        public class Scout {
        //            public static final String POISON_ATTACK = "Attacks the target with high chance to poison him";
        //            public static final String TRICK_ATTACK = "Deals physical damage and steals gold equal to damage dealt";
        //            public static final String WEAPON_MASTERY = "Passively increases ATK";
        //            public static final String EXPERIENCED_FIGHTER = "Passively increases AGI and DEX";
        //        }
        //
        //        public class Rogue {
        //            public static final String SHAMELESS = "Deals more damage if target's % HP is lower than yours. No cooldown but consumes mana";
        //            public static final String DOUBLE_STRIKE = "Quickly performs two attacks with a chance to stun the target";
        //            public static final String TRIPLE_STRIKE = "Quickly performs three attacks. Deals more damage if target is stunned";
        //            public static final String CRITICAL_STRIKE = "Strikes the target with high chance of crit. Crit damage is greater for this skill";
        //            public static final String FIVE_FINGER_DEATH_PUNCH = "Deals devastating damage to unarmoured targets";
        //        }
        //
        //        public class Ranger {
        //            public static final String FAST_REFLEXES = "Increases ASPD for the duration";
        //            public static final String ENCHANTED_ARROW = "Stuns target. Stun lasts longer for target's with high armor rating";
        //            public static final String EAGLE_EYE = "Passively increases ATK based on DEX";
        //            public static final String PINPOINT_WEAKNESS = "Decreases target's defense for the duration";
        //            public static final String BULLSEYE = "Deals armor ignoring damage to target."
        //                    + "Target's defense is not ignored. "
        //                    + "Damage is based on caster's DEX";
        //        }
        //    }
        //
        //    public class Weapon {
        //        public static final String HANDS = "That's right, go kill everyone with your bare hands";
        //        public static final String GETSUGA_TENSHO = "A powerful sword that is carved from the fangs of the moon itself and pierced through heaven";
        //        public static final String SOUL_REAPER = "Forged in the dephts of Aesmir, it is said the weilder can feel the weapon crave the souls of its enemies";
        //        public static final String GUT_RIPPER = "A fierce weapon that punctures and ruptures enemies with vicious and lightning fast blows";
        //        public static final String DRAGON_CLAW = "A mythical bow made of claws of the legendary dragon. Contains dragon's wisdom and loyal to only one master throughout his whole life. Grants dragon's and earlier owner's wisdom and knowledge to the new master";
        //        public static final String FROSTMOURN = "The legendary sword of the Ice Dungeon's King. Can turn enemies into frozen rocks with 5% chance. Has water element";
        //        public static final String IRON_SWORD = "A standard warrior's sword with decent attack damage";
        //        public static final String KNIFE = "A simple knife with poor blade";
        //
        //        public static final String PRACTICE_SWORD = "A basic one-handed sword";
        //        public static final String CLAYMORE = "Large, double-edged broad sword that was used by the Scottish highlanders";
        //        public static final String BROADSWORD = "A sword with a wide, double sided blade";
        //        public static final String HALLSTATT_SWORD = "A sword favored by gladiators, it is especially designed for battles against armored enemies";
        //        public static final String KAMPILAN_SWORD = "A thin sword designed to be easily bent, light, and very elastic";
        //        public static final String MACHETE = "A strong cleaver-like sword";
        //        public static final String TEGA_SWORD = "A ceremonial sword used by gravekeeper's to lead the dead to the great beyond";
        //        public static final String BATTLESWORD = "A terrifying two-handed sword that is said to stimulate the nerves in order to kill, once it's in the wearer's hands";
        //        public static final String LONGSWORD = "A two-handed sword with straight double-edged blade";
        //        public static final String SCHIAVONA_SWORD = "A popular sword among mercenary soldiers";
        //        public static final String COLICHERMARDE_SWORD = "Small sword with good parrying characteristics";
        //    }



    // MISC DATA

    //
    //    private static HashMap<Integer, Weapon> defaultWeapons = new HashMap<>();
    //    private static HashMap<Integer, Armor> defaultArmor = new HashMap<>();
    //    private static HashMap<Integer, Skill> defaultSkills = new HashMap<>();
    //    private static HashMap<Integer, EnemyControl> defaultEnemies = new HashMap<>();
    //    private static HashMap<Integer, Essence> defaultEssences = new HashMap<>();
    //
    //    private EntityManagerOld() {}
    //
    //    private static void loadArmor() {
    //        ArmorBuilder armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.CLOTHES)
    //                    .name("Clothes")
    //                    .description(Desc.Armor.CLOTHES)
    //                    .textureName("clothes.png")
    //                    .type(ArmorType.BODY);
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.SHOES)
    //                    .name("Shoes")
    //                    .description(Desc.Armor.SHOES)
    //                    .textureName("shoes.png")
    //                    .type(ArmorType.SHOES);
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.HAT)
    //                    .name("Hat")
    //                    .description(Desc.Armor.HAT)
    //                    .textureName("hat.png")
    //                    .type(ArmorType.HELM);
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.CHAINMAL)
    //                    .name("Chainmail")
    //                    .description(Desc.Armor.CHAINMAL)
    //                    .textureName("chainmail.png")
    //                    .type(ArmorType.BODY)
    //                    .armor(10)
    //                    .marmor(5)
    //                    .runes(new Rune(Attribute.STRENGTH, 2));
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.THANATOS_BODY_ARMOR)
    //                    .name("Thanatos Body Armor")
    //                    .description(Desc.Armor.THANATOS_BODY_ARMOR)
    //                    .textureName("thanatos_body_armor.png")
    //                    .type(ArmorType.BODY)
    //                    .armor(50)
    //                    .marmor(25)
    //                    .itemLevel(ItemLevel.EPIC)
    //                    .element(Element.EARTH)
    //                    .runes(new Rune(Attribute.VITALITY, 5), new Rune(Attribute.PERCEPTION, 4));
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL)
    //                    .name("Sapphire Legion Plate Mail")
    //                    .description(Desc.Armor.SAPPHIRE_LEGION_PLATE_MAIL)
    //                    .textureName("sapphire_legion_plate.png")
    //                    .type(ArmorType.BODY)
    //                    .armor(30)
    //                    .marmor(10)
    //                    .itemLevel(ItemLevel.UNIQUE)
    //                    .runes(new Rune(Attribute.VITALITY, 4));
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.DOMOVOI)
    //                    .name("Domovoi")
    //                    .description(Desc.Armor.DOMOVOI)
    //                    .textureName("domovoi.png")
    //                    .type(ArmorType.BODY)
    //                    .armor(15)
    //                    .marmor(35)
    //                    .itemLevel(ItemLevel.UNIQUE)
    //                    .runes(new Rune(Attribute.WILLPOWER, 3));
    //        addArmor(armorBuilder);
    //
    //        armorBuilder = new ArmorBuilder();
    //        armorBuilder.id(ID.Armor.SOUL_BARRIER)
    //                    .name("Soul Barrier")
    //                    .description(Desc.Armor.SOUL_BARRIER)
    //                    .textureName("soul_barrier.png")
    //                    .type(ArmorType.BODY)
    //                    .armor(10)
    //                    .marmor(50)
    //                    .itemLevel(ItemLevel.UNIQUE)
    //                    .runes(new Rune(Attribute.WILLPOWER, 3));
    //        addArmor(armorBuilder);
    //    }
    //
    //    private static void loadWeapons() {
    //        WeaponBuilder weaponBuilder = new WeaponBuilder();
    //        weaponBuilder.id(ID.Weapon.HANDS)
    //                    .name("Hands")
    //                    .description(Desc.Weapon.HANDS)
    //                    .textureName("hands.png")
    //                    .damage(0)
    //                    .type(WeaponType.MACE);
    //        addWeapon(weaponBuilder);
    //
    //        weaponBuilder = new WeaponBuilder();
    //        weaponBuilder.id(ID.Weapon.KNIFE)
    //                    .name("Knife")
    //                    .description(Desc.Weapon.KNIFE)
    //                    .textureName("knife.png")
    //                    .damage(20)
    //                    .type(WeaponType.DAGGER);
    //        addWeapon(weaponBuilder);
    //
    //        weaponBuilder = new WeaponBuilder();
    //        weaponBuilder.id(ID.Weapon.GUT_RIPPER)
    //                    .name("The Gut Ripper")
    //                    .description(Desc.Weapon.GUT_RIPPER)
    //                    .textureName("gut_ripper.png")
    //                    .damage(100)
    //                    .type(WeaponType.DAGGER)
    //                    .itemLevel(ItemLevel.EPIC)
    //                    .runes(new Rune(Attribute.AGILITY, 4), new Rune(Attribute.DEXTERITY, 4), new Rune(Attribute.LUCK, 1));
    //        addWeapon(weaponBuilder);
    //
    ////      addWeapon(new Weapon(ID.Weapon.IRON_SWORD, "Iron Sword", Desc.Weapon.IRON_SWORD, 0, 5, WeaponType.ONE_H_SWORD, 15));
    ////      addWeapon(new Weapon(ID.Weapon.CLAYMORE, "Claymore", Desc.Weapon.CLAYMORE, 10, 5, WeaponType.TWO_H_SWORD, 35));
    ////      addWeapon(new Weapon(ID.Weapon.BROADSWORD, "Broadsword", Desc.Weapon.BROADSWORD, 11, 5, WeaponType.TWO_H_SWORD, 28));
    ////
    ////      addWeapon(new Weapon(ID.Weapon.BATTLESWORD,
    ////              "Battlesword", Desc.Weapon.BATTLESWORD, 12, 5,
    ////              "Almas", ItemLevel.NORMAL, WeaponType.TWO_H_SWORD, 44, Element.NEUTRAL, 2,
    ////              new Rune(Attribute.STRENGTH, 2)));
    ////
    ////      addWeapon(new Weapon(ID.Weapon.LONGSWORD,
    ////              "Longsword", Desc.Weapon.LONGSWORD, 9, 5,
    ////              "Almas", ItemLevel.NORMAL, WeaponType.TWO_H_SWORD, 33, Element.NEUTRAL, 2,
    ////              new Rune(Attribute.DEXTERITY, 2), new Rune(Attribute.AGILITY, 1)));
    ////
    ////      addWeapon(new Weapon(ID.Weapon.GETSUGA_TENSHO,
    ////              "Getsuga Tensho", Desc.Weapon.GETSUGA_TENSHO, 4, 6,
    ////              "Matthew", ItemLevel.EPIC, WeaponType.ONE_H_SWORD, 150, Element.NEUTRAL, 4,
    ////              new Rune(Attribute.STRENGTH, 5), new Rune(Attribute.AGILITY, 4), new Rune(Attribute.DEXTERITY, 4), new Rune(Attribute.LUCK, 1)));
    ////
    ////      addWeapon(new Weapon(ID.Weapon.SOUL_REAPER,
    ////              "Soul Reaper", Desc.Weapon.SOUL_REAPER, 10, 10,
    ////              "Sam Bowen", ItemLevel.EPIC, WeaponType.TWO_H_AXE, 170, Element.NEUTRAL, 4,
    ////              new Rune(Attribute.STRENGTH, 7), new Rune(Attribute.VITALITY, 4), new Rune(Attribute.DEXTERITY, 2)));
    ////
    ////
    ////      addWeapon(new Weapon(ID.Weapon.DRAGON_CLAW,
    ////              "Dragon's Claw", Desc.Weapon.DRAGON_CLAW, 12, 11,
    ////              "Atheryos", ItemLevel.EPIC, WeaponType.BOW, 130, Element.FIRE, 4,
    ////              new Rune(Attribute.VITALITY, 3), new Rune(Attribute.WISDOM, 5), new Rune(Attribute.AGILITY, 3)));
    ////
    ////      addWeapon(new Weapon(ID.Weapon.FROSTMOURN, "Frostmourn", Desc.Weapon.FROSTMOURN, 8, 25,
    ////              "Stefos", ItemLevel.EPIC, WeaponType.TWO_H_SWORD, 130, Element.WATER, 4,
    ////              new Rune(Attribute.DEXTERITY, 5), new Rune(Attribute.STRENGTH, 3)));
    //    }
    //
    //    public static void load() {
    //        loadArmor();
    //        loadWeapons();
    ////        // SKILLS
    ////
    ////        addSkill(new Skill(ID.Skill.Gladiator.BLOODLUST, "Bloodlust", Desc.Skill.Gladiator.BLOODLUST, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5844145407908548491L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.ATK, -value);
    ////                // div 0 shouldn't occur
    ////                value = (int) (10*level * caster.getTotalStat(Stat.MAX_HP) / (caster.getHP() + 1));
    ////                caster.addBonusStat(Stat.ATK, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Gladiator.BASH, "Bash", Desc.Skill.Gladiator.BASH, true, 15.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 2177640389884854474L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 3;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = (1 + (15 + 5*level) / 100.0f) * caster.getTotalStat(Stat.ATK);
    ////                int d = caster.dealPhysicalDamage(target, dmg);
    ////                target.addStatusEffect(new StatusEffect(Status.STUNNED, 5.0f));
    ////
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d) + ",STUNNED");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Warrior.MIGHTY_SWING, "Mighty Swing", Desc.Skill.Warrior.MIGHTY_SWING, true, 15.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 8019137126608309704L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float diff = caster.getTotalAttribute(Attribute.STRENGTH) - target.getTotalAttribute(Attribute.STRENGTH);
    ////                float dmg = (Math.max(diff, 0) + 10*level) * 5;
    ////                int d = caster.dealPhysicalDamage(target, dmg);
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Gladiator.DOUBLE_EDGE, "Double Edge", Desc.Skill.Gladiator.DOUBLE_EDGE, true, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -5670132035647752285L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = (0.1f + 0.02f * level) * caster.getHP();
    ////                caster.setHP(Math.round(caster.getHP() - dmg));
    ////                caster.dealPureDamage(target, 2*dmg);
    ////
    ////                useResult = new SkillUseResult(2*dmg + ",PURE");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Warrior.ROAR, "Roar", Desc.Skill.Warrior.ROAR, true, 5.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5098091102433780519L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 2 + level*2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect((5.0f), ID.Skill.Warrior.ROAR,
    ////                        new Rune[] {
    ////                        new Rune(Attribute.STRENGTH, level*2),
    ////                        new Rune(Attribute.VITALITY, level*2)
    ////                },
    ////                new Essence[] {}
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("STR +" + level*2 + " VIT +" + level*2);
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Crusader.LAST_STAND, "Last Stand", Desc.Skill.Crusader.LAST_STAND, true, 60.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -8176078084748576113L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 2 + level*5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect((20.0f), ID.Skill.Crusader.LAST_STAND,
    ////                        new Rune[] {
    ////                }, new Essence[] {
    ////                        new Essence(Stat.ATK, Math.round(caster.getBaseStat(Stat.ATK)))
    ////                }
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("ATK UP!");
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Gladiator.SHATTER_ARMOR, "Shatter Armor", Desc.Skill.Gladiator.SHATTER_ARMOR, true, 30.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -4834599835655165707L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 2 + level*5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                target.addEffect(new Effect((20.0f), ID.Skill.Gladiator.SHATTER_ARMOR,
    ////                        new Rune[] {
    ////                }, new Essence[] {
    ////                        new Essence(Stat.ARM, -2*level)
    ////                }
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("ARM -" + 2*level);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Warrior.ARMOR_MASTERY, "Armor Mastery", Desc.Skill.Warrior.ARMOR_MASTERY, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 8019137126608309704L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.ARM, -value);
    ////                float factor = 2.0f;
    ////                for (Skill skill : caster.getSkills()) {
    ////                    if (skill.id.equals(ID.Skill.Crusader.DIVINE_ARMOR)) {
    ////                        factor += 0.15f * skill.getLevel();
    ////                        break;
    ////                    }
    ////                }
    ////
    ////                value = (int)(factor * level);
    ////                caster.addBonusStat(Stat.ARM, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Warrior.WARRIOR_HEART, "Heart of a Warrior", Desc.Skill.Warrior.WARRIOR_HEART, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -9161209014480342120L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.MAX_HP, -value);
    ////                float factor = 0.025f;
    ////                for (Skill skill : caster.getSkills()) {
    ////                    if (skill.id.equals(ID.Skill.Crusader.FAITH)) {
    ////                        factor += 0.01f * skill.getLevel();
    ////                        break;
    ////                    }
    ////                }
    ////
    ////
    ////                value = Math.round(factor * level * caster.getBaseStat(Stat.MAX_HP));
    ////                caster.addBonusStat(Stat.MAX_HP, value);
    ////            }
    ////        });
    ////
    ////        // MAGE SKILL SET
    ////
    ////        addSkill(new Skill(ID.Skill.Mage.AIR_SPEAR, "Air Spear", Desc.Skill.Mage.AIR_SPEAR, true, 9.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 6306777256266732648L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
    ////                int d = caster.dealMagicalDamage(target, dmg, Element.AIR);
    ////
    ////                useResult = new SkillUseResult(d);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Wizard.AMPLIFY_MAGIC, "Amplify Magic", Desc.Skill.Wizard.AMPLIFY_MAGIC, true, 30.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -6423702278665617928L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect((15.0f), ID.Skill.Wizard.AMPLIFY_MAGIC,
    ////                        new Rune[] {},
    ////                        new Essence[] {
    ////                        new Essence(Stat.MATK, 10*level)
    ////                }
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("MATK +" + 10*level);
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Enchanter.ASTRAL_PROTECTION, "Astral Protection", Desc.Skill.Enchanter.ASTRAL_PROTECTION, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 8691650266711866295L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.MDEF, -value);
    ////                value = level * 2;
    ////                caster.addBonusStat(Stat.MDEF, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Mage.EARTH_BOULDER, "Earth Boulder", Desc.Skill.Mage.EARTH_BOULDER, true, 15.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 1871962939560471153L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) + level *25;
    ////                int d = caster.dealMagicalDamage(target, dmg, Element.EARTH);
    ////
    ////                useResult = new SkillUseResult(d);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Mage.FIREBALL, "Fireball", Desc.Skill.Mage.FIREBALL, true, 9.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -1839096679550971399L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
    ////                int d = caster.dealMagicalDamage(target, dmg, Element.FIRE);
    ////
    ////                useResult = new SkillUseResult(d);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Mage.ICE_SHARD, "Ice Shard", Desc.Skill.Mage.ICE_SHARD, true, 9.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5561489415884518543L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
    ////                int d = caster.dealMagicalDamage(target, dmg, Element.WATER);
    ////
    ////                useResult = new SkillUseResult(d);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Wizard.MAGIC_MASTERY, "Magic Mastery", Desc.Skill.Wizard.MAGIC_MASTERY, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 9020149732268399438L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusAttribute(Attribute.INTELLECT, -value);
    ////                caster.addBonusAttribute(Attribute.WILLPOWER, -value);
    ////                value = level * 2;
    ////                caster.addBonusAttribute(Attribute.INTELLECT, value);
    ////                caster.addBonusAttribute(Attribute.WILLPOWER, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Enchanter.MAGIC_SHIELD, "Magic Shield", Desc.Skill.Enchanter.MAGIC_SHIELD, true, 60.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 7104420977798092420L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect((25.0f), ID.Skill.Enchanter.MAGIC_SHIELD,
    ////                        new Rune[] {},
    ////                        new Essence[] {
    ////                        new Essence(Stat.ARM, 5*level)
    ////                }
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("ARM +" + 5*level);
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Wizard.MENTAL_STRIKE, "Mental Strike", Desc.Skill.Wizard.MENTAL_STRIKE, true, 20.0f) {
    ////
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -55046003688618764L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 5;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) * (1 + level*0.1f);
    ////                caster.dealPureDamage(target, dmg);
    ////
    ////                useResult = new SkillUseResult(dmg + ",PURE");
    ////            }
    ////        });
    ////
    ////        // SCOUT SKILL SET
    ////
    ////        addSkill(new Skill(ID.Skill.Rogue.CRITICAL_STRIKE, "Critical Strike", Desc.Skill.Rogue.CRITICAL_STRIKE, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -7584376145233708322L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level * 2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.ATK) + 15 + 5 * level;
    ////                caster.addBonusStat(Stat.CRIT_CHANCE, 50 + level * 3);
    ////                int d = caster.dealPhysicalDamage(target, dmg);
    ////                caster.addBonusStat(Stat.CRIT_CHANCE, -(50 + level * 3));
    ////
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Ranger.PINPOINT_WEAKNESS, "Pinpoint Weakness", Desc.Skill.Ranger.PINPOINT_WEAKNESS, true, 15.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 2458408699758838323L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level * 2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                target.addEffect(new Effect((10.0f), ID.Skill.Ranger.PINPOINT_WEAKNESS,
    ////                        new Rune[] {},
    ////                        new Essence[] {
    ////                        new Essence(Stat.ARM, -2*level)
    ////                }
    ////                        ));
    ////
    ////                useResult = new SkillUseResult("ARM -" + 2*level);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Rogue.DOUBLE_STRIKE, "Double Strike", Desc.Skill.Rogue.DOUBLE_STRIKE, true, 8.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5685022402103377679L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level * 2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                int dmg1 = caster.attack(target);
    ////                int dmg2 = caster.attack(target);
    ////                boolean stun = false;
    ////                if (GameMath.checkChance(level*5)) {
    ////                    target.addStatusEffect(new StatusEffect(Status.STUNNED, 2.5f));
    ////                    stun = true;
    ////                }
    ////
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg1) + "," + GameMath.normalizeDamage(dmg2)
    ////                        + (stun ? ",STUNNED" : ",X2"));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Scout.EXPERIENCED_FIGHTER, "Experienced Fighter", Desc.Skill.Scout.EXPERIENCED_FIGHTER, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 2024648263069876L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusAttribute(Attribute.AGILITY, -value);
    ////                caster.addBonusAttribute(Attribute.DEXTERITY, -value);
    ////                value = level * 2;
    ////                caster.addBonusAttribute(Attribute.AGILITY, value);
    ////                caster.addBonusAttribute(Attribute.DEXTERITY, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Rogue.SHAMELESS, "Shameless", Desc.Skill.Rogue.SHAMELESS, true, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 2306928037030551618L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 10;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.ATK);
    ////                float casterHPFactor = caster.getHP() / caster.getTotalStat(Stat.MAX_HP);
    ////                float targetHPFactor = target.getHP() / target.getTotalStat(Stat.MAX_HP);
    ////                if (casterHPFactor > targetHPFactor) {
    ////                    dmg += level * 0.1f * (casterHPFactor - targetHPFactor) * dmg;
    ////                }
    ////
    ////                int d = caster.dealPhysicalDamage(target, dmg);
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Scout.WEAPON_MASTERY, "Weapon Mastery", Desc.Skill.Scout.WEAPON_MASTERY, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -6762381875332894326L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.ATK, -value);
    ////                value = level * 7;
    ////                caster.addBonusStat(Stat.ATK, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Scout.POISON_ATTACK, "Poison Attack", Desc.Skill.Scout.POISON_ATTACK, true, 30.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 6791451275759000638L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                int dmg = caster.attack(target);
    ////                boolean poison = false;
    ////                if (GameMath.checkChance(level*7)) {
    ////                    target.addStatusEffect(new StatusEffect(Status.POISONED, 10.0f));
    ////                    poison = true;
    ////                }
    ////
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg) + (poison ? ",POISONED" : ""));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Scout.TRICK_ATTACK, "Throw Dagger", Desc.Skill.Scout.TRICK_ATTACK, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 1031700846462374399L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level*2;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.ATK) + level * 2 * GameMath.random(5);
    ////                int d = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
    ////                boolean money = false;
    ////                if (caster instanceof Player) {
    ////                    ((Player)caster).incMoney(d);
    ////                    money = true;
    ////                }
    ////
    ////                useResult = new SkillUseResult(d + (money ? ",MONEY" : ""));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Rogue.TRIPLE_STRIKE, "Triple Strike", Desc.Skill.Rogue.TRIPLE_STRIKE, true, 40.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 8295208480454374043L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.ATK);
    ////                if (target.hasStatusEffect(Status.STUNNED)) {
    ////                    dmg += level * 15;
    ////                }
    ////
    ////                int dmg1 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
    ////                int dmg2 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
    ////                int dmg3 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
    ////
    ////                useResult = new SkillUseResult(dmg1 + "," + dmg2 + "," + dmg3 + ",X3");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Crusader.HOLY_LIGHT, "Holy Light", Desc.Skill.Crusader.HOLY_LIGHT, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5685022402103377679L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                target.setHP(Math.min(target.getHP() + 30 + level*10, (int)target.getTotalStat(Stat.MAX_HP)));
    ////                target.addEffect(new Effect(20.0f, ID.Skill.Crusader.HOLY_LIGHT, new Rune[] {
    ////                        new Rune(Attribute.VITALITY, level*2)
    ////                },
    ////                new Essence[] {}));
    ////
    ////                useResult = new SkillUseResult("VIT +" + level*2);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Crusader.PRECISION_STRIKE, "Precision Strike", Desc.Skill.Crusader.PRECISION_STRIKE, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 2024648263069876L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.ATK) + (caster.getTotalAttribute(Attribute.STRENGTH) / 10) * level;
    ////                caster.dealPureDamage(target, dmg);
    ////
    ////                useResult = new SkillUseResult((int)dmg);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Crusader.DIVINE_ARMOR, "Divine Armor", Desc.Skill.Crusader.DIVINE_ARMOR, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -7936080589333242098L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                // impl is in ARMOR_MASTERY
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Crusader.FAITH, "Faith", Desc.Skill.Crusader.FAITH, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 4325213967370795918L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                // impl is in WARRIOR_HEART
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Gladiator.ENDURANCE, "Endurance", Desc.Skill.Gladiator.ENDURANCE, true, 40.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -7936080589333242098L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect(15.0f, ID.Skill.Gladiator.ENDURANCE, new Rune[] {},
    ////                        new Essence[] {
    ////                        new Essence(Stat.DEF, 2*level),
    ////                        new Essence(Stat.HP_REGEN, 2*level)
    ////                }));
    ////
    ////                useResult = new SkillUseResult("DEF +" + level*2 + ", HP REGEN +" + 2*level);
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Wizard.THUNDERBOLT_FIRESTORM, "Thunderbolt Firestorm", Desc.Skill.Wizard.THUNDERBOLT_FIRESTORM, true, 40.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 4325213967370795918L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
    ////                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.FIRE);
    ////                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.AIR);
    ////
    ////                useResult = new SkillUseResult(dmg1 + "," + dmg2);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Wizard.ICICLE_AVALANCHE, "Icicle Avalanche", Desc.Skill.Wizard.ICICLE_AVALANCHE, true, 40.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 6791451275759000638L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
    ////                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.WATER);
    ////                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.EARTH);
    ////
    ////                useResult = new SkillUseResult(dmg1 + "," + dmg2);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Enchanter.MANA_BURN, "Mana Burn", Desc.Skill.Enchanter.MANA_BURN, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 1031700846462374399L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                int oldSP = target.getSP();
    ////                target.setSP(Math.max(oldSP - 50 * level, 0));
    ////                int dmg = caster.dealMagicalDamage(target, oldSP-target.getSP(), Element.NEUTRAL);
    ////
    ////                useResult = new SkillUseResult(dmg);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Enchanter.CURSE_OF_WITCHCRAFT, "Curse of Witchcraft", Desc.Skill.Enchanter.CURSE_OF_WITCHCRAFT, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 8295208480454374043L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                target.addStatusEffect(new StatusEffect(Status.SILENCED, level*3));
    ////
    ////                useResult = new SkillUseResult("SILENCED");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Enchanter.MIND_BLAST, "Mind Blast", Desc.Skill.Enchanter.MIND_BLAST, true, 20.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = -3587620067204007562L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                // TODO: impl
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, "Five Finger Death Punch", Desc.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, true, 35.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 9128637084476710269L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = 20 + level*30 - target.getTotalStat(Stat.ARM);
    ////                int d = caster.dealPhysicalDamage(target, dmg);
    ////
    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Ranger.BULLSEYE, "Bullseye", Desc.Skill.Ranger.BULLSEYE, true, 60.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 3498114139079315L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 5 + level * 10;
    ////            }
    ////
    ////            @Override
    ////            public void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float dmg = 100 + 0.2f*level * caster.getTotalAttribute(Attribute.DEXTERITY) - target.getTotalStat(GameCharacter.DEF);
    ////                caster.dealPureDamage(target, dmg);
    ////
    ////                useResult = new SkillUseResult(dmg + ",PURE");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Ranger.EAGLE_EYE, "Eagle Eye", Desc.Skill.Ranger.EAGLE_EYE, false, 0.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 7005439875094828368L;
    ////
    ////            private int value = 0;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 0;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addBonusStat(Stat.ATK, -value);
    ////                value = (int)(caster.getTotalAttribute(Attribute.DEXTERITY) * level * 0.1f);
    ////                caster.addBonusStat(Stat.ATK, value);
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Ranger.ENCHANTED_ARROW, "Enchanted Arrows", Desc.Skill.Ranger.ENCHANTED_ARROW, true, 35.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5416340917264724397L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                float duration = target.getTotalStat(Stat.ARM) * 0.1f;
    ////                target.addStatusEffect(new StatusEffect(Status.STUNNED, duration));
    ////
    ////                useResult = new SkillUseResult("STUNNED");
    ////            }
    ////        });
    ////
    ////        addSkill(new Skill(ID.Skill.Ranger.FAST_REFLEXES, "Fast Reflexes", Desc.Skill.Ranger.FAST_REFLEXES, true, 35.0f) {
    ////            /**
    ////             *
    ////             */
    ////            private static final long serialVersionUID = 5766544471206156505L;
    ////
    ////            @Override
    ////            public int getManaCost() {
    ////                return 3 + level*4;
    ////            }
    ////
    ////            @Override
    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
    ////                caster.addEffect(new Effect(10.0f, ID.Skill.Ranger.FAST_REFLEXES, new Rune[] {},
    ////                        new Essence[] {
    ////                        new Essence(Stat.ASPD, level*2)
    ////                }));
    ////
    ////                useResult = new SkillUseResult("ASPD +" + level*2);
    ////            }
    ////
    ////            @Override
    ////            public boolean isSelfTarget() {
    ////                return true;
    ////            }
    ////        });
    ////
    ////
    ////        // ENEMIES
    //
    //        EnemyBuilder enemyBuilder = new EnemyBuilder();
    //        enemyBuilder.id(ID.Enemy.MINOR_EARTH_SPIRIT)
    //                    .description(Desc.Enemy.MINOR_EARTH_SPIRIT)
    //                    .name("Minor Earth Spirit")
    //                    .textureName("enemy.png")
    //                    .xp(new Experience(100, 100, 100))
    //                    .element(Element.EARTH)
    //                    .drops(new DroppableItem(ID.Weapon.KNIFE, 50), new DroppableItem(ID.Armor.CHAINMAL, 35));
    //
    //        addEnemy(enemyBuilder);
    //
    ////
    ////        addEnemy(new Enemy(ID.Enemy.MINOR_FIRE_SPIRIT, "Minor Fire Spirit", Desc.Enemy.MINOR_FIRE_SPIRIT,
    ////                EnemyType.NORMAL, Element.FIRE, 1, new AttributeInfo(),
    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.KNIFE, 50), new DroppableItem(ID.Armor.THANATOS_BODY_ARMOR, 10)));
    ////
    ////        addEnemy(new Enemy(ID.Enemy.MINOR_EARTH_SPIRIT, "Minor Earth Spirit", Desc.Enemy.MINOR_EARTH_SPIRIT,
    ////                EnemyType.NORMAL, Element.EARTH, 1, new AttributeInfo(),
    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.IRON_SWORD, 15), new DroppableItem(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL, 5)));
    ////
    ////        addEnemy(new Enemy(ID.Enemy.MINOR_WATER_SPIRIT, "Minor Water Spirit", Desc.Enemy.MINOR_WATER_SPIRIT,
    ////                EnemyType.NORMAL, Element.WATER, 1, new AttributeInfo(),
    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Armor.CHAINMAL, 25), new DroppableItem(ID.Weapon.SOUL_REAPER, 5)));
    //    }
    //
    //    private static void addArmor(ArmorBuilder ab) {
    //        Armor armor = ab.build();
    //        defaultArmor.put(armor.getID(), armor);
    //    }
    //
    //    private static void addWeapon(WeaponBuilder wb) {
    //        Weapon weapon = wb.build();
    //        defaultWeapons.put(weapon.getID(), weapon);
    //    }
    //
    ////    private static void addSkill(Skill skill) {
    ////        defaultSkills.put(skill..getID(), skill);
    ////    }
    //
    //    private static void addEnemy(EnemyBuilder eb) {
    ////        EnemyControl enemy = eb.build();
    ////        defaultEnemies.put(enemy.getID(), enemy);
    //    }
    //
    ////    private static void addEssence(Essence e) {
    ////        defaultEssences.put(e.id, e);
    ////    }
    //
    ////    public static Skill getSkillByID(int id) {
    ////        if (defaultSkills.containsKey(id)) {
    ////            Skill sk = defaultSkills.get(id);
    ////            Constructor<? extends Skill> c;
    ////            try {
    ////                c = sk.getClass().getDeclaredConstructor(Integer.class, String.class, String.class, String.class, String.class, Boolean.class, Float.class);
    ////                return c.newInstance(sk.getID(), sk.getName(), sk.getDes, sk.textureName, sk.active, sk.skillCooldown);
    ////            }
    ////            catch (Exception e) {
    ////                e.printStackTrace();
    ////            }
    ////        }
    ////
    ////        return null;
    ////    }
    //
    //    public static Armor getArmorByID(int id) {
    //        return defaultArmor.containsKey(id) ? new Armor(defaultArmor.get(id)) : null;
    //    }
    //
    //    public static Weapon getWeaponByID(int id) {
    //        return defaultWeapons.containsKey(id) ? new Weapon(defaultWeapons.get(id)) : null;
    //    }
    //
    //    public static EnemyControl getEnemyByID(int id) {
    //        return defaultEnemies.containsKey(id) ? new EnemyControl(defaultEnemies.get(id)) : null;
    //    }
    //
    //    public static DescriptionComponent getItemByID(int id) {
    //        String sid = String.valueOf(id);
    //
    //        if (sid.startsWith("5"))
    //            return getArmorByID(id);
    //        if (sid.startsWith("4"))
    //            return getWeaponByID(id);
    //
    //        return null;
    //    }
    //
    ////    public static Essence getEssenceByID(int id) {
    ////        return defaultEssences.containsKey(id) ? new Essence(defaultEssences.get(id)) : null;
    ////    }

}