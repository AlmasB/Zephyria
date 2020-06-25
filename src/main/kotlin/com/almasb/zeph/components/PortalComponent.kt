package com.almasb.zeph.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.EntityType
import com.almasb.zeph.Gameplay
import com.almasb.zeph.ZephyriaApp
import javafx.geometry.Rectangle2D

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PortalComponent(
        val mapName: String,
        val toCellX: Int,
        val toCellY: Int
) : Component() {

    private lateinit var interactionCollisionBox: Rectangle2D

    override fun onAdded() {
        interactionCollisionBox = entity.getObject("interactionCollisionBox")
    }

    override fun onUpdate(tpf: Double) {
        val player = getGameWorld().getSingleton(EntityType.PLAYER)

        if (interactionCollisionBox.contains(player.anchoredPosition)) {
            FXGL.getGameController().gotoLoading(Runnable {
                Gameplay.gotoMap(mapName, toCellX, toCellY)
            })
        }
    }
}