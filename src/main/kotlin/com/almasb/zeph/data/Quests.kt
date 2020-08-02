package com.almasb.zeph.data

import com.almasb.zeph.combat.Experience
import com.almasb.zeph.data.Data.UsableItems
import com.almasb.zeph.quest.quest

/**
 * Quests [7500-7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Quests {

    val SARAH_HEALING_HERBS = quest {

        // TODO: auto-generate description from collect{}?

        desc {
            id = 7500
            name = "A cure..."
            description = "Collect 5 healing herbs for Sarah."
        }

        rewardMoney = 50
        rewardXP = Experience(1, 1, 5)

        collect {
            UsableItems.HEALING_HERBS x 5
        }
    }
}