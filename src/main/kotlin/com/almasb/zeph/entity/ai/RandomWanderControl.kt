package com.almasb.zeph.entity.ai

import com.almasb.fxgl.entity.Entity
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

    override fun onUpdate(tpf: Double) {
        super.onUpdate(tpf)

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