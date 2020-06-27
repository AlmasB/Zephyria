package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.zeph.Config.SPRITE_SIZE
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
import javafx.util.Duration
import java.lang.RuntimeException

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

    private val channelCastDown: AnimationChannel
    private val channelCastRight: AnimationChannel
    private val channelCastUp: AnimationChannel
    private val channelCastLeft: AnimationChannel

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

        channelWalkDown = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 10, 9 * 10 + 9 - 1)
        channelWalkRight = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 11, 9 * 11 + 9 - 1)
        channelWalkUp = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 8, 9 * 8 + 9 - 1)
        channelWalkLeft = AnimationChannel(texture(textureName).image, 9, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.5), 9 * 9, 9 * 9 + 9 - 1)

        channelCastDown = AnimationChannel(texture(textureName).image, 7, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.7), 7 * 2, 7 * 2 + 7 - 1)
        channelCastRight = AnimationChannel(texture(textureName).image, 7, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.7), 7 * 3, 7 * 3 + 7 - 1)
        channelCastUp = AnimationChannel(texture(textureName).image, 7, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.7), 7 * 0, 7 * 0 + 7 - 1)
        channelCastLeft = AnimationChannel(texture(textureName).image, 7, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.7), 7 * 1, 7 * 1 + 7 - 1)

        channelSlashDown = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.6), 6 * 14, 6 * 14 + 6 - 1)
        channelSlashRight = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.6), 6 * 15, 6 * 15 + 6 - 1)
        channelSlashUp = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.6), 6 * 12, 6 * 12 + 6 - 1)
        channelSlashLeft = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(0.6), 6 * 13, 6 * 13 + 6 - 1)

        channelShootDown = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 18, 13 * 18 + 13 - 1)
        channelShootRight = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 19, 13 * 19 + 13 - 1)
        channelShootUp = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 16, 13 * 16 + 13 - 1)
        channelShootLeft = AnimationChannel(texture(textureName).image, 13, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 13 * 17, 13 * 17 + 13 - 1)

        channelDeath = AnimationChannel(texture(textureName).image, 6, SPRITE_SIZE, SPRITE_SIZE, Duration.seconds(1.2), 6 * 20, 6 * 20 + 6 - 1)

        animatedTexture = AnimatedTexture(channelIdleDown)
        animatedTexture.isPickOnBounds = true
        animatedTexture.loop()
    }

    override fun onAdded() {
        entity.viewComponent.addChild(animatedTexture)
    }

    override fun onUpdate(tpf: Double) {
        entity.z = entity.getComponent(NewCellMoveComponent::class.java).cellY + 5000
    }

    fun playDeath(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelDeath)
        animatedTexture.onCycleFinished = onFinished
    }

    internal val isFacingUp: Boolean
        get() = isIn(channelIdleUp, channelShootUp, channelSlashUp, channelWalkUp, channelCastUp)

    internal val isFacingDown: Boolean
        get() = isIn(channelIdleDown, channelShootDown, channelSlashDown, channelWalkDown, channelCastDown)

    internal val isFacingRight: Boolean
        get() = isIn(channelIdleRight, channelShootRight, channelSlashRight, channelWalkRight, channelCastRight)

    internal val isFacingLeft: Boolean
        get() = isIn(channelIdleLeft, channelShootLeft, channelSlashLeft, channelWalkLeft, channelCastLeft)

    private fun isIn(vararg channels: AnimationChannel) = channels.any { it === animatedTexture.animationChannel }

    fun playCastUp(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelCastUp)
        animatedTexture.onCycleFinished = onFinished
    }

    fun playCastDown(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelCastDown)
        animatedTexture.onCycleFinished = onFinished
    }

    fun playCastRight(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelCastRight)
        animatedTexture.onCycleFinished = onFinished
    }

    fun playCastLeft(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelCastLeft)
        animatedTexture.onCycleFinished = onFinished
    }

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

    fun loopAttackUp() {
        animatedTexture.loopAnimationChannel(channelSlashUp)
    }

    fun loopAttackDown() {
        animatedTexture.loopAnimationChannel(channelSlashDown)
    }

    fun loopAttackRight() {
        animatedTexture.loopAnimationChannel(channelSlashRight)
    }

    fun loopAttackLeft() {
        animatedTexture.loopAnimationChannel(channelSlashLeft)
    }
}