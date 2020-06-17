package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.state.EntityState
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.pathfinding.astar.AStarCell
import com.almasb.fxgl.pathfinding.astar.AStarPathfinder
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.UsableItem
import com.almasb.zeph.item.Weapon
import javafx.geometry.Point2D

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterActionComponent : Component() {

    private var attackTarget: CharacterEntity? = null
    private var pickUpTarget: Entity? = null
    private var moveTarget: Point2D? = null

    private lateinit var state: StateComponent
    private lateinit var char: CharacterEntity
    private lateinit var animationComponent: AnimationComponent

    private val IDLE: EntityState = object : EntityState() {
        override fun onEnteredFrom(prevState: EntityState?) {
            if (prevState != null)
                animationComponent.loopIdle()
        }
    }

    private val MOVE: EntityState = object : EntityState() {

        private val grid = FXGL.getAppCast<ZephyriaApp>().grid
        protected var path: MutableList<AStarCell> = arrayListOf()

        override fun onUpdate(tpf: Double) {
            doMove()

            if (moveTarget != null) {

                moveTo(moveTarget!!.x.toInt(), moveTarget!!.y.toInt())

                if (path.isEmpty()) {
                    state.changeStateToIdle()
                }

            } else if (attackTarget != null) {
                val x = attackTarget!!.characterComponent.getTileX()
                val y = attackTarget!!.characterComponent.getTileY()

                moveTo(x, y)

                if (path.isEmpty()) {
                    state.changeState(ATTACK)
                }

            } else if (pickUpTarget != null) {

                val item = pickUpTarget!!

                val x = item.x.toInt() / Config.tileSize
                val y = item.y.toInt() / Config.tileSize

                moveTo(x, y)

                if (path.isEmpty()) {
                    pickUp(item)

                    state.changeStateToIdle()
                }
            }
        }

        private fun doMove() {
            while (!path.isEmpty()) {
                val node = path[0]

                var dx = node.x * Config.tileSize - char.x
                var dy = node.y * Config.tileSize - char.y

                dx = Math.signum(dx)
                dy = Math.signum(dy)

                if (dx == 0.0 && dy == 0.0) {
                    path.removeAt(0)
                    continue
                } else if (dx > 0) {
                    //animation.setAnimationChannel(CharacterAnimation.WALK_RIGHT)
                    onChangeDir(0)
                } else if (dx < 0) {
                    //animation.setAnimationChannel(CharacterAnimation.WALK_LEFT)
                    onChangeDir(1)
                } else if (dy > 0) {
                    //animation.setAnimationChannel(CharacterAnimation.WALK_DOWN)
                    onChangeDir(2)
                } else if (dy < 0) {
                    //animation.setAnimationChannel(CharacterAnimation.WALK_UP)
                    onChangeDir(3)
                }

                dx *= 2.0
                dy *= 2.0

                char.translate(dx, dy)
                break
            }
        }

        private var currentDir = 0

        private fun onChangeDir(newDir: Int) {
            if (newDir != currentDir) {
                currentDir = newDir
                when (currentDir) {
                    0 -> {
                        animationComponent.loopWalkRight()
                    }
                    1 -> {
                        animationComponent.loopWalkLeft()
                    }
                    2 -> {
                        animationComponent.loopWalkDown()
                    }
                    3 -> {
                        animationComponent.loopWalkUp()
                    }
                    else -> {}
                }
            }
        }

        private fun moveTo(x: Int, y: Int) {
            if (Point2D(x.toDouble(), y.toDouble()).multiply(Config.tileSize.toDouble()).distance(char.position) < Config.tileSize) {
                return
            }

            val startX = char.characterComponent.getTileX()
            val startY = char.characterComponent.getTileY()

            path = AStarPathfinder(grid).findPath(startX, startY, x, y)
        }
    }

    private val ATTACK: EntityState = object : EntityState() {
        override fun onEnteredFrom(prevState: EntityState?) {
            animationComponent.loopAttack()
        }

        override fun onUpdate(tpf: Double) {
            if (char.characterComponent.isInWeaponRange(attackTarget!!)) {

                if (char.characterComponent.canAttack()) {
                    val dmg = char.characterComponent.attack(attackTarget!!)
                    FXGL.getAppCast<ZephyriaApp>().showDamage(dmg, attackTarget!!.center)

                    char.characterComponent.resetAtkTick()

                    if (attackTarget!!.hp.isZero) {
                        FXGL.getAppCast<ZephyriaApp>().playerKilledChar(attackTarget!!)
                        state.changeStateToIdle()
                    }
                }

            } else {
                state.changeState(MOVE)
            }
        }
    }

    override fun onAdded() {
        char = entity as CharacterEntity
    }

    fun orderMove(x: Int, y: Int) {
        reset()
        moveTarget = Point2D(x.toDouble(), y.toDouble())
        state.changeState(MOVE)
    }

    fun orderAttack(target: CharacterEntity) {
        reset()
        attackTarget = target

        if (char.characterComponent.isInWeaponRange(target)) {
            state.changeState(ATTACK)
        } else {
            state.changeState(MOVE)
        }
    }

    fun orderPickUp(item: Entity) {
        reset()
        pickUpTarget = item

        if (char.distance(item) <= Config.tileSize) {
            pickUp(item)
        } else {
            state.changeState(MOVE)
        }
    }

    private fun pickUp(item: Entity) {
        item.getPropertyOptional<Weapon>("weapon").ifPresent {
            (char as PlayerEntity).inventory.addItem(it)
        }

        item.getPropertyOptional<Armor>("armor").ifPresent {
            (char as PlayerEntity).inventory.addItem(it)
        }

        item.getPropertyOptional<UsableItem>("usable").ifPresent {
            (char as PlayerEntity).inventory.addItem(it)
        }

        item.removeFromWorld()
    }

    private fun reset() {
        attackTarget = null
        pickUpTarget = null
        moveTarget = null
    }
}