package com.almasb.zeph.data

import com.almasb.zeph.character.CharacterType
import com.almasb.zeph.character.CharacterType.*
import com.almasb.zeph.character.char
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Element.*
import com.almasb.zeph.combat.Experience

/**
 * Characters id range [2000-2999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Monsters {

    val SKELETON_ARCHER = char {
        desc {
            id = 2004
            name = "Skeleton Archer"
        }

        baseLevel = 1
        element = EARTH
        rewardXP = Experience(3, 3, 3)

        attributes {
            DEXTERITY +1
        }

        drops {
            Data.Weapons.Daggers.KNIFE has 50
        }
    }

    val SKELETON_WARRIOR = char {
        desc {
            id = 2005
            name = "Skeleton Warrior"
        }

        baseLevel = 1
        element = EARTH
        rewardXP = Experience(3, 5, 3)

        attributes {
            STRENGTH +1
            VITALITY +1
        }

        drops {
            Data.Weapons.Daggers.KNIFE has 50
        }
    }

    val SKELETON_MAGE = char {
        desc {
            id = 2006
            name = "Skeleton Mage"
        }

        baseLevel = 2
        element = FIRE
        rewardXP = Experience(5, 3, 7)

        attributes {
            INTELLECT +3
            WILLPOWER +1
        }
    }

    val SKELETON_ROGUE = char {
        desc {
            id = 2007
            name = "Skeleton Rogue"
        }

        baseLevel = 3
        element = EARTH
        rewardXP = Experience(7, 6, 4)

        attributes {
            DEXTERITY +4
            AGILITY +2
        }
    }

    val SKELETON_KNIGHT = char {
        desc {
            id = 2008
            name = "Skeleton Knight"
        }

        baseLevel = 4
        element = EARTH
        rewardXP = Experience(8, 5, 6)

        attributes {
            STRENGTH +4
            VITALITY +5
            DEXTERITY +2
            AGILITY +1
        }
    }

    val SKELETON_GLADIATOR = char {
        desc {
            id = 2009
            name = "Skeleton Gladiator"
        }

        baseLevel = 5
        element = EARTH
        rewardXP = Experience(9, 6, 6)

        attributes {
            STRENGTH +5
            VITALITY +1
            DEXTERITY +4
            AGILITY +3
        }
    }

    val SKELETON_CRUSADER = char {
        desc {
            id = 2010
            name = "Skeleton Crusader"
        }

        baseLevel = 6
        element = EARTH
        rewardXP = Experience(11, 8, 4)

        attributes {
            STRENGTH +7
            DEXTERITY +2
            AGILITY +2
            WILLPOWER +6
            LUCK +2
        }
    }

    val SKELETON_ASSASSIN = char {
        desc {
            id = 2011
            name = "Skeleton Assassin"
        }

        baseLevel = 7
        element = AIR
        rewardXP = Experience(13, 5, 10)

        attributes {
            STRENGTH +7
            DEXTERITY +6
            AGILITY +6
            LUCK +5
        }
    }

    val SKELETON_STALKER = char {
        desc {
            id = 2012
            name = "Skeleton Stalker"
        }

        baseLevel = 8
        element = EARTH
        rewardXP = Experience(13, 8, 10)

        attributes {
            STRENGTH +4
            VITALITY +2
            DEXTERITY +8
            AGILITY +9
        }
    }

    val SKELETON_WIZARD = char {
        desc {
            id = 2013
            name = "Skeleton Wizard"
        }

        baseLevel = 9
        element = FIRE
        rewardXP = Experience(14, 9, 9)

        attributes {
            INTELLECT +9
            WILLPOWER +4
            WISDOM +6
            DEXTERITY +2
            LUCK +1
        }
    }

    val SKELETON_ROYAL_GUARD = char {
        desc {
            id = 2014
            name = "Skeleton Royal Guard"
        }

        baseLevel = 9
        element = EARTH
        rewardXP = Experience(15, 10, 9)

        attributes {
            STRENGTH +5
            VITALITY +10
            DEXTERITY +4
            AGILITY +5
            WILLPOWER +2
        }
    }

    val SKELETON_KING = char {
        desc {
            id = 2015
            name = "Skeleton King"
            textureName = "chars/enemies/skeleton_king.png"
        }

        baseLevel = 10
        type = BOSS
        element = NEUTRAL
        rewardXP = Experience(30, 30, 30)

        attributes {
            STRENGTH +15
            VITALITY +15
            DEXTERITY +10
            AGILITY +10
            LUCK +5
            WILLPOWER +5
        }
    }
}