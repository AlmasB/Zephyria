package com.almasb.zeph

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityWorldListener
import com.almasb.fxgl.pathfinding.astar.AStarGrid
import com.almasb.zeph.character.CharacterEntity

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class GameMap {

    //val grid: AStarGrid

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
            }
        })
    }

    fun onUpdate(tpf: Double) {

    }
}