package com.almasb.zeph.character.ai

import com.almasb.fxgl.dsl.random
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent
import com.almasb.zeph.Config
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.components.CharacterActionComponent


/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RandomWanderComponent : Component() {

    private lateinit var astar: AStarMoveComponent
    private lateinit var action: CharacterActionComponent

    private var time = 0.0
    private var nextMoveTime = random(Config.CHAR_IDLE_TIME, Config.CHAR_IDLE_TIME + 2.5)

    override fun onUpdate(tpf: Double) {
        if (astar.isAtDestination) {
            time += tpf

            if (time >= nextMoveTime) {
                // TODO: check logic for identifying where a char is
                val cellX = (entity as CharacterEntity).cellX
                val cellY = (entity as CharacterEntity).cellY

                astar.grid
                        .getRandomCell { it.isWalkable && it.distance(astar.grid.get(cellX, cellY)) < 5 }
                        .ifPresent {
                            action.orderMove(it.x, it.y)
                        }

                time = 0.0
                nextMoveTime = random(Config.CHAR_IDLE_TIME, Config.CHAR_IDLE_TIME + 2.5)
            }
        }
    }
}