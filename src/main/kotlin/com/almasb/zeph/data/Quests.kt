package com.almasb.zeph.data

import com.almasb.zeph.combat.Experience
import com.almasb.zeph.data.Data.UsableItems
import com.almasb.zeph.data.Data.Monsters
import com.almasb.zeph.quest.quest

/**
 * Quests [7500-7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Quests {

    val SARAH_HEALING_HERBS = quest {
        desc {
            id = 7500
            name = "A cure..."
        }

        rewardMoney = 50
        rewardXP = Experience(1, 1, 5)

        collect {
            UsableItems.HEALING_HERBS x 5
        }
    }

    val TUTORIAL_KILLS = quest {
        desc {
            id = 7501
            name = "A new beginning"
        }

        rewardMoney = 70
        rewardXP = Experience(2, 2, 2)

        kill {
            Monsters.SKELETON_WARRIOR x 3
        }
    }
}