package com.almasb.zeph.data

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.Effect
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.Gameplay
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.*
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Stat.*
import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.Weapon
import com.almasb.zeph.item.usableItem
import javafx.util.Duration

/**
 * Usable items id range [6000-6999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItems {

    val HEALING_POTION = usableItem {
        desc {
            id = 6000
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

    val MANA_POTION = usableItem {
        desc {
            id = 6001
            name = "Mana Potion"
            description = "Restores 30 SP"
            textureName = "items/usable/mana_potion.png"
        }

        onUseScript = {
            it.sp.restore(30.0)
        }
    }

    val TREASURE_BOX = usableItem {
        desc {
            id = 6002
            name = "Treasure Box"
            description = "An old box that may contain valuable items. Gives a random equipment item when opened."
            textureName = "items/usable/treasure_box.png"
        }

        onUseScript = {
            val isWeapon = GameMath.checkChance(50)

            val item =
                    if (isWeapon)
                        Weapon(Data.allWeaponData.random())
                    else
                        Armor(Data.allArmorData.random())

            it.inventory.add(item)
        }
    }

    val FIRE_MIXTURE = usableItem {
        desc {
            id = 6003
            name = "Fire Mixture"
            description = "Imbues the equipped weapon with the FIRE element for 2 minutes"
            textureName = "items/usable/fire_mixture.png"
        }

        onUseScript = {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.FIRE, Duration.minutes(2.0)))
        }
    }

    // TODO: mixtures 6004-6006

    val TELEPORTATION_STONE = usableItem {
        desc {
            id = 6007
            name = "Teleportation Stone"
            description = "Teleports the stone user to a random location on the map."
            textureName = "items/usable/teleportation_stone.png"
        }

        onUseScript = {
            val grid = Gameplay.currentMap.grid

            val cell = grid.getRandomCell { it.isWalkable }.get()

            it.actionComponent.orderIdle();
            it.setPositionToCell(cell.x, cell.y)
        }
    }

    // TODO: possibly generate description from effect data?
    val MUSHROOM = usableItem {
        desc {
            id = 6008
            name = "Mushroom"
            description = "WIL +2 for 60 seconds"
            textureName = "items/usable/mushroom.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                WILLPOWER +2
            })
        }
    }

    val BANANA = usableItem {
        desc {
            id = 6009
            name = "Banana"
            description = "VIT +1, ATK +3 for 60 seconds"
            textureName = "items/usable/banana.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                VITALITY +1
                ATK +3
            })
        }
    }

    val LEMON = usableItem {
        desc {
            id = 6010
            name = "Lemon"
            description = "DEX +1, AGI +1 for 60 seconds"
            textureName = "items/usable/lemon.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                DEXTERITY +1
                AGILITY +1
            })
        }
    }

    val PINEAPPLE = usableItem {
        desc {
            id = 6011
            name = "Pineapple"
            description = "STR +2 for 60 seconds"
            textureName = "items/usable/pineapple.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                STRENGTH +2
            })
        }
    }

    val WATERMELON = usableItem {
        desc {
            id = 6012
            name = "Watermelon"
            description = "WIS +1, PER +1 for 60 seconds"
            textureName = "items/usable/watermelon.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                WISDOM +1
                PERCEPTION +1
            })
        }
    }

    val CHEESE = usableItem {
        desc {
            id = 6013
            name = "Cheese"
            description = "INT +1, MAX SP +5 for 60 seconds"
            textureName = "items/usable/cheese.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                INTELLECT +1
                MAX_SP +5
            })
        }
    }

    val CARROT = usableItem {
        desc {
            id = 6014
            name = "Carrot"
            description = "PER +2 for 60 seconds"
            textureName = "items/usable/carrot.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                PERCEPTION +2
            })
        }
    }

    val STRAWBERRY = usableItem {
        desc {
            id = 6015
            name = "Strawberry"
            description = "LUC +2 for 60 seconds"
            textureName = "items/usable/strawberry.png"
        }

        onUseScript = {
            it.addEffect(effect(description) {
                duration = 1.0 * 60

                LUCK +2
            })
        }
    }
}

private class WeaponElementChangeEffect(val char: CharacterEntity, val element: Element, duration: Duration) : Effect(duration) {
    private lateinit var oldElement: Element

    override fun onStart(entity: Entity) {
        oldElement = char.characterComponent.weaponElement.value
        char.characterComponent.weaponElement.value = element
    }

    override fun onEnd(entity: Entity) {
        char.characterComponent.weaponElement.value = oldElement
    }
}