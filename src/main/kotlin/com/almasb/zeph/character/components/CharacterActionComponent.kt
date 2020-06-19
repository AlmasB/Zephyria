package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.state.EntityState
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
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
    private lateinit var moveComponent: NewCellMoveComponent
    private lateinit var astar: NewAStarMoveComponent

    private val MOVE: EntityState = object : EntityState() {

        override fun onUpdate(tpf: Double) {
            if (astar.isAtDestination) {
                // identify why we came here

                if (attackTarget != null) {
                    attack(attackTarget!!)
                } else if (pickUpTarget != null) {
                    pickUp(pickUpTarget!!)
                } else {
                    state.changeStateToIdle()
                }
            } else {
                if (attackTarget != null) {
                    if (moveTarget!!.x.toInt() != attackTarget!!.characterComponent.getTileX()
                            || moveTarget!!.y.toInt() != attackTarget!!.characterComponent.getTileY()) {
                        orderAttack(attackTarget!!)
                    }
                }
            }
        }
    }

    private val ATTACK: EntityState = object : EntityState() {
        override fun onEnteredFrom(prevState: EntityState) {
            // TODO: figure out correct attack loop
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
                orderAttack(attackTarget!!)
            }
        }
    }

    override fun onAdded() {
        char = entity as CharacterEntity
    }

    override fun onUpdate(tpf: Double) {
        if (!state.isIn(MOVE))
            return

        if (!moveComponent.isMoving) {
            animationComponent.loopIdle()
            return
        }

        when {
            moveComponent.isMovingDown -> animationComponent.loopWalkDown()
            moveComponent.isMovingUp -> animationComponent.loopWalkUp()
            moveComponent.isMovingLeft -> animationComponent.loopWalkLeft()
            moveComponent.isMovingRight -> animationComponent.loopWalkRight()
        }
    }

    fun orderMove(x: Int, y: Int) {
        reset()
        state.changeState(MOVE)
        move(x, y)
    }

    fun orderAttack(target: CharacterEntity) {
        reset()
        attackTarget = target

        if (char.characterComponent.isInWeaponRange(target)) {
            attack(attackTarget!!)
        } else {
            move(attackTarget!!.characterComponent.getTileX(), attackTarget!!.characterComponent.getTileY())
        }
    }

    fun orderPickUp(item: Entity) {
        reset()
        pickUpTarget = item

        if (char.distance(item) <= Config.spriteSize) {
            pickUp(item)
        } else {
            // TODO: items should have smth like ItemComponent with cellX,cellY...
            val x = item.x.toInt() / Config.tileSize
            val y = item.y.toInt() / Config.tileSize
            move(x, y)
        }
    }

    private fun move(cellX: Int, cellY: Int) {
        moveTarget = Point2D(cellX.toDouble(), cellY.toDouble())
        state.changeState(MOVE)
        entity.getComponent(NewAStarMoveComponent::class.java).moveToCell(cellX, cellY)
    }

    private fun attack(target: CharacterEntity) {
        state.changeState(ATTACK)
    }

    private fun pickUp(item: Entity) {
        item.getPropertyOptional<Weapon>("weapon").ifPresent {
            char.inventory.addItem(it)
        }

        item.getPropertyOptional<Armor>("armor").ifPresent {
            char.inventory.addItem(it)
        }

        item.getPropertyOptional<UsableItem>("usable").ifPresent {
            char.inventory.addItem(it)
        }

        item.removeFromWorld()

        state.changeStateToIdle()
    }

    private fun reset() {
        attackTarget = null
        pickUpTarget = null
        moveTarget = null
    }
}