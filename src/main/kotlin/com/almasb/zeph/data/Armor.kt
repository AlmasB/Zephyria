package com.almasb.zeph.data

import com.almasb.zeph.item.ArmorType
import com.almasb.zeph.item.armor

/**
 * Weapons id range [5000-5999].
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

        armorType = ArmorType.HELM
    }

    fun CLOTHES() = armor {
        desc {
            id = 5001
            name = "Clothes"
            description = "Just normal clothes, don't count on any defense."
            textureName = "items/armor/clothes.png"
        }

        armorType = ArmorType.BODY
    }

    fun SHOES() = armor {
        desc {
            id = 5002
            name = "Shoes"
            description = "Some old shoes."
            textureName = "items/armor/shoes.png"
        }

        armorType = ArmorType.SHOES
    }
}