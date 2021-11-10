package com.almasb.zeph.data

import com.almasb.zeph.combat.XP
import com.almasb.zeph.data.Data.Maps
import com.almasb.zeph.data.Data.UsableItems
import com.almasb.zeph.data.Data.Monsters
import com.almasb.zeph.data.Data.NPCs
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
        rewardXP = XP(1, 1, 5)

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
        rewardXP = XP(2, 2, 2)

        kill {
            Monsters.SKELETON_WARRIOR x 3
        }
    }

    val TUTORIAL_GOTO = quest {
        desc {
            id = 7502
            name = "Finding your way"
            description = "This tutorial tells you how to move around."
        }

        rewardMoney = 10
        rewardXP = XP(3, 2, 1)

        go {
            Maps.TUTORIAL_MAP at p(5, 5)
        }
    }

    val TUTORIAL_TALKTO = quest {
        desc {
            id = 7503
            name = "Networking is good"
            description = "This tutorial tells you how to talk to NPCs."
        }

        rewardMoney = 20
        rewardXP = XP(1, 2, 4)

        talk(NPCs.SARAH)
    }
}