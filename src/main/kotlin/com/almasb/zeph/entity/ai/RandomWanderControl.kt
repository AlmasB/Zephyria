package com.almasb.zeph.entity.ai

import com.almasb.fxgl.ecs.Entity
import com.almasb.zeph.Config
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RandomWanderControl : MovementControl() {

    private val random = Random()
    private var time = 0.0

    override fun onUpdate(entity: Entity, tpf: Double) {
        super.onUpdate(entity, tpf)

        if (path.isEmpty()) {
            time += tpf

            if (time >= 3.0) {
                nextRandomPoint()
                time = 0.0
            }
        }
    }

    private fun nextRandomPoint() {
        val targetX = random.nextInt(Config.mapWidth)
        val targetY = random.nextInt(Config.mapHeight)

        moveTo(targetX, targetY)
    }
}