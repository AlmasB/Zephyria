package com.almasb.zeph.character.npc

import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Gameplay
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.character.components.CharacterComponent

/**
 * Adds following behavior to an NPC.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class NPCFollowComponent : Component() {

    private lateinit var charComponent: CharacterComponent
    private lateinit var charActionComponent: CharacterActionComponent

    private var entityToFollow: CharacterEntity? = null

    override fun onUpdate(tpf: Double) {
        entityToFollow?.let { target ->
            if (target.distance(charComponent.char.cellX, charComponent.char.cellY) >= 3) {
                val randomCell = Gameplay.currentMap.grid
                        .getNeighbors(target.cellX, target.cellY)
                        .random()

                charActionComponent.orderMove(randomCell.x, randomCell.y)
            }
        }
    }

    fun startFollow(target: CharacterEntity) {
        entityToFollow = target
    }
}