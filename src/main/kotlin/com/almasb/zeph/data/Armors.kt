package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.runIfChance
import com.almasb.zeph.item.ArmorType.*
import com.almasb.zeph.item.armor

/**
 * Armor id range [5000-5999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Armors {

    @JvmField
    val Helm = Helm()

    @JvmField
    val Body = Body()

    @JvmField
    val Shoes = Shoes()
}

class Helm {
    val HAT = armor {
        desc {
            id = 5000
            name = "Hat"
            description = "Ordinary hat, already out of fashion."
            textureName = "items/armor/hat.png"
        }

        armorType = HELM
    }
}

class Body {
    val CLOTHES = armor {
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
}

class Shoes {
    val SIMPLE_SHOES = armor {
        desc {
            id = 5002
            name = "Shoes"
            description = "Some old shoes."
            textureName = "items/armor/shoes.png"
        }

        armorType = SHOES
    }

    val LUCKY_SHOES = armor {
        desc {
            id = 5003
            name = "Lucky Shoes"
            description = "It is said that the wearer gains immense luck."
            textureName = "items/armor/lucky_shoes.png"
        }

        armorType = SHOES

        LUCK +5
    }
}