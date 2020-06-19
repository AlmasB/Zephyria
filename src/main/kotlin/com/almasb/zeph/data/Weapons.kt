package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element.WATER
import com.almasb.zeph.combat.Stat.MAX_SP
import com.almasb.zeph.item.ItemLevel.EPIC
import com.almasb.zeph.item.WeaponType.*
import com.almasb.zeph.item.weapon

/**
 * Weapons id range [4000-4999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Weapons {

    fun HANDS() = weapon {
        desc {
            id = 4000
            name = "Hands"
            description = "That's right, go kill everyone with your bare hands."
            textureName = "items/weapons/hands.png"
        }

        type = MACE
    }

    fun KNIFE() = weapon {
        desc {
            id = 4001
            name = "Knife"
            description = "A simple knife with poor blade."
            textureName = "items/weapons/knife.png"
        }

        type = DAGGER
        pureDamage = 15
    }

    fun IRON_SWORD() = weapon {
        desc {
            id = 4100
            name = "Iron Sword"
            description = "A standard warrior's sword with decent attack damage."
            textureName = "items/weapons/iron_sword.png"
        }

        type = ONE_H_SWORD
        pureDamage = 25

        onEquipScript = {
            //it.characterComponent.addEffect()
        }
    }

    fun FROSTMOURNE() = weapon {
        desc {
            id = 4202
            name = "Frostmourne"
            description = "The legendary sword of the Ice Dungeon's King."
            textureName = "items/weapons/frostmourne.png"
        }

        itemLevel = EPIC
        type = TWO_H_SWORD
        pureDamage = 130
        element = WATER

        STRENGTH +3
        DEXTERITY +5
    }

    // 1H AXES 4300

    // 2H AXES 4400

    fun SOUL_REAPER() = weapon {
        desc {
            id = 4400
            name = "Soul Reaper"
            description = "Forged in the depths of Aesmir, it is said the wielder can feel the weapon crave the souls of its enemies.\n" +
                    "Steals 3 SP on attack."
            textureName = "items/weapons/soul_reaper.png"
        }

        itemLevel = EPIC
        type = TWO_H_AXE
        pureDamage = 170

        onAttackScript = { attacker, target ->
            target.sp.damage(3.0)
            attacker.sp.restore(3.0)
        }

        STRENGTH +7
        VITALITY +4
        DEXTERITY +2

        MAX_SP +20
    }
}