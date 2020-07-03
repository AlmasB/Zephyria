package com.almasb.zeph.data

import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.runIfChance
import com.almasb.zeph.item.*
import com.almasb.zeph.item.ArmorType.*

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

// 5004-5099
class Helm {
    val HAT = armorHelm {
        desc {
            id = 5000
            name = "Hat"
            description = "Ordinary hat, already out of fashion."
            textureName = "items/armor/hat.png"
        }

        armorType = HELM
    }
}

// 5100-5199
class Body {
    val CLOTHES = armorBody {
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

    val CHAINMAIL = armorBody {
        desc {
            id = 5100
            name = "Chainmail"
            description = "Armor consisting of small metal rings linked together in a pattern to form a mesh."
        }

        armor = 6
        marmor = 2

        STRENGTH +1
    }

    val SOUL_BARRIER = armorBody {
        desc {
            id = 5101
            name = "Soul Barrier"
            description = "Protects its wearer from magic attacks."
        }

        itemLevel = ItemLevel.UNIQUE

        armor = 5
        marmor = 20

        WILLPOWER +2
    }

    val THANATOS_BODY_ARMOR = armorBody {
        desc {
            id = 5102
            name = "Thanatos Body Armor"
            description = "A shattered piece of Thanatos' legendary armor. Grants its user great constitution."
        }

        itemLevel = ItemLevel.EPIC

        armor = 30
        marmor = 10

        VITALITY +5
        PERCEPTION +3
    }

    val MAGE_ARMOR = armorBody {
        desc {
            id = 5103
            name = "Mage Armor"
            description = "Light armor designed for mages."
        }

        armor = 3
        marmor = 3

        INTELLECT +1
        WILLPOWER +1
    }

    val LEGION_PLATE = armorBody {
        desc {
            id = 5104
            name = "Legion Plate"
            description = "Body armor made from steel and subsequently enchanted to resist magic attacks."
        }

        armor = 10
        marmor = 3

        VITALITY +2
    }

    val TRAINING_ARMOR = armorBody {
        desc {
            id = 5105
            name = "Training Armor"
            description = "Armor used for practice."
        }

        armor = 3
    }
}

// 5200-5299
class Shoes {
    val SIMPLE_SHOES = armorShoes {
        desc {
            id = 5002
            name = "Shoes"
            description = "Some old shoes."
            textureName = "items/armor/shoes.png"
        }

        armorType = SHOES
    }

    val LUCKY_SHOES = armorShoes {
        desc {
            id = 5200
            name = "Lucky Shoes"
            description = "It is said that the wearer gains immense luck."
            textureName = "items/armor/lucky_shoes.png"
        }

        armorType = SHOES

        LUCK +5
    }
}