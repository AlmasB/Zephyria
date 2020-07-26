package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.cutscene.Cutscene
import com.almasb.fxgl.cutscene.dialogue.DialogueGraphSerializer
import com.almasb.fxgl.cutscene.dialogue.SerializableGraph
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.entity.components.IrremovableComponent
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.pathfinding.Cell
import com.almasb.fxgl.pathfinding.CellMoveComponent
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxgl.procedural.HeightMapGenerator
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.SKILL_PROJECTILE_SPEED
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.Config.Z_INDEX_CELL_SELECTION
import com.almasb.zeph.Config.Z_INDEX_DECOR_BELOW_PLAYER
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_CHAR
import com.almasb.zeph.Vars.SELECTED_SKILL_INDEX
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.ai.RandomWanderComponent
import com.almasb.zeph.character.char
import com.almasb.zeph.character.components.*
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Element
import com.almasb.zeph.components.CellSelectionComponent
import com.almasb.zeph.components.PortalComponent
import com.almasb.zeph.data.Data

import com.almasb.zeph.item.*
import com.almasb.zeph.skill.Skill
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.ImageCursor
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.util.function.Supplier

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    @Spawns("npc")
    fun newNPC(data: SpawnData): Entity {
        val cellX = data.get<Int>("cellX")
        val cellY = data.get<Int>("cellY")

        val npcData = data.get<NPCData>("npcData")

        val entity = entityBuilder()
                .type(NPC)
                .with(CellMoveComponent(TILE_SIZE, TILE_SIZE, Config.CHAR_MOVE_SPEED))
                .with(AStarMoveComponent(LazyValue(Supplier { Gameplay.getCurrentMap().grid })))
                .with(AnimationComponent(npcData.description.textureName))
                .build()

        entity.localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
        entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

        entity.getComponent(AStarMoveComponent::class.java).stopMovementAt(cellX, cellY)

        entity.viewComponent.parent.cursor = ImageCursor(image("ui/chat.png"), 16.0, 16.0)

        entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
            // TODO: order player to move/talk to NPC

            val fullTexture = texture(npcData.textureNameFull, 948.0 * 0.25, 1920.0 * 0.25).outline(Color.BLACK, 2)

            addUINode(fullTexture, getAppWidth() - fullTexture.width, getAppHeight() - fullTexture.height)

            runOnce({
                removeUINode(fullTexture)
            }, Duration.seconds(0.05))

            val graph = getAssetLoader().loadDialogueGraph(npcData.dialogueName.removePrefix("dialogues/"))

            getCutsceneService().startDialogueScene(graph, Gameplay, Runnable { removeUINode(fullTexture) })
        })

        return entity
    }

    @Spawns("monster")
    fun newMonster(data: SpawnData): Entity {
        val charData = data.get<CharacterData>("charData")

        try {
            val cellX = data.get<Int>("cellX")
            val cellY = data.get<Int>("cellY")

            val entity = CharacterEntity()
            entity.type = MONSTER
            entity.localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
            entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

            with(entity) {
                addComponent(CollidableComponent(true))
                addComponent(StateComponent())
                addComponent(CharacterEffectComponent())
                addComponent(CellMoveComponent(TILE_SIZE, TILE_SIZE, Config.CHAR_MOVE_SPEED))
                addComponent(AStarMoveComponent(LazyValue(Supplier { Gameplay.getCurrentMap().grid })))

                addComponent(AnimationComponent(charData.description.textureName))
                addComponent(CharacterComponent(charData))
                addComponent(CharacterActionComponent())

                addComponent(CharacterChildViewComponent())
                addComponent(RandomWanderComponent())
            }

            entity.setPositionToCell(cellX, cellY)

            // TODO: convenience methods on mouse click
            entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {

                val player = getGameWorld().getSingleton(PLAYER) as CharacterEntity

                // TODO: handle differently?
                if (player === entity)
                    return@EventHandler

                // TODO: check for skill range
                if (getb(IS_SELECTING_SKILL_TARGET_CHAR)) {
                    set(IS_SELECTING_SKILL_TARGET_CHAR, false)

                    player.actionComponent.orderSkillCast(geti(SELECTED_SKILL_INDEX), entity)
                } else {
                    player.actionComponent.orderAttack(entity)
                }
            })

            animationBuilder()
                    .fadeIn(entity)
                    .buildAndPlay()

            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw RuntimeException("Failed to create char: $data")
    }

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        try {
            val charData = char {
                desc {
                    id = 1
                    name = "Developer"
                    description = "It's you! $name"
                    textureName = "chars/players/dev_player.png"
                }

                charClass = CharacterClass.NOVICE

                attributes {
                    Attribute.values().forEach {
                        it +1
                    }
                }
            }

            data.put("cellX", 0)
            data.put("cellY", 0)
            data.put("charData", charData)

            // TODO: separate newMonster to newCharacter?
            val player = newMonster(data) as CharacterEntity
            player.type = PLAYER
            player.removeComponent(RandomWanderComponent::class.java)
            player.addComponent(PlayerComponent())
            player.addComponent(IrremovableComponent())

            player.viewComponent.parent.isMouseTransparent = true

            // TODO: TEST DATA BEGIN

            player.characterComponent.skills += Skill(Data.Skills.Warrior.ROAR)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.ARMOR_MASTERY)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.MIGHTY_SWING)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.WARRIOR_HEART)

            player.inventory.add(newDagger(Element.NEUTRAL))
            player.inventory.add(newDagger(Element.FIRE))
            player.inventory.add(newDagger(Element.EARTH))
            player.inventory.add(newDagger(Element.AIR))
            player.inventory.add(newDagger(Element.WATER))

            player.inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
            player.inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
            player.inventory.add(Weapon(Data.Weapons.OneHandedSwords.GUARD_SWORD))
            player.inventory.add(UsableItem(Data.UsableItems.TREASURE_BOX))

            player.inventory.add(Armor(Data.Armors.Body.TRAINING_ARMOR))

            player.inventory.add(MiscItem(Data.MiscItems.SKELETON_BONE))

            // TEST DATA END

            return player
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw RuntimeException("Failed to create player: $data")
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
        } else if (itemData is MiscItemData) {
            data.put("misc", MiscItem(itemData))
        }

        val e = entityBuilder(data)
                .view(itemData.description.textureName)
                .zIndex(Z_INDEX_DECOR_BELOW_PLAYER)
                .build()

        e.viewComponent.parent.isPickOnBounds = true

        e.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {

            val player = getGameWorld().getSingleton(PLAYER) as CharacterEntity

            player.actionComponent.orderPickUp(e)
        })

        animationBuilder()
                .repeat(2)
                .autoReverse(true)
                .duration(Duration.seconds(0.3))
                .interpolator(Interpolators.SMOOTH.EASE_IN())
                .translate(e)
                .from(e.position)
                .to(e.position.add(0.0, -25.0))
                .buildAndPlay()

        return e
    }

    @Spawns("nav")
    fun newWalkableCell(data: SpawnData): Entity {
        val e = entityBuilder(data)
                .type(NAV)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .onClick {
//                    if (selectingSkillTargetArea) {
//                        useAreaSkill()
//                        selectingSkillTargetArea = false
//                        selectedSkillIndex = -1
//                        return
//                    }

                    //selected.set(null)

                    val targetX = (FXGL.getInput().mouseXWorld / TILE_SIZE).toInt()
                    val targetY = (FXGL.getInput().mouseYWorld / TILE_SIZE).toInt()

                    getGameWorld().getSingleton(CELL_SELECTION)
                            .getComponent(CellSelectionComponent::class.java)
                            .onClick()

                    getGameWorld().getSingleton(PLAYER)
                            .getComponent(CharacterActionComponent::class.java)
                            .orderMove(targetX, targetY)
                }
                .build()

        // Dynamic patches on tiles

//        val size = TILE_SIZE / 16
//
//        val W = data.get<Double>("width").toInt() / size
//        val H = data.get<Double>("height").toInt() / size
//
//        val buffer = WritableImage(size * W, size * H)
//
//        val map = Grid(HeightMapGenerator.HeightData::class.java, W, H, CustomHeightMapGenerator(0, e.x, e.y, W, H))
//
//        map.forEach { cell: HeightMapGenerator.HeightData ->
//
//            val maxDistance =  (W / 2 + H / 2)
//            val opacity = (1 - cell.distance(W / 2, H / 2) * 1.0 / maxDistance) * 0.4
//
//            // data.get<Color>("color")
//            var color = if (data.hasKey("color")) Color.rgb(77, 146, 98) else Color.rgb(194, 152, 109)
//
//            color = Color.color(color.red, color.green, color.blue, opacity)
//
//            val texture = if (cell.height < 0.2) {
//                // water
//                ColoredTexture(size, size, Color.TRANSPARENT)
//            } else if (cell.height < 0.5) {
//                // grass
//                ColoredTexture(size, size, color.darker())
//            } else if (cell.height < 0.7) {
//                // grass
//                ColoredTexture(size, size, color)
//            } else {
//                // in-land grass / mud?
//                ColoredTexture(size, size, color.brighter())
//            }
//
//            buffer.pixelWriter.setPixels(cell.x * size, cell.y * size, size, size, texture.image.pixelReader, 0, 0)
//        }
//
//        e.viewComponent.addChild(ImageView(buffer))

        // TODO: this is not necessary if we make all layers but 1 UI transparent and use
        // the only layer to handle movement clicks

        e.viewComponent.addChild(Rectangle(data.get("width"), data.get("height"), Color.TRANSPARENT))

        return e
    }

    private fun Cell.distance(cellX: Int, cellY: Int): Int {
        return Math.abs(x - cellX) + Math.abs(y - cellY)
    }

    @Spawns("portal")
    fun newPortal(data: SpawnData): Entity {
        val interactionCollisionBox = Rectangle2D(
                data.x + TILE_SIZE, data.y + TILE_SIZE / 2,
                data.get<Double>("width") - TILE_SIZE *2, data.get<Double>("height") - TILE_SIZE
        )

        return entityBuilder(data)
                .type(PORTAL)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .with("interactionCollisionBox", interactionCollisionBox)
                .with(PortalComponent(data.get("mapName"), data.get("toCellX"), data.get("toCellY")))
                .build()
    }

    @Spawns("cellSelection")
    fun newCellSelection(data: SpawnData): Entity {
        val view = Rectangle(TILE_SIZE * 1.0, TILE_SIZE * 1.0, null)
        view.stroke = Color.BLACK

        animationBuilder()
                .repeatInfinitely()
                .autoReverse(true)
                .interpolator(Interpolators.CIRCULAR.EASE_OUT())
                .animate(view.strokeProperty())
                .from(Color.color(0.0, 0.0, 0.0, 0.7))
                .to(Color.color(1.0, 0.84313726, 0.0, 0.7))
                .buildAndPlay()

        val e = entityBuilder(data)
                .type(CELL_SELECTION)
                .view(view)
                .zIndex(Z_INDEX_CELL_SELECTION)
                .with(CellSelectionComponent())
                .with(IrremovableComponent())
                .build()

        e.viewComponent.parent.isMouseTransparent = true

        return e
    }

    @Spawns("skillProjectile")
    fun newSkillProjectile(data: SpawnData): Entity {
        return entityBuilder(data)
                .type(SKILL_PROJECTILE)
                .viewWithBBox(data.get<String>("projectileTextureName"))
                .collidable()
                .with(ProjectileComponent(data.get("dir"), SKILL_PROJECTILE_SPEED))
                .build()
    }
}

private class CustomHeightMapGenerator(val seed: Int, val tx: Double, val ty: Double, width: Int, height: Int) : HeightMapGenerator(width, height) {

    private val random = FXGLMath.getRandom(seed.toLong())

    private val multiplier = random.nextInt(19000) + 1

    override fun apply(x: Int, y: Int): HeightData {
        val nx = tx / (MAP_WIDTH * TILE_SIZE) + x * 1.0 / width - 0.5
        val ny = ty / (MAP_HEIGHT * TILE_SIZE) + y * 1.0 / height - 0.5

        var noiseValue = (FXGLMath.noise2D(nx, ny)
                + 0.5 * FXGLMath.noise2D(20 * nx, 20 * ny))

        noiseValue *= noiseValue

        if (noiseValue < 0) {
            noiseValue = 0.0
        }

        if (noiseValue > 1) {
            noiseValue = 1.0
        }

        return HeightData(x, y, noiseValue)
    }
}

private fun newDagger(element: Element): Weapon {
    val weapon = Weapon(Data.Weapons.Daggers.KNIFE)
    weapon.element.set(element)
    return weapon
}