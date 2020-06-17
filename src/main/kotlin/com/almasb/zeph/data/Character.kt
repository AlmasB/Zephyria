package com.almasb.zeph.data

import com.almasb.zeph.character.char
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Experience

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Character {

    fun SKELETON_ARCHER() = char {
        desc {
            id = 2004
            name = "Skeleton-Archer"
            description = "Skeleton-Archer Description."
            textureName = "chars/enemies/skeleton_archer.png"
        }

        baseLevel = 2
        element = Element.EARTH
        rewardXP = Experience(10, 3, 3)

        attributes {
            str = 5
            vit = 30
            dex = 3
            agi = 2
        }

        // drop items
        4001 has 50
    }

    fun SKELETON_WARRIOR() = char {
        desc {
            id = 2005
            name = "Skeleton Warrior"
            description = "Skeleton Warrior Description."
            textureName = "chars/enemies/skeleton_warrior.png"
        }

        baseLevel = 2
        element = Element.EARTH
        rewardXP = Experience(20, 5, 3)

        attributes {
            str = 12
            vit = 11
            dex = 8
            agi = 5
        }

        // drop items
        4001 has 50
    }

    fun SKELETON_MAGE() = char {
        desc {
            id = 2005
            name = "Skeleton Mage"
            description = "Skeleton Mage Description."
            textureName = "chars/enemies/skeleton_mage.png"
        }

        baseLevel = 2
        element = Element.FIRE
        rewardXP = Experience(15, 3, 7)

        attributes {
            str = 3
            vit = 5
            dex = 2
            agi = 2
            int = 10
        }
    }
}