package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element.WATER
import com.almasb.zeph.combat.Stat.MAX_SP
import com.almasb.zeph.item.ItemLevel.EPIC
import com.almasb.zeph.item.WeaponType.*
import com.almasb.zeph.item.weapon

/**
 * Weapons id range [4000-4999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapons {

    val Maces = Maces()
    val OneHandedSwords = OneHandedSwords()
    val OneHandedAxes = OneHandedAxes()
    val Daggers = Daggers()
    val Spears = Spears()
    val Rods = Rods()
    val Shields = Shields()
    val TwoHandedSwords = TwoHandedSwords()
    val TwoHandedAxes = TwoHandedAxes()
    val Katars = Katars()
    val Bows = Bows()
}

// MACES 4500?
class Maces {
    val HANDS = weapon {
        desc {
            id = 4000
            name = "Hands"
            description = "That's right, go kill everyone with your bare hands."
            textureName = "items/weapons/hands.png"
        }

        type = MACE
    }
}

//        public static final String HALLSTATT_SWORD = "A sword favored by gladiators, it is especially designed for battles against armored enemies";
//        public static final String KAMPILAN_SWORD = "A thin sword designed to be easily bent, light, and very elastic";
//        public static final String MACHETE = "A strong cleaver-like sword";
//        public static final String TEGA_SWORD = "A ceremonial sword used by gravekeeper's to lead the dead to the great beyond";
//        public static final String SCHIAVONA_SWORD = "A popular sword among mercenary soldiers";
//        public static final String COLICHERMARDE_SWORD = "S";

class OneHandedSwords {
    val PRACTICE_SWORD = weapon {
        desc {
            id = 4100
            name = "Practice Sword"
            description = "A basic one-handed sword."
        }

        type = ONE_H_SWORD
        pureDamage = 15
    }

    val IRON_SWORD = weapon {
        desc {
            id = 4101
            name = "Iron Sword"
            description = "A standard warrior's sword."
        }

        type = ONE_H_SWORD
        pureDamage = 25
    }

    val SCIMITAR = weapon {
        desc {
            id = 4102
            name = "Scimitar"
            description = "A balanced sword with good parrying characteristics."
        }

        type = ONE_H_SWORD
        pureDamage = 35

        AGILITY +1
        DEXTERITY +1
    }

    val STEEL_SWORD = weapon {
        desc {
            id = 4103
            name = "Steel Sword"
            description = "A swords made of strongest steel."
        }

        type = ONE_H_SWORD
        pureDamage = 38
    }
}

class OneHandedAxes {
    // 1H AXES 4300
}

class Daggers {
    val KNIFE = weapon {
        desc {
            id = 4001
            name = "Knife"
            description = "A simple knife with poor blade."
        }

        type = DAGGER
        pureDamage = 10
    }

    val GUT_RIPPER = weapon {
        desc {
            id = 4050
            name = "Gut Ripper"
            description = "A fierce weapon that punctures and ruptures enemies with vicious and lightning fast blows."
        }

        type = DAGGER
        itemLevel = EPIC
        pureDamage = 100

        AGILITY +4
        DEXTERITY +4
        LUCK +1
    }
}

// SPEARS 4700
class Spears {

}

// RODS 4900
class Rods {

}

class Shields {

}

class TwoHandedSwords {
    val CLAYMORE = weapon {
        desc {
            id = 4200
            name = "Claymore"
            description = "Large, double-edged broad sword"
        }

        type = TWO_H_SWORD
        pureDamage = 35

        STRENGTH +1
    }

    val BROADSWORD = weapon {
        desc {
            id = 4201
            name = "Broadsword"
            description = "A sword with a wide, double sided blade."
        }

        type = TWO_H_SWORD
        pureDamage = 28

        LUCK +1
    }

    val BATTLESWORD = weapon {
        desc {
            id = 4202
            name = "Battlesword"
            description = "A terrifying two-handed sword that is said to stimulate the nerves in order to kill, once it's in the wearer's hands."
        }

        type = TWO_H_SWORD
        pureDamage = 44

        STRENGTH +2
        VITALITY +1
    }

    val LONGSWORD = weapon {
        desc {
            id = 4203
            name = "Longsword"
            description = "A two-handed sword with straight double-edged blade."
        }

        type = TWO_H_SWORD
        pureDamage = 33

        AGILITY +1
        DEXTERITY +2
    }





    val FROSTMOURNE = weapon {
        desc {
            id = 4250
            name = "Frostmourne"
            description = "The legendary sword of the Ice Dungeon's King."
            textureName = "items/weapons/frostmourne.png"
        }

        itemLevel = EPIC
        type = TWO_H_SWORD
        pureDamage = 130
        element = WATER

        STRENGTH +3
        DEXTERITY +5
    }
}

class TwoHandedAxes {
    // 2H AXES 4400

    val SOUL_REAPER = weapon {
        desc {
            id = 4400
            name = "Soul Reaper"
            description = "Forged in the depths of Aesmir, it is said the wielder can feel the weapon crave the souls of its enemies.\n" +
                    "Steals 3 SP on attack."
            textureName = "items/weapons/soul_reaper.png"
        }

        itemLevel = EPIC
        type = TWO_H_AXE
        pureDamage = 170

        onAttackScript = { attacker, target ->
            target.sp.damage(3.0)
            attacker.sp.restore(3.0)
        }

        STRENGTH +7
        VITALITY +4
        DEXTERITY +2

        MAX_SP +20
    }
}

// KATARS 4600
class Katars {

}



// BOWS 4800
class Bows {

}