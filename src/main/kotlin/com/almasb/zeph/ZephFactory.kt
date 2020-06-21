package com.almasb.zeph

import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.spawn
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.IrremovableComponent
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.zeph.Config.Z_INDEX_CELL_SELECTION
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.ai.RandomWanderComponent
import com.almasb.zeph.character.char
import com.almasb.zeph.character.components.*
import com.almasb.zeph.combat.Element
import com.almasb.zeph.components.CellSelectionComponent
import com.almasb.zeph.components.PortalComponent
import com.almasb.zeph.data.Data
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
import com.almasb.zeph.item.*
import com.almasb.zeph.skill.Skill
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.function.Supplier

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    @Spawns("char")
    fun newCharacter(data: SpawnData): Entity {
        val charData = data.get<CharacterData>("charData")

        try {
            val entity = CharacterEntity()
            entity.x = data.x
            entity.y = data.y
            entity.type = MONSTER
            entity.localAnchor = Point2D(Config.SPRITE_SIZE / 2.0, Config.SPRITE_SIZE - 10.0)
            entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(Config.SPRITE_SIZE.toDouble(), Config.SPRITE_SIZE.toDouble())))

            with(entity) {
                addComponent(StateComponent())
                addComponent(EffectComponent())
                addComponent(NewCellMoveComponent(Config.TILE_SIZE, Config.TILE_SIZE, Config.CHAR_MOVE_SPEED))
                addComponent(NewAStarMoveComponent(LazyValue(Supplier { FXGL.getAppCast<ZephyriaApp>().grid })))

                addComponent(AnimationComponent(charData.description.textureName))
                addComponent(CharacterComponent(charData))
                addComponent(CharacterActionComponent())

                addComponent(CharacterChildViewComponent())
                addComponent(RandomWanderComponent())
            }

            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw RuntimeException("Failed to create char: $data")
    }

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        val player = newCharacter(data)
        player.type = PLAYER
        player.removeComponent(RandomWanderComponent::class.java)
        player.addComponent(PlayerComponent())
        player.addComponent(IrremovableComponent())
        return player
    }

    @Spawns("item")
    fun newItem(data: SpawnData): Entity {
        val itemData = data.get<ItemData>("itemData")

        // TODO: this is where we parse everything related to item
        if (itemData is UsableItemData) {
            data.put("usable", UsableItem(itemData))
        } else if (itemData is WeaponData) {
            data.put("weapon", Weapon(itemData))
        } else if (itemData is ArmorData) {
            data.put("armor", Armor(itemData))
        }
        return FXGL.entityBuilder(data)
                .view(itemData.description.textureName)
                .build()
    }

    @Spawns("nav")
    fun newWalkableCell(data: SpawnData): Entity {
        return entityBuilder(data)
                .type(NAV)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .view(Rectangle(data.get("width"), data.get("height"), Color.TRANSPARENT))
                .onClick {
//                    if (selectingSkillTargetArea) {
//                        useAreaSkill()
//                        selectingSkillTargetArea = false
//                        selectedSkillIndex = -1
//                        return
//                    }

                    //selected.set(null)

                    val targetX = (FXGL.getInput().mouseXWorld / Config.TILE_SIZE).toInt()
                    val targetY = (FXGL.getInput().mouseYWorld / Config.TILE_SIZE).toInt()

                    getGameWorld().getSingleton(PLAYER)
                            .getComponent(CharacterActionComponent::class.java)
                            .orderMove(targetX, targetY)
                }
                .build()
    }

    @Spawns("portal")
    fun newPortal(data: SpawnData): Entity {
        return entityBuilder(data)
                .type(PORTAL)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .with(PortalComponent(data.get("mapName"), data.get("toCellX"), data.get("toCellY")))
                .build()
    }

    @Spawns("cellSelection")
    fun newCellSelection(data: SpawnData): Entity {
        val e = entityBuilder(data)
                .view(Rectangle(Config.TILE_SIZE * 1.0, Config.TILE_SIZE * 1.0, null).also { it.stroke = Color.BLACK })
                .zIndex(Z_INDEX_CELL_SELECTION)
                .with(CellSelectionComponent())
                .with(IrremovableComponent())
                .build()

        e.viewComponent.parent.isMouseTransparent = true

        return e
    }
}

fun newPlayer(): CharacterEntity {
    val charData = char {
        desc {
            id = 1
            name = "Developer"
            description = "It's you! $name"
            textureName = "chars/players/player_full.png"
        }

        charClass = CharacterClass.NOVICE

        attributes {
            str = 1
            vit = 1
            dex = 1
            agi = 1
            int = 1
            wis = 1
            wil = 1
            per = 1
            luc = 1
        }
    }

    val player = spawn("player", SpawnData(0.0, 0.0).put("charData", charData)) as CharacterEntity

    // TODO: TEST DATA BEGIN

    player.characterComponent.skills += Skill(Data.Skills.Warrior.ROAR)

    player.inventory.items.add(newDagger(Element.NEUTRAL))
    player.inventory.items.add(newDagger(Element.FIRE))
    player.inventory.items.add(newDagger(Element.EARTH))
    player.inventory.items.add(newDagger(Element.AIR))
    player.inventory.items.add(newDagger(Element.WATER))

    // TEST DATA END

    return player
}

private fun newDagger(element: Element): Weapon {
    val weapon = Weapon(Data.Weapons.Daggers.KNIFE)
    weapon.element.set(element)
    return weapon
}