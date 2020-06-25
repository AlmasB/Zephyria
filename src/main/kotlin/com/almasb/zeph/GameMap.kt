package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityWorldListener
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.tiled.Layer
import com.almasb.fxgl.pathfinding.CellState
import com.almasb.fxgl.pathfinding.astar.AStarCell
import com.almasb.fxgl.pathfinding.astar.AStarGrid
import com.almasb.fxgl.pathfinding.astar.AStarGridView
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.Config.Z_INDEX_DECOR_ABOVE_PLAYER
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.data.Data.getCharacterData
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.item.ItemData
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.util.Duration
import java.util.function.Consumer
import java.util.function.Predicate

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class GameMap(private val level: Level) {

    lateinit var grid: AStarGrid

    /**
     * K - monster id.
     * V - how many to keep alive.
     */
    private val spawnIDs = hashMapOf<Int, Int>()

    /**
     * K - monster id.
     * V - list of monsters in the game (active) with that id.
     */
    private val monsters = hashMapOf<Int, MutableList<CharacterEntity>>()

    init {
        level.properties
                .keys() // a char id
                .filter { key: String -> key.startsWith("2") }
                .map { key: String -> key.toInt() }
                .forEach { id: Int ->
                    val numMobs = level.properties.getInt(id.toString())

                    spawnIDs[id] = numMobs
                }

        spawnIDs.forEach { id, _ ->
            monsters[id] = arrayListOf()
        }

        getGameWorld().addWorldListener(object : EntityWorldListener {
            override fun onEntityAdded(e: Entity) {
                if (!e.isType(EntityType.MONSTER)) {
                    return
                }

                val monster = e as CharacterEntity

                monsters[monster.data.description.id]!! += monster
            }

            override fun onEntityRemoved(e: Entity) {
                if (!e.isType(EntityType.MONSTER)) {
                    return
                }

                val monster = e as CharacterEntity

                monsters[monster.data.description.id]!! -= monster

                // TODO: spawn a new one ...
            }
        })
    }

    fun enter() {
        getGameWorld().setLevel(level)

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), MAP_WIDTH, MAP_HEIGHT, TILE_SIZE, TILE_SIZE) { type: Any ->
            if (type == EntityType.NAV || type == EntityType.PORTAL)
                return@fromWorld CellState.WALKABLE

            CellState.NOT_WALKABLE
        }

        getGameWorld().getEntitiesFiltered(Predicate { it.isType("TiledMapLayer") })
                .filter { it.getObject<Layer>("layer").name == "Decor_above_player" }
                .forEach {
                    it.viewComponent.parent.isMouseTransparent = true
                    it.z = Z_INDEX_DECOR_ABOVE_PLAYER
                }

        spawnMobs(level)
    }

    fun onUpdate(tpf: Double) {

    }

    private fun spawnMobs(level: Level) {
        spawnIDs.forEach { id, numMobs ->
            for (i in 0 until numMobs) {
                grid.getRandomCell { it.isWalkable }
                        .ifPresent { spawnCharacter(it.x, it.y, getCharacterData(id)) }
            }
        }
    }

    private fun spawnCharacter(cellX: Int, cellY: Int, charData: CharacterData) {
        val data = SpawnData(0.0, 0.0)
        data.put("cellX", cellX)
        data.put("cellY", cellY)
        data.put("charData", charData)

        val e = spawn("char", data) as CharacterEntity
    }



    // TODO:
//    private fun spawnItem(x: Int, y: Int, itemData: ItemData) {
//        val data = SpawnData(x.toDouble(), y.toDouble())
//        data.put("itemData", itemData)
//        val itemEntity = FXGL.spawn("item", data)
//        itemEntity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler { event: MouseEvent? -> player.actionComponent.orderPickUp(itemEntity) })
//
//        animationBuilder()
//                .duration(Duration.seconds(0.3))
//                .interpolator(Interpolators.SMOOTH.EASE_IN())
//                .translate(itemEntity)
//                .from(itemEntity.position)
//                .to(itemEntity.position.add(FXGLMath.randomPoint2D().multiply(20.0)))
//                .buildAndPlay()
//    }

    // TODO: showGrid() add a check box to DevPane
//    private fun showGrid() {
//        val gridView = AStarGridView(grid, TILE_SIZE, TILE_SIZE)
//        gridView.isMouseTransparent = true
//
//        getGameScene().addUINode(gridView)
//    }
}