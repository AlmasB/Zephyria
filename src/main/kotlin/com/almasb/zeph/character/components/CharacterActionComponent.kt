package com.almasb.zeph.character.components

import com.almasb.fxgl.core.collection.grid.Cell
import com.almasb.fxgl.dsl.fire
import com.almasb.fxgl.dsl.spawn
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.state.EntityState
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.pathfinding.CellMoveComponent
import com.almasb.fxgl.pathfinding.astar.AStarCell
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent
import com.almasb.zeph.Config
import com.almasb.zeph.EntityType
import com.almasb.zeph.Gameplay
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.events.OnItemPickedUpEvent
import com.almasb.zeph.events.OnOrderedMoveEvent

import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.MiscItem
import com.almasb.zeph.item.UsableItem
import com.almasb.zeph.item.Weapon
import com.almasb.zeph.skill.Skill
import javafx.geometry.Point2D

/**
 * Contains actions that can be performed by a character.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterActionComponent : Component() {

    private val log = Logger.get("CharacterActionComponent")

    private var attackTarget: CharacterEntity? = null
    private var dialogueTarget: CharacterEntity? = null
    private var pickUpTarget: Entity? = null
    private var moveTarget: Point2D? = null

    private lateinit var state: StateComponent
    private lateinit var char: CharacterEntity
    private lateinit var animationComponent: AnimationComponent
    private lateinit var moveComponent: CellMoveComponent

    private val astar: AStarMoveComponent<AStarCell>
        get() = entity.getComponent(AStarMoveComponent::class.java) as AStarMoveComponent<AStarCell>

    private val MOVE: EntityState = object : EntityState() {

        override fun onUpdate(tpf: Double) {
            if (astar.isAtDestination) {
                // identify why we came here

                if (attackTarget != null) {
                    startCombatWith(attackTarget!!)
                    attackTarget = null
                } else if (pickUpTarget != null) {
                    pickUp(pickUpTarget!!)
                } else if (dialogueTarget != null) {
                    startDialogue(dialogueTarget!!)
                } else {
                    state.changeStateToIdle()
                }
            } else {
                if (attackTarget != null) {
                    val moveCellX = moveTarget!!.x.toInt()
                    val moveCellY = moveTarget!!.y.toInt()

                    if (attackTarget!!.distance(moveCellX, moveCellY) > char.characterComponent.attackRange) {
                        orderAttack(attackTarget!!)
                    }
                }

                if (dialogueTarget != null) {
                    val moveCellX = moveTarget!!.x.toInt()
                    val moveCellY = moveTarget!!.y.toInt()

                    if (dialogueTarget!!.distance(moveCellX, moveCellY) > 1) {
                        orderStartDialogue(dialogueTarget!!)
                    }
                }
            }
        }
    }

    override fun onAdded() {
        char = entity as CharacterEntity
    }

    override fun onUpdate(tpf: Double) {
        if (state.isIdle) {
            when {
                animationComponent.isFacingLeft -> animationComponent.loopIdleLeft()
                animationComponent.isFacingRight -> animationComponent.loopIdleRight()
                animationComponent.isFacingUp -> animationComponent.loopIdleUp()
                animationComponent.isFacingDown -> animationComponent.loopIdleDown()
            }
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
        fire(OnOrderedMoveEvent(char, x, y))

        reset()
        state.changeState(MOVE)
        move(x, y)
    }

    fun orderAttack(target: CharacterEntity) {
        reset()
        attackTarget = target

        if (char.characterComponent.isInWeaponRange(target)) {
            startCombatWith(attackTarget!!)
        } else {

            val cell = findNearestWalkableCell(attackTarget!!)

            move(cell.x, cell.y)
        }
    }

    private fun findNearestWalkableCell(target: CharacterEntity): Cell {
        return Gameplay.currentMap
                .grid
                .getNeighbors(target.cellX, target.cellY)
                .filter { it.isWalkable }
                .minBy{ char.distance(it.x, it.y) }
    }

    private fun castSkill(skillIndex: Int, target: CharacterEntity) {
        val skill: Skill = char.characterComponent.skills[skillIndex]

        char.characterComponent.useTargetSkill(skill, target)
    }

    fun orderPickUp(item: Entity) {
        reset()
        pickUpTarget = item

        if (char.distance(item) <= Config.SPRITE_SIZE) {
            pickUp(item)
        } else {
            // TODO: items should have smth like ItemComponent with cellX,cellY...
            val x = item.x.toInt() / Config.TILE_SIZE
            val y = item.y.toInt() / Config.TILE_SIZE
            move(x, y)
        }
    }

    fun orderStartDialogue(target: CharacterEntity) {
        reset()
        dialogueTarget = target

        if (char.distance(target) <= Config.SPRITE_SIZE) {
            startDialogue(dialogueTarget!!)
        } else {
            val cell = findNearestWalkableCell(dialogueTarget!!)

            move(cell.x, cell.y)
        }
    }

    fun orderIdle() {
        state.changeStateToIdle()
    }

    private fun move(cellX: Int, cellY: Int) {
        moveTarget = Point2D(cellX.toDouble(), cellY.toDouble())
        entity.getComponent(AStarMoveComponent::class.java).moveToCell(cellX, cellY)
        state.changeState(MOVE)
    }

    private fun startCombatWith(target: CharacterEntity) {
        Gameplay.startCombat(char, target)
    }

    private fun pickUp(item: Entity) {
        item.getPropertyOptional<Weapon>("weapon").ifPresent {
            char.inventory.add(it)

            fire(OnItemPickedUpEvent(char, it))
        }

        item.getPropertyOptional<Armor>("armor").ifPresent {
            char.inventory.add(it)

            fire(OnItemPickedUpEvent(char, it))
        }

        item.getPropertyOptional<UsableItem>("usable").ifPresent {
            char.inventory.add(it)

            fire(OnItemPickedUpEvent(char, it))
        }

        item.getPropertyOptional<MiscItem>("misc").ifPresent {
            char.inventory.add(it)

            fire(OnItemPickedUpEvent(char, it))
        }

        item.removeFromWorld()

        state.changeStateToIdle()
    }

    private fun startDialogue(npc: CharacterEntity) {
        state.changeStateToIdle()

        if (!npc.isType(EntityType.NPC)) {
            log.warning("Cannot start dialogue because $npc is not NPC type")
            return
        }

        val npcData = npc.getObject<NPCData>("npcData")

        Gameplay.startDialogue(npcData.dialogueName.removePrefix("dialogues/"), npc)
    }

    private fun reset() {
        attackTarget = null
        pickUpTarget = null
        dialogueTarget = null
        moveTarget = null
    }
}