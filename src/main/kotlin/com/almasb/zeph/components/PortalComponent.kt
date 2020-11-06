package com.almasb.zeph.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.image
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.zeph.EntityType
import com.almasb.zeph.Gameplay
import com.almasb.zeph.ZephyriaApp
import javafx.geometry.Rectangle2D
import javafx.util.Duration

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

    private var isCollidable = true

    val isOn: Boolean
        get() = isCollidable

    private lateinit var auraTexture: AnimatedTexture

    override fun onAdded() {
        interactionCollisionBox = entity.getObject("interactionCollisionBox")

        entity.getPropertyOptional<Boolean>("isCollidable").ifPresent {
            isCollidable = it
        }

        val channel = AnimationChannel(image("portal_aura.png"),
                framesPerRow = 8,
                frameWidth = 128, frameHeight = 128,
                channelDuration = Duration.seconds(1.0),
                startFrame = 0, endFrame = 31
        )

        auraTexture = AnimatedTexture(channel).loop()
        auraTexture.translateX = -3.0
        auraTexture.translateY = -93.0
        auraTexture.scaleX = 1.5
        auraTexture.scaleY = 1.5

        if (isCollidable) {
            entity.viewComponent.addChild(auraTexture)
        }
    }

    override fun onUpdate(tpf: Double) {
        if (!isCollidable)
            return

        val player = getGameWorld().getSingleton(EntityType.PLAYER)

        if (interactionCollisionBox.contains(player.anchoredPosition)) {
            FXGL.getGameController().gotoLoading(Runnable {
                Gameplay.gotoMap(mapName, toCellX, toCellY)
            })
        }
    }

    fun turnOn() {
        isCollidable = true

        entity.viewComponent.addChild(auraTexture)
    }

    fun turnOff() {
        isCollidable = false

        entity.viewComponent.removeChild(auraTexture)
    }
}