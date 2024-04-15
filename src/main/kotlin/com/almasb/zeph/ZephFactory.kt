package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.collection.grid.Cell
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.ExpireCleanComponent
import com.almasb.fxgl.dsl.components.LiftComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.entity.components.IDComponent
import com.almasb.fxgl.entity.components.IrremovableComponent
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.particle.ParticleComponent
import com.almasb.fxgl.particle.ParticleEmitters
import com.almasb.fxgl.pathfinding.CellMoveComponent
import com.almasb.fxgl.pathfinding.CellState
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxgl.procedural.HeightMapGenerator
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.fxgl.texture.Texture
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.Config.Z_INDEX_CELL_SELECTION
import com.almasb.zeph.Config.Z_INDEX_DECOR_ABOVE_PLAYER
import com.almasb.zeph.Config.Z_INDEX_DECOR_BELOW_PLAYER
import com.almasb.zeph.EntityType.*
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
import com.almasb.zeph.ui.TooltipView
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.geometry.Rectangle2D
import javafx.scene.ImageCursor
import javafx.scene.control.Button
import javafx.scene.effect.BlendMode
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.util.function.Supplier
import kotlin.math.roundToInt

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    // TODO: use .tmx object or similar to place NPCs, so they are visible in Tiled
    @Spawns("npc")
    fun newNPC(data: SpawnData): Entity {
        val cellX = data.get<Int>("cellX")
        val cellY = data.get<Int>("cellY")

        val npcData = data.get<NPCData>("npcData")

        val charData = char {
            desc {
                id = npcData.description.id
                name = npcData.description.name
                description = npcData.description.description
                textureName = npcData.description.textureName
            }

            charClass = CharacterClass.NOVICE

            attributes {
                Attribute.values().forEach {
                    it +1
                }
            }
        }

        data.put("charData", charData)

        // TODO: separate newMonster to newCharacter?
        val npc = newMonster(data) as CharacterEntity

        npc.setProperty("id", npcData.description.id)

        // TODO: duplicate here as in newMonster
        data.data.forEach { npc.setProperty(it.key, it.value) }

        npc.type = NPC
        npc.removeComponent(RandomWanderComponent::class.java)

        with(npc) {
            addComponent(IDComponent("NPC", npcData.description.id))
            addComponent(NPCFollowComponent())
            addComponent(NPCChildViewComponent(npcData.description.name))
        }

        val entity = npc

        entity.localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
        entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

        entity.getComponent(AStarMoveComponent::class.java).stopMovementAt(cellX, cellY)

        entity.viewComponent.parent.cursor = ImageCursor(image("ui/chat.png"), 16.0, 16.0)

        entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
            Gameplay.player.actionComponent.orderStartDialogue(npc)
        })

        runOnce({
            entity.addComponent(LiftComponent().yAxisDistanceDuration(1.0, Duration.seconds(0.4)))
        }, Duration.seconds(random(0.01, 1.0)))

        return entity
    }

    @Spawns("monster")
    fun newMonster(data: SpawnData): Entity {
        val charData = data.get<CharacterData>("charData")

        val cellX = data.get<Int>("cellX")
        val cellY = data.get<Int>("cellY")

        val entity = CharacterEntity()

        data.data.forEach { entity.setProperty(it.key, it.value) }

        entity.type = MONSTER
        entity.localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
        entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

        with(entity) {
            addComponent(CollidableComponent(true))
            addComponent(StateComponent())
            addComponent(CharacterEffectComponent())
            addComponent(CellMoveComponent(TILE_SIZE, TILE_SIZE, Config.CHAR_MOVE_SPEED))
            addComponent(AStarMoveComponent(LazyValue(Supplier { Gameplay.currentMap.grid })))

            addComponent(AnimationComponent(charData.description.textureName))
            addComponent(CharacterComponent(charData))
            addComponent(CharacterActionComponent())

            addComponent(CharacterChildViewComponent())
            addComponent(RandomWanderComponent())
        }

        entity.setPositionToCell(cellX, cellY)

        entity.viewComponent.parent.cursor = ImageCursor(image("ui/cursors/attack.png"), 1.0, 1.0)

        // TODO: do not allow click if dying ...

        entity.viewComponent.addOnClickHandler {
            val player = getGameWorld().getSingleton(PLAYER) as CharacterEntity

            // TODO: handle differently?
            if (player === entity)
                return@addOnClickHandler

            if (entity.isType(MONSTER))
                player.actionComponent.orderAttack(entity)
        }

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay()

        return entity
    }

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
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
        player.addComponent(PlayerWorldComponent())
        player.addComponent(IrremovableComponent())

        player.viewComponent.parent.isMouseTransparent = true

        // add dev stuff if not on release
        if (!isReleaseMode()) {
            player.characterComponent.skills += Skill(Data.Skills.Warrior.ROAR)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.ARMOR_MASTERY)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.MIGHTY_SWING)
            player.characterComponent.skills += Skill(Data.Skills.Warrior.WARRIOR_HEART)

            player.characterComponent.skills += Skill(Data.Skills.Gladiator.DOUBLE_EDGE)
            player.characterComponent.skills += Skill(Data.Skills.Gladiator.BASH)
            player.characterComponent.skills += Skill(Data.Skills.Gladiator.BLOODLUST)
            player.characterComponent.skills += Skill(Data.Skills.Gladiator.ENDURANCE)
            player.characterComponent.skills += Skill(Data.Skills.Gladiator.SHATTER_ARMOR)

            player.inventory.add(newDagger(Element.NEUTRAL))
            player.inventory.add(newDagger(Element.FIRE))
            player.inventory.add(newDagger(Element.EARTH))
            player.inventory.add(newDagger(Element.AIR))
            player.inventory.add(newDagger(Element.WATER))

            player.inventory.add(UsableItem(Data.UsableItems.MANA_POTION))
            player.inventory.add(UsableItem(Data.UsableItems.HEALING_POTION))

            player.inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
            player.inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
            player.inventory.add(Weapon(Data.Weapons.OneHandedSwords.GUARD_SWORD))

            repeat(100) {
                player.inventory.add(UsableItem(Data.UsableItems.TREASURE_BOX))
            }

            player.inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))
            player.inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))
            player.inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))

            player.inventory.add(Armor(Data.Armors.Body.TRAINING_ARMOR))

            player.inventory.add(MiscItem(Data.MiscItems.SILVER_INGOT))
        }

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
        } else if (itemData is MiscItemData) {
            data.put("misc", MiscItem(itemData))
        }

        val e = entityBuilder(data)
                .type(ITEM)
                .view(itemData.description.textureName)
                .zIndex(Z_INDEX_DECOR_BELOW_PLAYER)
                .build()

        e.viewComponent.parent.isPickOnBounds = true

        e.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {

            // TODO: if close
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

    // TODO: check fxgl can load margin-based tiles correctly
    // TODO: allow tmx / fxgl to use standalone animated textures
    // should tmx parsing be done in from(data) so that devs can make use of the tmx view in this function?
    @Spawns("anim_tree")
    fun newAnimatedTree(data: SpawnData): Entity {
        val texture = texture("animated_tree.png").subTexture(Rectangle2D(0.0, 0.0, 100.0, 32.0))

        return entityBuilder(data)
                .onActive { it.addComponent(AnimatedTreeComponent(texture)) }
                .build()
    }

    @Spawns("camp_fire")
    fun newAnimatedFlame(data: SpawnData): Entity {
        val channel = AnimationChannel(
                image("camp_fire.png"),
                Duration.seconds(0.8),
                5
        )

        val texture = AnimatedTexture(channel).loop()
        texture.translateX = -TILE_SIZE * 1.0
        texture.translateY = -TILE_SIZE * 1.5

        return entityBuilder(data)
                .zIndex(Z_INDEX_DECOR_ABOVE_PLAYER)
                .onActive {
                    it.viewComponent.clearChildren()
                    it.viewComponent.addChild(texture)
                }
                .build()
    }

    @Spawns("smoke")
    fun newSmoke(data: SpawnData): Entity {
        val t = FXGL.texture("particles/smoke.png", 128.0, 128.0).brighter().brighter()

        val emitter = ParticleEmitters.newFireEmitter()
        emitter.blendMode = BlendMode.SRC_OVER
        emitter.setSourceImage(t.getImage())
        emitter.setSize(TILE_SIZE / 4.0, TILE_SIZE / 2.0)
        emitter.numParticles = 10
        emitter.emissionRate = 0.02
        emitter.setVelocityFunction { i -> Point2D(FXGL.random() * 2.5, -FXGL.random() * FXGL.random(30, 50)) }
        emitter.setExpireFunction { i -> Duration.seconds(FXGL.random(2, 4).toDouble()) }
        emitter.setScaleFunction { i -> Point2D(0.15, 0.10) }
        //emitter.setSpawnPointFunction { i -> Point2D(FXGL.random(0.0, appWidth - 200.0), 120.0) }

        val comp = ParticleComponent(emitter)

        return entityBuilder(data)
                .zIndex(Z_INDEX_DECOR_ABOVE_PLAYER)
                .with(comp)
                .onActive {
                    it.viewComponent.parent.isMouseTransparent = true

                    comp.parent.zIndex = Z_INDEX_DECOR_ABOVE_PLAYER
                    comp.parent.viewComponent.parent.isMouseTransparent = true
                }
                .build()
    }

    @Spawns("level_up")
    fun newLevelUpAnim(data: SpawnData): Entity {
        val channel = AnimationChannel(image("level_up_anim.png"),
                framesPerRow = 4,
                frameWidth = 128, frameHeight = 128,
                channelDuration = Duration.seconds(1.0),
                startFrame = 0, endFrame = 15
        )

        return entityBuilder(data)
                .at(data.x - 64.0, data.y - 64.0)
                .view(AnimatedTexture(channel).play())
                .with(ExpireCleanComponent(Duration.seconds(1.0)))
                .zIndex(Z_INDEX_DECOR_ABOVE_PLAYER)
                .build()
    }

    /**
     * Only spawned dynamically. This is not currently spawned from .tmx file.
     */
    @Spawns("treasureChest")
    fun newTreasureChest(data: SpawnData): Entity {
//        val cellX = if (data.hasKey("cellX")) data.get<Int>("cellX") else coordToCell(data.x)
//        val cellY = if (data.hasKey("cellY")) data.get<Int>("cellY") else coordToCell(data.y)

        val cellX = data.get<Int>("cellX")
        val cellY = data.get<Int>("cellY")

        val gold = if (data.hasKey("gold")) data.get<Int>("gold") else 1

        val cell = Gameplay.currentMap.grid.get(cellX, cellY)
        cell.state = CellState.NOT_WALKABLE

        return entityBuilder(data)
                .type(TREASURE_CHEST)
                .viewWithBBox("chest_closed.png")
                .collidable()
                .with("cell", cell)
                .onClick {

                    if (Gameplay.player.distance(cellX, cellY) < 2) {
                        Gameplay.addMoney(gold)

                        it.viewComponent.clearChildren()
                        it.viewComponent.addChild(texture("chest_open_full.png"))
                        it.addComponent(ExpireCleanComponent(Duration.seconds(1.0)).animateOpacity())
                    }
                }
                .onNotActive { cell.state = CellState.WALKABLE }
                .build()
    }

    @Spawns("textTriggerBox")
    fun newTextTriggerBox(data: SpawnData): Entity {
        return entityBuilder(data)
                .type(TEXT_TRIGGER_BOX)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .collidable()
                .build()
    }

    @Spawns("textBox")
    fun newTextBox(data: SpawnData): Entity {
        val text = data.get<String>("text")

        val tooltip = TooltipView(150.0)
        tooltip.setNode(getUIFactoryService().newTextFlow().append(text, Color.AQUAMARINE, 11.0))

        val button = Button("X")

        val stack = StackPane(tooltip, button)
        stack.alignment = Pos.TOP_RIGHT

        val e = entityBuilder(data)
                .view(stack)
                .zIndex(Z_INDEX_DECOR_ABOVE_PLAYER)
                .with(LiftComponent().yAxisDistanceDuration(10.0, Duration.seconds(2.0)))
                .build()

        button.setOnAction { e.removeFromWorld() }

        return e
    }

    @Spawns("dialogue")
    fun newDialogue(data: SpawnData): Entity {
        val text = data.get<String>("text")

        return entityBuilder(data)
                .type(DIALOGUE_TRIGGER_BOX)
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .collidable()
                .build()
    }
}

private class AnimatedTreeComponent(private val texture: Texture) : Component() {
    override fun onAdded() {
        entity.viewComponent.clearChildren()
        entity.viewComponent.addChild(texture.toAnimatedTexture(3, Duration.seconds(0.5)).loop())
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

fun coordToCell(value: Double): Int = (value / Config.TILE_SIZE).roundToInt()