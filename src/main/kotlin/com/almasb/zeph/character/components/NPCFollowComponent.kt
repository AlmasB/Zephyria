package com.almasb.zeph.character.components

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Gameplay
import com.almasb.zeph.character.CharacterEntity

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