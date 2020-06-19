package com.almasb.zeph.character.ai

import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Config
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RandomWanderComponent : Component() {

    private lateinit var astar: NewAStarMoveComponent
    private lateinit var action: CharacterActionComponent

    private var time = 0.0

    override fun onUpdate(tpf: Double) {
        if (astar.isAtDestination) {
            time += tpf

            if (time >= Config.CHAR_IDLE_TIME) {
                astar.grid
                        .getRandomCell { it.isWalkable }
                        .ifPresent {
                            action.orderMove(it.x, it.y)
                        }

                time = 0.0
            }
        }
    }
}