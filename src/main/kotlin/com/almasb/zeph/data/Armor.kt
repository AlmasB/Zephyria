package com.almasb.zeph.data

import com.almasb.zeph.combat.runIfChance
import com.almasb.zeph.item.ArmorType.*
import com.almasb.zeph.item.armor

/**
 * Armor id range [5000-5999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Armor {
    fun HAT() = armor {
        desc {
            id = 5000
            name = "Hat"
            description = "Ordinary hat, already out of fashion."
            textureName = "items/armor/hat.png"
        }

        armorType = HELM
    }

    fun CLOTHES() = armor {
        desc {
            id = 5001
            name = "Clothes"
            description = "Just normal clothes, don't count on any defense. Chance to restore 1 HP on hit."
            textureName = "items/armor/clothes.png"
        }

        armorType = BODY

        onBeingHitScript = { attacker, target ->
            runIfChance(50) {
                target.hp.restore(1.0)
            }
        }
    }

    fun SHOES() = armor {
        desc {
            id = 5002
            name = "Shoes"
            description = "Some old shoes."
            textureName = "items/armor/shoes.png"
        }

        armorType = SHOES
    }
}