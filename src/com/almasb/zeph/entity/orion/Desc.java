package com.almasb.zeph.entity.orion;

public class Desc {
    public class Armor {
        public static final String HAT = "Ordinary hat, already out of fashion";
        public static final String CLOTHES = "Just normal clothes, don't count on any defense";
        public static final String SHOES = "Average size shoes";
        public static final String CHAINMAL = "Armour consisting of small metal rings linked together in a pattern to form a mesh.";
        public static final String SOUL_BARRIER = "Protects its wearer from magic attacks";
        public static final String DOMOVOI = "Generations of guardians have bled in this armour, imbuing it with spirits of protection. Spirits that awaken when the wearers need is greatest.";
        public static final String SAPPHIRE_LEGION_PLATE_MAIL = "Produced in the Jaded Forges of the Jewelled King, strictly for use by warriors who have proved their mastery of combat through decades of service.";
        public static final String THANATOS_BODY_ARMOR = "A shattered piece of Thanatos' legendary armor. Grants its user great constitution";
    }

    public class Enemy {
        public static final String MINOR_FIRE_SPIRIT = "Fire Spirit desc";
        public static final String MINOR_EARTH_SPIRIT = "Earth Spirit desc";
        public static final String MINOR_WATER_SPIRIT = "Water Spirit desc";
        public static final String MINOR_WIND_SPIRIT = "Wind Spirit desc";
    }

    public class Essence {

    }

    public class Skill {

        public class Warrior {
            public static final String MIGHTY_SWING = "Physical attack. Damage is greater if you have more STR than your target";
            public static final String ROAR = "Increases STR and VIT for the duration";
            public static final String WARRIOR_HEART = "Passively increases max HP";
            public static final String ARMOR_MASTERY = "Increases armor rating";
        }

        public class Crusader {
            public static final String HOLY_LIGHT = "Heals and increases VIT for the duration";
            public static final String FAITH = "Further increases bonus given by Heart of a Warrior skill";
            public static final String DIVINE_ARMOR = "Further increases bonus given by Armor Mastery skill";
            public static final String PRECISION_STRIKE = "Deals armor ignoring damage based on STR";
            public static final String LAST_STAND = "Deals double base damage for the duration";
        }

        public class Gladiator {
            public static final String BLOODLUST = "Increases ATK based on the missing % HP";
            public static final String BASH = "A powerful physical attack that stuns the target for 5 seconds";
            public static final String SHATTER_ARMOR = "Decreases target's armor for the duration";
            public static final String DOUBLE_EDGE = "Sacrifice % of HP to deal double that damage to target. Damage is pure";
            public static final String ENDURANCE = "Takes less damage and regenerates HP faster for the duration";
        }

        public class Mage {
            public static final String FIREBALL = "Deals magic damage with fire element";
            public static final String ICE_SHARD = "Deals magic damage with water element";
            public static final String AIR_SPEAR = "Deals magic damage with air element";
            public static final String EARTH_BOULDER = "Deals magic damage with earth element";
        }

        public class Wizard {
            public static final String MENTAL_STRIKE = "Deals pure damage based on MATK";
            public static final String AMPLIFY_MAGIC = "Increases MATK for the duration";
            public static final String MAGIC_MASTERY = "Passively increases INT and WIL";
            public static final String THUNDERBOLT_FIRESTORM = "Deals magic damage with air and fire element";
            public static final String ICICLE_AVALANCHE = "Deals magic damage with water and earth element";
        }

        public class Enchanter {
            public static final String MAGIC_SHIELD = "Increases Armor rating for the duration";
            public static final String ASTRAL_PROTECTION = "Passively increases MDEF";
            public static final String MIND_BLAST = "Drains % of target's SP. Increases mana cost of all target's skills";
            public static final String CURSE_OF_WITCHCRAFT = "Target cannot use skills for the duration";
            public static final String MANA_BURN = "Burns target's SP and deals damage based on the SP burnt";
        }

        public class Scout {
            public static final String POISON_ATTACK = "Attacks the target with high chance to poison him";
            public static final String TRICK_ATTACK = "Deals physical damage and steals gold equal to damage dealt";
            public static final String WEAPON_MASTERY = "Passively increases ATK";
            public static final String EXPERIENCED_FIGHTER = "Passively increases AGI and DEX";
        }

        public class Rogue {
            public static final String SHAMELESS = "Deals more damage if target's % HP is lower than yours. No cooldown but consumes mana";
            public static final String DOUBLE_STRIKE = "Quickly performs two attacks with a chance to stun the target";
            public static final String TRIPLE_STRIKE = "Quickly performs three attacks. Deals more damage if target is stunned";
            public static final String CRITICAL_STRIKE = "Strikes the target with high chance of crit. Crit damage is greater for this skill";
            public static final String FIVE_FINGER_DEATH_PUNCH = "Deals devastating damage to unarmoured targets";
        }

        public class Ranger {
            public static final String FAST_REFLEXES = "Increases ASPD for the duration";
            public static final String ENCHANTED_ARROW = "Stuns target. Stun lasts longer for target's with high armor rating";
            public static final String EAGLE_EYE = "Passively increases ATK based on DEX";
            public static final String PINPOINT_WEAKNESS = "Decreases target's defense for the duration";
            public static final String BULLSEYE = "Deals armor ignoring damage to target."
                    + "Target's defense is not ignored. "
                    + "Damage is based on caster's DEX";
        }
    }

    public class Weapon {
        public static final String HANDS = "That's right, go kill everyone with your bare hands";
        public static final String GETSUGA_TENSHO = "A powerful sword that is carved from the fangs of the moon itself and pierced through heaven";
        public static final String SOUL_REAPER = "Forged in the dephts of Aesmir, it is said the weilder can feel the weapon crave the souls of its enemies";
        public static final String GUT_RIPPER = "A fierce weapon that punctures and ruptures enemies with vicious and lightning fast blows";
        public static final String DRAGON_CLAW = "A mythical bow made of claws of the legendary dragon. Contains dragon's wisdom and loyal to only one master throughout his whole life. Grants dragon's and earlier owner's wisdom and knowledge to the new master";
        public static final String FROSTMOURN = "The legendary sword of the Ice Dungeon's King. Can turn enemies into frozen rocks with 5% chance. Has water element";
        public static final String IRON_SWORD = "A standard warrior's sword with decent attack damage";
        public static final String KNIFE = "A simple knife with poor blade";

        public static final String PRACTICE_SWORD = "A basic one-handed sword";
        public static final String CLAYMORE = "Large, double-edged broad sword that was used by the Scottish highlanders";
        public static final String BROADSWORD = "A sword with a wide, double sided blade";
        public static final String HALLSTATT_SWORD = "A sword favored by gladiators, it is especially designed for battles against armored enemies";
        public static final String KAMPILAN_SWORD = "A thin sword designed to be easily bent, light, and very elastic";
        public static final String MACHETE = "A strong cleaver-like sword";
        public static final String TEGA_SWORD = "A ceremonial sword used by gravekeeper's to lead the dead to the great beyond";
        public static final String BATTLESWORD = "A terrifying two-handed sword that is said to stimulate the nerves in order to kill, once it's in the wearer's hands";
        public static final String LONGSWORD = "A two-handed sword with straight double-edged blade";
        public static final String SCHIAVONA_SWORD = "A popular sword among mercenary soldiers";
        public static final String COLICHERMARDE_SWORD = "Small sword with good parrying characteristics";
    }
}
