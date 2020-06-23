package com.almasb.zeph

import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.spawn
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.entity.components.IrremovableComponent
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.pathfinding.Cell
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxgl.procedural.HeightMapGenerator
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.SKILL_PROJECTILE_SPEED
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.Config.TILE_SIZE
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
import javafx.geometry.Rectangle2D
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
            entity.localAnchor = Point2D(SPRITE_SIZE / 2.0, SPRITE_SIZE - 10.0)
            entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(SPRITE_SIZE.toDouble(), SPRITE_SIZE.toDouble())))

            with(entity) {
                addComponent(CollidableComponent(true))
                addComponent(StateComponent())
                addComponent(CharacterEffectComponent())
                addComponent(NewCellMoveComponent(TILE_SIZE, TILE_SIZE, Config.CHAR_MOVE_SPEED))
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
        try {
            val player = newCharacter(data)
            player.type = PLAYER
            player.removeComponent(RandomWanderComponent::class.java)
            player.addComponent(PlayerComponent())
            player.addComponent(IrremovableComponent())
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
        }
        return FXGL.entityBuilder(data)
                .view(itemData.description.textureName)
                .build()
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
        val e = entityBuilder(data)
                .view(Rectangle(TILE_SIZE * 1.0, TILE_SIZE * 1.0, null).also { it.stroke = Color.BLACK })
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
    player.characterComponent.skills += Skill(Data.Skills.Wizard.AMPLIFY_MAGIC)
    player.characterComponent.skills += Skill(Data.Skills.Mage.FIREBALL)

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