package com.almasb.zeph.character.components

import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.extra.ai.pathfinding.AStarNode
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.CharacterEntity
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class MovementComponent : Component() {

    protected lateinit var char: CharacterEntity
    //private lateinit var animation: AnimatedTexture

    private val grid = FXGL.getAppCast<ZephyriaApp>().grid
    protected var path: MutableList<AStarNode> = arrayListOf()

    override fun onAdded() {
        char = entity as CharacterEntity
        //animation = char.data.animation
    }

    override fun onUpdate(tpf: Double) {
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
                //animation.setAnimationChannel(CharacterAnimation.WALK_RIGHT)
            } else if (dx < 0) {
                //animation.setAnimationChannel(CharacterAnimation.WALK_LEFT)
            } else if (dy > 0) {
                //animation.setAnimationChannel(CharacterAnimation.WALK_DOWN)
            } else if (dy < 0) {
                //animation.setAnimationChannel(CharacterAnimation.WALK_UP)
            }

            dx *= 2.0
            dy *= 2.0

            char.positionComponent.translate(dx, dy)
            break
        }
    }

    fun moveTo(x: Int, y: Int) {
        val startX = char.characterComponent.getTileX()
        val startY = char.characterComponent.getTileY()

        path = grid.getPath(startX, startY, x, y)
    }
}