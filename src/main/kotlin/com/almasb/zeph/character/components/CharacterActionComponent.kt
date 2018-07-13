package com.almasb.zeph.character.components

import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.extra.ai.pathfinding.AStarNode
import com.almasb.fxgl.extra.entity.state.State
import com.almasb.fxgl.extra.entity.state.StateComponent
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.item.Weapon
import javafx.geometry.Point2D

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterActionComponent : StateComponent() {

    private var attackTarget: CharacterEntity? = null
    private var pickUpTarget: Entity? = null
    private var moveTarget: Point2D? = null

    private lateinit var char: CharacterEntity

    private val IDLE: State = object : State() {
        override fun onEnter(prevState: State?) {

        }

        override fun onUpdate(tpf: Double) {

        }
    }

    private val MOVE: State = object : State() {
        override fun onEnter(prevState: State?) {
            // play anim
        }

        //private lateinit var animation: AnimatedTexture

        private val grid = FXGL.getAppCast<ZephyriaApp>().grid
        protected var path: MutableList<AStarNode> = arrayListOf()

        override fun onUpdate(tpf: Double) {

            doMove()

            if (moveTarget != null) {

                moveTo(moveTarget!!.x.toInt(), moveTarget!!.y.toInt())

                if (path.isEmpty()) {
                    state = IDLE
                }

            } else if (attackTarget != null) {
                val x = attackTarget!!.characterComponent.getTileX()
                val y = attackTarget!!.characterComponent.getTileY()

                moveTo(x, y)

                if (path.isEmpty()) {
                    state = ATTACK
                }

            } else if (pickUpTarget != null) {

                val item = pickUpTarget!!

                val x = item.x.toInt() / Config.tileSize
                val y = item.y.toInt() / Config.tileSize

                moveTo(x, y)

                if (path.isEmpty()) {
                    val weapon = item.getObject<Weapon>("weapon")

                    (char as PlayerEntity).inventory.addItem(weapon);
                    item.removeFromWorld()

                    state = IDLE
                }
            }
        }

        private fun doMove() {
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

        private fun moveTo(x: Int, y: Int) {
            if (Point2D(x.toDouble(), y.toDouble()).multiply(Config.tileSize.toDouble()).distance(char.position) < Config.tileSize) {
                return
            }

            val startX = char.characterComponent.getTileX()
            val startY = char.characterComponent.getTileY()

            path = grid.getPath(startX, startY, x, y)
        }
    }

    private val ATTACK: State = object : State() {
        override fun onEnter(prevState: State?) {
            // play anim
        }

        override fun onUpdate(tpf: Double) {
            if (char.characterComponent.isInWeaponRange(attackTarget!!)) {

                if (char.characterComponent.canAttack()) {
                    val dmg = char.characterComponent.attack(attackTarget!!)
                    FXGL.getAppCast<ZephyriaApp>().showDamage(dmg, attackTarget!!.center)

                    char.characterComponent.resetAtkTick()

                    // TODO: if dead, state IDLE
                }

            } else {
                state = MOVE
            }
        }
    }

    init {
        state = IDLE
    }

    override fun onAdded() {
        char = entity as CharacterEntity
    }

    fun orderMove(x: Int, y: Int) {
        reset()
        moveTarget = Point2D(x.toDouble(), y.toDouble())
        state = MOVE
    }

    fun orderAttack(target: CharacterEntity) {
        reset()
        attackTarget = target

        if (char.characterComponent.isInWeaponRange(target)) {
            state = ATTACK
        } else {
            state = MOVE
        }
    }

    fun orderPickUp(item: Entity) {
        reset()
        pickUpTarget = item

        if (char.distance(item) <= Config.tileSize) {
            val weapon = item.getObject<Weapon>("weapon")

            (char as PlayerEntity).inventory.addItem(weapon);
            item.removeFromWorld()
        } else {
            state = MOVE
        }
    }

    private fun reset() {
        attackTarget = null
        pickUpTarget = null
        moveTarget = null
    }
}