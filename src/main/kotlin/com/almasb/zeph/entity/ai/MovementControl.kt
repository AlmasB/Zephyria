package com.almasb.zeph.entity.ai

import com.almasb.astar.AStarNode
import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.entity.GameEntity
import com.almasb.fxgl.texture.DynamicAnimatedTexture
import com.almasb.zeph.CharacterAnimation
import com.almasb.zeph.Config
import com.almasb.zeph.Services
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
    private lateinit var animation: DynamicAnimatedTexture

    private val grid = FXGL.getService(Services.GAME_APP).grid
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

    protected fun getTileX(entity: GameEntity): Int {
        return entity.positionComponent.x.toInt() / Config.tileSize
    }

    protected fun getTileY(entity: GameEntity): Int {
        return entity.positionComponent.y.toInt() / Config.tileSize
    }

    fun moveTo(x: Int, y: Int) {
        val startX = getTileX(char)
        val startY = getTileY(char)

        path = grid.getPath(startX, startY, x, y)
    }
}