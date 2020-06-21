package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.character.CharacterEntity
import javafx.util.Duration

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AnimationComponent(textureName: String) : Component() {

    private val animatedTexture: AnimatedTexture

    private val channelIdleDown: AnimationChannel
    private val channelIdleRight: AnimationChannel
    private val channelIdleUp: AnimationChannel
    private val channelIdleLeft: AnimationChannel

    private val channelWalkDown: AnimationChannel
    private val channelWalkRight: AnimationChannel
    private val channelWalkUp: AnimationChannel
    private val channelWalkLeft: AnimationChannel

    private val channelSlashDown: AnimationChannel
    private val channelSlashRight: AnimationChannel
    private val channelSlashUp: AnimationChannel
    private val channelSlashLeft: AnimationChannel

    private val channelShootDown: AnimationChannel
    private val channelShootRight: AnimationChannel
    private val channelShootUp: AnimationChannel
    private val channelShootLeft: AnimationChannel

    private val channelDeath: AnimationChannel

    init {
        channelIdleDown = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 9 * 10, 9 * 10)
        channelIdleRight = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 9 * 11, 9 * 11)
        channelIdleUp = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 9 * 8, 9 * 8)
        channelIdleLeft = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 9 * 9, 9 * 9)

        channelWalkDown = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 10, 9 * 10 +  9 -1)
        channelWalkRight = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 11, 9 * 11 +  9 -1)
        channelWalkUp = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 8, 9 * 8 +  9 -1)
        channelWalkLeft = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 9, 9 * 9 +  9 -1)

        channelSlashDown = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6 * 14, 6 * 14 +  6 -1)
        channelSlashRight = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6 * 15, 6 * 15 +  6 -1)
        channelSlashUp = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6 * 12, 6 * 12 +  6 -1)
        channelSlashLeft = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6 * 13, 6 * 13 +  6 -1)

        channelShootDown = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 18, 13 * 18 +  13 -1)
        channelShootRight = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 19, 13 * 19 +  13 -1)
        channelShootUp = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 16, 13 * 16 +  13 -1)
        channelShootLeft = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 17, 13 * 17 +  13 -1)

        channelDeath = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6*20, 6*20 + 6 -1)

        animatedTexture = AnimatedTexture(channelIdleDown)
        animatedTexture.loop()
    }

    override fun onAdded() {
        entity.viewComponent.addChild(animatedTexture)
    }

    override fun onUpdate(tpf: Double) {
        entity.z = (entity as CharacterEntity).characterComponent.getTileY()
    }

    fun playDeath(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelDeath)
        animatedTexture.onCycleFinished = onFinished
    }

    internal val isFacingUp: Boolean
        get() = isIn(channelIdleUp, channelShootUp, channelSlashUp, channelWalkUp)

    internal val isFacingDown: Boolean
        get() = isIn(channelIdleDown, channelShootDown, channelSlashDown, channelWalkDown)

    internal val isFacingRight: Boolean
        get() = isIn(channelIdleRight, channelShootRight, channelSlashRight, channelWalkRight)

    internal val isFacingLeft: Boolean
        get() = isIn(channelIdleLeft, channelShootLeft, channelSlashLeft, channelWalkLeft)

    private fun isIn(vararg channels: AnimationChannel) = channels.any { it === animatedTexture.animationChannel }

    // TODO: move to loopNoOverride()
    fun loopIdleUp() {
        if (animatedTexture.animationChannel === channelIdleUp)
            return

        animatedTexture.loopAnimationChannel(channelIdleUp)
    }

    fun loopIdleDown() {
        if (animatedTexture.animationChannel === channelIdleDown)
            return

        animatedTexture.loopAnimationChannel(channelIdleDown)
    }

    fun loopIdleRight() {
        if (animatedTexture.animationChannel === channelIdleRight)
            return

        animatedTexture.loopAnimationChannel(channelIdleRight)
    }

    fun loopIdleLeft() {
        if (animatedTexture.animationChannel === channelIdleLeft)
            return

        animatedTexture.loopAnimationChannel(channelIdleLeft)
    }

    fun loopWalkUp() {
        if (animatedTexture.animationChannel === channelWalkUp)
            return

        animatedTexture.loopAnimationChannel(channelWalkUp)
    }

    fun loopWalkDown() {
        if (animatedTexture.animationChannel === channelWalkDown)
            return

        animatedTexture.loopAnimationChannel(channelWalkDown)
    }

    fun loopWalkRight() {
        if (animatedTexture.animationChannel === channelWalkRight)
            return

        animatedTexture.loopAnimationChannel(channelWalkRight)
    }

    fun loopWalkLeft() {
        if (animatedTexture.animationChannel === channelWalkLeft)
            return

        animatedTexture.loopAnimationChannel(channelWalkLeft)
    }

    fun loopAttack() {
        animatedTexture.loopAnimationChannel(channelSlashDown)
    }
}