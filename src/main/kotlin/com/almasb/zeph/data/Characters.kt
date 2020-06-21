package com.almasb.zeph.data

import com.almasb.zeph.character.char
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Experience

/**
 * Characters id range [2000-2999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Characters {

    val SKELETON_ARCHER = char {
        desc {
            id = 2004
            name = "Skeleton-Archer"
            description = "Skeleton-Archer Description."
            textureName = "chars/enemies/skeleton_archer.png"
        }

        baseLevel = 1
        element = Element.EARTH
        rewardXP = Experience(3, 3, 3)

        attributes {
            dex = 1
        }

        drops {
            Data.Weapons.Daggers.KNIFE has 50
        }
    }

    val SKELETON_WARRIOR = char {
        desc {
            id = 2005
            name = "Skeleton Warrior"
            description = "Skeleton Warrior Description."
            textureName = "chars/enemies/skeleton_warrior.png"
        }

        baseLevel = 1
        element = Element.EARTH
        rewardXP = Experience(3, 5, 3)

        attributes {
            str = 1
            vit = 1
        }

        drops {
            Data.Weapons.Daggers.KNIFE has 50
        }
    }

    val SKELETON_MAGE = char {
        desc {
            id = 2006
            name = "Skeleton Mage"
            description = "Skeleton Mage Description."
            textureName = "chars/enemies/skeleton_mage.png"
        }

        baseLevel = 2
        element = Element.FIRE
        rewardXP = Experience(5, 3, 7)

        attributes {
            str = 3
            vit = 5
            dex = 2
            agi = 2
            int = 10
        }
    }
}