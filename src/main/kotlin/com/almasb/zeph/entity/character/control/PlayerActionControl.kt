package com.almasb.zeph.entity.character.control

import com.almasb.astar.AStarGrid
import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.zeph.Config
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.entity.ai.AttackControl
import com.almasb.zeph.entity.ai.MovementControl
import com.almasb.zeph.entity.ai.RandomWanderControl
import com.almasb.zeph.entity.character.CharacterEntity
import com.almasb.zeph.entity.character.PlayerEntity
import javafx.beans.property.SimpleObjectProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerActionControl : AbstractControl() {

    private lateinit var player: PlayerEntity

    val moveControl = MovementControl()
    val attackControl = AttackControl()

    val selected = SimpleObjectProperty<Entity>()

    private var attacking = false

    private val range = 5
    private val grid: AStarGrid

    init {
        grid = (FXGL.getApp() as ZephyriaApp).grid
    }

    override fun onAdded(entity: Entity) {
        player = entity as PlayerEntity

        attackControl.enabled = false
        attackControl.selected.bind(selected)

        player.addControl(moveControl)
        player.addControl(attackControl)
    }

    override fun onUpdate(entity: Entity, tpf: Double) {

        if (selected.value is CharacterEntity /* TODO: && can be attacked, i.e. enemy */) {
            if (!attacking) {
                moveControl.enabled = false
                attackControl.enabled = true
                attacking = true
            }
        } else {
            if (attacking) {
                moveControl.enabled = true
                attackControl.enabled = false
                attacking = false
            }
        }
    }

    //private fun isInRange() = player.positionComponent.distance(char.positionComponent) <= range * Config.tileSize

    fun moveTo(x: Int, y: Int) {
        moveControl.moveTo(x, y)
    }
}