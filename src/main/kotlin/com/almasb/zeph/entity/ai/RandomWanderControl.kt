package com.almasb.zeph.entity.ai

import com.almasb.astar.AStarNode
import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.entity.GameEntity
import com.almasb.fxgl.texture.DynamicAnimatedTexture
import com.almasb.zeph.CharacterAnimation
import com.almasb.zeph.Config
import com.almasb.zeph.Services
import com.almasb.zeph.entity.character.CharacterEntity
import javafx.util.Duration
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RandomWanderControl : AbstractControl() {

    private lateinit var char: CharacterEntity
    private lateinit var animation: DynamicAnimatedTexture

    private val random = Random()
    private var time = 0.0

    private var path: MutableList<AStarNode> = ArrayList()

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity
        animation = char.data.animation
    }

    override fun onUpdate(entity: Entity, tpf: Double) {

        while (!path.isEmpty()) {
            val node = path[0]

            var dx = node.x * Config.tileSize - char.positionComponent.x
            var dy = node.y * Config.tileSize - char.positionComponent.y

            dx = Math.signum(dx)
            dy = Math.signum(dy)

            if (dx == 0.0 && dy == 0.0) {
                path.removeAt(0)
                continue
            } else if (dx > 0) {
                animation.setAnimationChannel(CharacterAnimation.WALK_RIGHT)
            } else if (dx < 0) {
                animation.setAnimationChannel(CharacterAnimation.WALK_LEFT)
            } else if (dy > 0) {
                animation.setAnimationChannel(CharacterAnimation.WALK_DOWN)
            } else if (dy < 0) {
                animation.setAnimationChannel(CharacterAnimation.WALK_UP)
            }

            dx *= 2.0
            dy *= 2.0

            char.positionComponent.translate(dx, dy)
            break
        }

        if (path.isEmpty()) {
            time += tpf

            if (time >= 3.0) {
                nextRandomPoint()
                time = 0.0
            }
        }
    }

    private fun getTileX(entity: GameEntity): Int {
        return entity.positionComponent.x.toInt() / Config.tileSize
    }

    private fun getTileY(entity: GameEntity): Int {
        return entity.positionComponent.y.toInt() / Config.tileSize
    }

    private fun nextRandomPoint() {
        val targetX = random.nextInt(Config.mapWidth)
        val targetY = random.nextInt(Config.mapHeight)

        val startX = getTileX(char)
        val startY = getTileY(char)

        path = FXGL.getService(Services.GAME_APP).grid.getPath(startX, startY, targetX, targetY)
    }
}