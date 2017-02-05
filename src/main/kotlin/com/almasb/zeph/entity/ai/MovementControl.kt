package com.almasb.zeph.entity.ai

import com.almasb.fxgl.ai.pathfinding.AStarNode
import com.almasb.fxgl.ecs.AbstractControl
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.zeph.CharacterAnimation
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.entity.character.CharacterEntity
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class MovementControl : AbstractControl() {

    var enabled = true

    protected lateinit var char: CharacterEntity
    private lateinit var animation: AnimatedTexture

    private val grid = (FXGL.getApp() as ZephyriaApp).grid
    protected var path: MutableList<AStarNode> = ArrayList()

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity
        animation = char.data.animation
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        if (!enabled)
            return

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
    }

    fun moveTo(x: Int, y: Int) {
        val startX = char.getTileX()
        val startY = char.getTileY()

        path = grid.getPath(startX, startY, x, y)
    }
}