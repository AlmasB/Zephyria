package com.almasb.zeph.data

import com.almasb.fxgl.dsl.components.Effect
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.dsl.getAssetLoader
import com.almasb.fxgl.dsl.getDialogService
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.Gameplay
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Attribute.*
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.GameMath
import com.almasb.zeph.combat.Stat.ATK
import com.almasb.zeph.combat.Stat.MAX_SP
import com.almasb.zeph.combat.effect
import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.Weapon
import com.almasb.zeph.item.usableItem
import com.almasb.zeph.pushMessage
import javafx.scene.control.TextArea
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
            textureName = "items/usable/healing_potion.png"
        }

        useSoundName = "bottle.wav"
        
        restoreHP(50.0)
    }

    val MANA_POTION = usableItem {
        desc {
            id = 6001
            name = "Mana Potion"
            textureName = "items/usable/mana_potion.png"
        }

        useSoundName = "bottle.wav"

        restoreSP(30.0)
    }

    val TREASURE_BOX = usableItem {
        desc {
            id = 6002
            name = "Treasure Box"
            description = "An old box that may contain valuable items. Gives a random equipment item when opened."
            textureName = "items/usable/treasure_box.png"
        }

        useSoundName = "treasure_box.wav"

        beforeUseScript = {
            val isInventoryFull = it.inventory.isFull

            if (isInventoryFull) {
                pushMessage("Cannot open treasure box. Inventory is full.")
            }

            !isInventoryFull
        }

        onUseScript {
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

        onUseScript {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.FIRE, Duration.minutes(2.0)))
        }
    }

    val WATER_MIXTURE = usableItem {
        desc {
            id = 6004
            name = "Water Mixture"
            description = "Imbues the equipped weapon with the WATER element for 2 minutes"
            textureName = "items/usable/water_mixture.png"
        }

        onUseScript {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.WATER, Duration.minutes(2.0)))
        }
    }

    val AIR_MIXTURE = usableItem {
        desc {
            id = 6005
            name = "Air Mixture"
            description = "Imbues the equipped weapon with the AIR element for 2 minutes"
            textureName = "items/usable/air_mixture.png"
        }

        onUseScript {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.AIR, Duration.minutes(2.0)))
        }
    }

    val EARTH_MIXTURE = usableItem {
        desc {
            id = 6006
            name = "Earth Mixture"
            description = "Imbues the equipped weapon with the EARTH element for 2 minutes"
            textureName = "items/usable/earth_mixture.png"
        }

        onUseScript {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.EARTH, Duration.minutes(2.0)))
        }
    }

    val TELEPORTATION_STONE = usableItem {
        desc {
            id = 6007
            name = "Teleportation Stone"
            description = "Teleports the stone user to a random location on the map."
            textureName = "items/usable/teleportation_stone.png"
        }

        useSoundName = "teleportation_stone.wav"

        onUseScript {
            val grid = Gameplay.currentMap.grid

            val cell = grid.getRandomCell { it.isWalkable }.get()

            it.actionComponent.orderIdle();
            it.setPositionToCell(cell.x, cell.y)
        }
    }

    val MUSHROOM = usableItem {
        desc {
            id = 6008
            name = "Mushroom"
            textureName = "items/usable/mushroom.png"
        }

        addEffect {
            duration = 60.0

            WILLPOWER +2
        }
    }

    val BANANA = usableItem {
        desc {
            id = 6009
            name = "Banana"
            textureName = "items/usable/banana.png"
        }

        addEffect {
            duration = 1.0 * 60

            VITALITY +1
            ATK +3
        }
    }

    val LEMON = usableItem {
        desc {
            id = 6010
            name = "Lemon"
            textureName = "items/usable/lemon.png"
        }

        addEffect {
            duration = 1.0 * 60

            DEXTERITY +1
            AGILITY +1
        }
    }

    val PINEAPPLE = usableItem {
        desc {
            id = 6011
            name = "Pineapple"
            textureName = "items/usable/pineapple.png"
        }

        addEffect {
            duration = 1.0 * 60

            STRENGTH +2
        }
    }

    val WATERMELON = usableItem {
        desc {
            id = 6012
            name = "Watermelon"
            textureName = "items/usable/watermelon.png"
        }

        addEffect {
            duration = 1.0 * 60

            WISDOM +1
            PERCEPTION +1
        }
    }

    val CHEESE = usableItem {
        desc {
            id = 6013
            name = "Cheese"
            textureName = "items/usable/cheese.png"
        }

        addEffect {
            duration = 1.0 * 60

            INTELLECT +1
            MAX_SP +5
        }
    }

    val CARROT = usableItem {
        desc {
            id = 6014
            name = "Carrot"
            textureName = "items/usable/carrot.png"
        }

        addEffect {
            duration = 1.0 * 60

            PERCEPTION +2
        }
    }

    val STRAWBERRY = usableItem {
        desc {
            id = 6015
            name = "Strawberry"
            textureName = "items/usable/strawberry.png"
        }

        addEffect {
            duration = 1.0 * 60

            LUCK +2
        }
    }

    val HEALING_HERBS = usableItem {
        desc {
            id = 6016
            name = "Healing Herbs"
            textureName = "items/usable/healing_herbs.png"
        }

        restoreHP(10.0)
    }

    val COOKED_MEAT = usableItem {
        desc {
            id = 6017
            name = "Cooked Meat"
            description = "A piece of deliciously cooked meat."
            textureName = "items/usable/cooked_meat.png"
        }

        restoreHP(35.0)

        addEffect {
            duration = 2.0 * 60

            STRENGTH +2
            VITALITY +2
        }
    }





    // custom

    val CODEFEST_CHALLENGE1 = usableItem {
        desc {
            id = 6997
            name = "Codefest Challenge 1"
            description = "A mysterious parchment with ancient text. Read it!"
            textureName = "items/usable/parchment.png"
        }

        isPermanentUse = true

        onUseScript {
            val lines = getAssetLoader().loadText("challenge1.txt")

            getDialogService().showMessageBox(
                    lines.fold("") { acc, item -> acc + "\n" + item }
            )
        }
    }

    val CODEFEST_CHALLENGE2 = usableItem {
        desc {
            id = 6998
            name = "Codefest Challenge 2"
            description = "A mysterious parchment with ancient text. Read it!"
            textureName = "items/usable/parchment.png"
        }

        isPermanentUse = true

        onUseScript {
            val lines = getAssetLoader().loadText("challenge2.txt")

            if (lines.size == 3) {
                val textArea = TextArea(lines[0] + "\n" + lines[2])
                textArea.prefWidth = 400.0
                textArea.isWrapText = true

                getDialogService().showBox("Message", textArea, getUIFactoryService().newButton("OK"))

            } else {
                getDialogService().showMessageBox("Encountered a bug, please report to Almas")
            }
        }
    }

    val CODEFEST_CHALLENGE3 = usableItem {
        desc {
            id = 6999
            name = "Codefest Challenge 3"
            description = "A mysterious parchment with ancient text. Read it!"
            textureName = "items/usable/parchment.png"
        }

        isPermanentUse = true

        onUseScript {
            val lines = getAssetLoader().loadText("challenge3.txt")

            getDialogService().showMessageBox(
                    lines.fold("") { acc, item -> acc + "\n" + item }
            )
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