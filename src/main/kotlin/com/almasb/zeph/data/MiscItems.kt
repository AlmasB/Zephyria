package com.almasb.zeph.data

import com.almasb.zeph.item.miscItem

/**
 * Misc [6500-6999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MiscItems {
    val SKELETON_BONE = miscItem {
        desc {
            id = 6500
            name = "Skeleton Bone"
            description = "An old bone."
        }
    }

    val ORC_TOOTH = miscItem {
        desc {
            id = 6501
            name = "Orc Tooth"
            description = "A chipped tooth of an orc."
        }
    }
}