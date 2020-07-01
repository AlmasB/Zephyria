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

// 5004-5099
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

// 5100-5199
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

    //        fun CHAINMAIL() = listOf<Component>(
//                Description(5100, "Chainmail", "Armour consisting of small metal rings linked together in a pattern to form a mesh.", "items/armor/chainmail.png"),
//                ArmorDataComponent(ItemLevel.NORMAL, ArmorType.BODY, 10, 5)
//                        .withRune(Rune(Attribute.STRENGTH, 2))
//        )
//
//        fun SOUL_BARRIER() = listOf<Component>(
//                Description(5101, "Soul Barrier", "Protects its wearer from magic attacks.", "items/armor/soul_barrier.png"),
//                ArmorDataComponent(ItemLevel.UNIQUE, ArmorType.BODY, 10, 50)
//                        .withRune(Rune(Attribute.WILLPOWER, 2))
//        )
//
//
//        fun THANATOS_BODY_ARMOR() = listOf<Component>(
//                Description(5102, "Thanatos Body Armor", "A shattered piece of Thanatos' legendary armor. Grants its user great constitution.", "items/armor/thanatos_body_armor.png"),
//                ArmorDataComponent(ItemLevel.EPIC, ArmorType.BODY, 50, 25)
//                        .withRune(Rune(Attribute.VITALITY, 5))
//                        .withRune(Rune(Attribute.PERCEPTION, 3))
//        )
}

// 5200-5299
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
            id = 5200
            name = "Lucky Shoes"
            description = "It is said that the wearer gains immense luck."
            textureName = "items/armor/lucky_shoes.png"
        }

        armorType = SHOES

        LUCK +5
    }
}