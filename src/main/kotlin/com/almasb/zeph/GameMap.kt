package com.almasb.zeph

import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityWorldListener
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.tiled.Layer
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.pathfinding.CellState
import com.almasb.fxgl.pathfinding.astar.AStarGrid
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.Config.Z_INDEX_DECOR_ABOVE_PLAYER
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.components.TiledMapLayerOptimizerComponent
import com.almasb.zeph.data.Data
import com.almasb.zeph.data.Data.getCharacterData
import com.almasb.zeph.item.ItemData
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView
import java.lang.RuntimeException
import java.util.function.Predicate

/**
 * TODO: where to store map data in .tmx or MapData, e.g. bgmName?
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class GameMap(private val level: Level) : EntityWorldListener {

    private val log = Logger.get(javaClass)

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

    private val npcs = hashMapOf<Int, Pair<Int, Int>>()

    init {
        level.properties
                .keys()
                // monster or NPC id
                .filter { key: String -> key.startsWith("2") }
                .map { key: String -> key.toInt() }
                .forEach { id: Int ->
                    if (Data.isMonster(id)) {
                        val numMobs = level.properties.getInt(id.toString())

                        spawnIDs[id] = numMobs
                    } else if (Data.isNPC(id)) {

                        val tokens = level.properties.getString(id.toString()).split(',')
                        val cellX = tokens[0].toInt()
                        val cellY = tokens[1].toInt()

                        npcs[id] = cellX to cellY

                    } else {
                        throw RuntimeException("Map has invalid data (not monster or NPC): $id")
                    }
                }

        spawnIDs.forEach { id, _ ->
            monsters[id] = arrayListOf()
        }
    }

    override fun onEntityAdded(e: Entity) {
        if (!e.isType(MONSTER)) {
            return
        }

        val monster = e as CharacterEntity

        monsters[monster.data.description.id]?.add(monster)
    }

    override fun onEntityRemoved(e: Entity) {
        if (!e.isType(MONSTER)) {
            return
        }

        val monster = e as CharacterEntity
        val id = monster.data.description.id

        if (monsters.containsKey(id)) {
            val entities = monsters[id]!!

            entities.remove(monster)
        }
    }

    fun enter() {
        getGameWorld().addWorldListener(this)

        getGameWorld().setLevel(level)

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), MAP_WIDTH, MAP_HEIGHT, TILE_SIZE, TILE_SIZE) { type: Any ->
            if (type == NAV || type == PORTAL || type == TEXT_TRIGGER_BOX || type == DIALOGUE)
                return@fromWorld CellState.WALKABLE

            CellState.NOT_WALKABLE
        }

        log.info("Entered map: ${grid.walkableCells.size} walkable cells")

        getGameWorld().getEntitiesFiltered(Predicate { it.isType("TiledMapLayer") })
                .onEach {
                    //it.viewComponent.isVisible = false
                    it.addComponent(TiledMapLayerOptimizerComponent())
                }
                .filter { it.getObject<Layer>("layer").name == "Decor_above_player" }
                .forEach {
                    it.viewComponent.parent.isMouseTransparent = true
                    it.z = Z_INDEX_DECOR_ABOVE_PLAYER
                }

        spawnMobs(level)
        spawnNPCs()
    }

    fun exit() {
        getGameWorld().removeWorldListener(this)
    }

    fun onUpdate(tpf: Double) {
        monsters.forEach { id, entities ->
            if (entities.size < spawnIDs[id]!!) {
                grid.getRandomCell { it.isWalkable }
                        .ifPresent { spawnMonster(it.x, it.y, getCharacterData(id)) }
            }
        }
    }

    private fun spawnMobs(level: Level) {
        spawnIDs.forEach { id, numMobs ->
            for (i in 0 until numMobs) {
                grid.getRandomCell { it.isWalkable }
                        .ifPresent { spawnMonster(it.x, it.y, getCharacterData(id)) }
            }
        }
    }

    fun spawnMonster(cellX: Int, cellY: Int, charData: CharacterData) {
        log.debug("spawnMonster ${charData.description.name} (${charData.description.id}) at $cellX,$cellY")

        val data = SpawnData(0.0, 0.0)
        data.put("cellX", cellX)
        data.put("cellY", cellY)
        data.put("charData", charData)

        val e = spawn("monster", data) as CharacterEntity
    }

    private fun spawnNPCs() {
        npcs.forEach { id, (cellX, cellY) ->
            spawnNPC(cellX, cellY, Data.getNPCData(id))
        }
    }

    fun spawnNPC(cellX: Int, cellY: Int, npcData: NPCData) {
        log.debug("spawnNPC ${npcData.description.name} (${npcData.description.id}) at $cellX,$cellY")

        val data = SpawnData(0.0, 0.0)
        data.put("cellX", cellX)
        data.put("cellY", cellY)
        data.put("npcData", npcData)

        spawn("npc", data)
    }

    fun spawnItem(cellX: Int, cellY: Int, itemData: ItemData) {
        log.debug("spawnItem ${itemData.description.name} (${itemData.description.id}) at $cellX,$cellY")

        val data = SpawnData(cellX * TILE_SIZE * 1.0, cellY * TILE_SIZE * 1.0)
        data.put("itemData", itemData)

        val itemEntity = spawn("item", data)
    }

    // TODO: showGrid() add a check box to DevPane
//    private fun showGrid() {
//        val gridView = AStarGridView(grid, TILE_SIZE, TILE_SIZE)
//        gridView.isMouseTransparent = true
//
//        getGameScene().addUINode(gridView)
//    }
}