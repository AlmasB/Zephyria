package com.almasb.zeph.data

import com.almasb.zeph.item.WeaponType
import com.almasb.zeph.item.weapon

/**
 * Weapons id range [4000-4999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapon {
    fun HANDS() = weapon {
        desc {
            id = 4000
            name = "Hands"
            description = "That's right, go kill everyone with your bare hands."
            textureName = "items/weapons/hands.png"
        }

        type = WeaponType.MACE
    }

    fun KNIFE() = weapon {
        desc {
            id = 4001
            name = "Knife"
            description = "A simple knife with poor blade."
            textureName = "items/weapons/knife.png"
        }

        type = WeaponType.DAGGER
        pureDamage = 15
    }

    fun IRON_SWORD() = weapon {
        desc {
            id = 4100
            name = "Iron Sword"
            description = "A standard warrior's sword with decent attack damage."
            textureName = "items/weapons/iron_sword.png"
        }

        type = WeaponType.ONE_H_SWORD
        pureDamage = 25
    }
}