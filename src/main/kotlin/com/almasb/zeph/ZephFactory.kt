package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
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
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.fxgl.texture.Texture
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.Config.Z_INDEX_CELL_SELECTION
import com.almasb.zeph.Config.Z_INDEX_DECOR_ABOVE_PLAYER
import com.almasb.zeph.Config.Z_INDEX_DECOR_BELOW_PLAYER
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.char
import com.almasb.zeph.character.components.*
import com.almasb.zeph.character.npc.NPCChildViewComponent
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.character.npc.NPCFollowComponent
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

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    @Spawns("monster")
    fun newMonster(data: SpawnData): Entity {
        val mob = newCharacter(data) as CharacterEntity
        with(mob) {
            type = MONSTER

            addComponent(RandomWanderComponent())

            viewComponent.parent.cursor = ImageCursor(image("ui/cursors/attack.png"), 1.0, 1.0)

            viewComponent.addOnClickHandler {
                if (isDying)
                    return@addOnClickHandler

                val player = getGameWorld().getSingleton(PLAYER) as CharacterEntity

                player.actionComponent.orderAttack(mob)
            }
        }

        return mob
    }

    // TODO: use .tmx object or similar to place NPCs, so they are visible in Tiled
    @Spawns("npc")
    fun newNPC(data: SpawnData): Entity {
        val npcData = data.get<NPCData>("npcData")

        data.put("charData", npcData.toCharData())

        val npc = newCharacter(data) as CharacterEntity
        with(npc) {
            type = NPC

            setProperty("id", npcData.description.id)

            addComponent(IDComponent("NPC", npcData.description.id))
            addComponent(NPCFollowComponent())
            addComponent(NPCChildViewComponent(npcData.description.name))

            viewComponent.parent.cursor = ImageCursor(image("ui/chat.png"), 16.0, 16.0)

            viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
                getNotificationService().pushNotification("Dialogues are not supported yet")
                // TODO:
                //Gameplay.player.actionComponent.orderStartDialogue(npc)
            })

            // add a random delay, so all NPCs have their own cycle
            runOnce({
                addComponent(LiftComponent().yAxisDistanceDuration(1.0, Duration.seconds(0.4)))
            }, Duration.seconds(random(0.01, 1.0)))
        }

        return npc
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

        val player = newCharacter(data) as CharacterEntity
        with(player) {
            type = PLAYER

            addComponent(PlayerComponent())
            addComponent(PlayerWorldComponent())
            addComponent(IrremovableComponent())

            viewComponent.parent.isMouseTransparent = true

            val addDevStuff = false

            if (addDevStuff) {
                characterComponent.skills += Skill(Data.Skills.Warrior.ROAR)
                characterComponent.skills += Skill(Data.Skills.Warrior.ARMOR_MASTERY)
                characterComponent.skills += Skill(Data.Skills.Warrior.MIGHTY_SWING)
                characterComponent.skills += Skill(Data.Skills.Warrior.WARRIOR_HEART)

                characterComponent.skills += Skill(Data.Skills.Gladiator.DOUBLE_EDGE)
                characterComponent.skills += Skill(Data.Skills.Gladiator.BASH)
                characterComponent.skills += Skill(Data.Skills.Gladiator.BLOODLUST)
                characterComponent.skills += Skill(Data.Skills.Gladiator.ENDURANCE)
                characterComponent.skills += Skill(Data.Skills.Gladiator.SHATTER_ARMOR)

                inventory.add(newDagger(Element.NEUTRAL))
                inventory.add(newDagger(Element.FIRE))
                inventory.add(newDagger(Element.EARTH))
                inventory.add(newDagger(Element.AIR))
                inventory.add(newDagger(Element.WATER))

                inventory.add(UsableItem(Data.UsableItems.MANA_POTION))
                inventory.add(UsableItem(Data.UsableItems.HEALING_POTION))

                inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
                inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
                inventory.add(Weapon(Data.Weapons.OneHandedSwords.GUARD_SWORD))

                repeat(100) {
                    inventory.add(UsableItem(Data.UsableItems.TREASURE_BOX))
                }

                inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))
                inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))
                inventory.add(UsableItem(Data.UsableItems.HEALING_HERBS))

                inventory.add(Armor(Data.Armors.Body.TRAINING_ARMOR))

                inventory.add(MiscItem(Data.MiscItems.SILVER_INGOT))
            }
        }

        return player
    }

    /**
     * This is a base creation call for any character (monster, npc, player).
     */
    private fun newCharacter(data: SpawnData): Entity {
        val charData = data.get<CharacterData>("charData")

        val entity = CharacterEntity()

        data.data.forEach { entity.setProperty(it.key, it.value) }

        with(entity) {
            localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
            boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

            addComponent(CollidableComponent(true))
            addComponent(StateComponent())
            addComponent(CharacterEffectComponent())
            addComponent(CellMoveComponent(TILE_SIZE, TILE_SIZE, Config.CHAR_MOVE_SPEED))
            addComponent(AStarMoveComponent(LazyValue(Supplier { Gameplay.currentMap.grid })))

            addComponent(AnimationComponent(charData.description.textureName))
            addComponent(CharacterComponent(charData))
            addComponent(CharacterActionComponent())

            addComponent(CharacterChildViewComponent())

            val cellX = data.get<Int>("cellX")
            val cellY = data.get<Int>("cellY")
            setPositionToCell(cellX, cellY)
        }

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay()

        return entity
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

        // TODO: this is not necessary if we make all layers but 1 UI transparent and use
        // the only layer to handle movement clicks

        e.viewComponent.addChild(Rectangle(data.get("width"), data.get("height"), Color.TRANSPARENT))

        return e
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
    fun newDialogueTriggerBox(data: SpawnData): Entity {
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

private fun newDagger(element: Element): Weapon {
    val weapon = Weapon(Data.Weapons.Daggers.KNIFE)
    weapon.element.set(element)
    return weapon
}