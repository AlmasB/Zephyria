package com.almasb.zeph.data

import com.almasb.zeph.combat.Experience
import com.almasb.zeph.data.Data.UsableItems
import com.almasb.zeph.quest.quest

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Quests {

    val APPLES = quest {
        desc {
            id = 7500
            name = "Apples"
            description = "Fetch me an apple."
        }

        rewardMoney = 3
        rewardXP = Experience(0, 0, 5)

        collect {
            UsableItems.CARROT x 2
            UsableItems.HEALING_POTION x 3
        }

//        go {
//
//        }
//
//        talk {
//
//        }
//
//        kill {
//
//        }
    }
}