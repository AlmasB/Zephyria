package com.almasb.zeph.entity.ai

import com.almasb.astar.AStarGrid
import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.zeph.Config
import com.almasb.zeph.Services
import com.almasb.zeph.entity.character.CharacterEntity
import com.almasb.zeph.entity.character.PlayerEntity

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AIControl : AbstractControl() {

    lateinit var char: CharacterEntity

    val moveControl = RandomWanderControl()
    val attackControl = AttackControl()

    private var attacking = false

    private val range = 5
    private val grid: AStarGrid
    private val player: PlayerEntity

    init {
        grid = FXGL.getService(Services.GAME_APP).grid
        player = FXGL.getService(Services.GAME_APP).player
    }

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity

        attackControl.enabled = false

        char.addControl(moveControl)
        char.addControl(attackControl)
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        if (isInRange()) {
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

    private fun isInRange() = player.positionComponent.distance(char.positionComponent) <= range * Config.tileSize
}