package com.almasb.zeph.data

import com.almasb.zeph.item.usableItem

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItem {

    fun HEALING_POTION() = usableItem {
        desc {
            id = 9000
            name = "Healing Potion"
            description = "Restores 50 HP"
            textureName = "items/usable/healing_potion.png"
        }

        beforeUseScript = {
            // everyone can use it
            true
        }

        onUseScript = {
            it.hp.restore(50.0)
        }
    }
}