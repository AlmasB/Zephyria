package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.zeph.Config
import javafx.util.Duration

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AnimationComponent(textureName: String) : Component() {

    private val animatedTexture: AnimatedTexture

    private val channelIdle: AnimationChannel

    private val channelWalkDown: AnimationChannel
    private val channelWalkRight: AnimationChannel
    private val channelWalkUp: AnimationChannel
    private val channelWalkLeft: AnimationChannel

    private val channelSlashDown: AnimationChannel
    private val channelSlashRight: AnimationChannel
    private val channelSlashUp: AnimationChannel
    private val channelSlashLeft: AnimationChannel

    private val channelDeath: AnimationChannel

    //    CAST_UP(0, 7),
//    CAST_LEFT(1, 7),
//    CAST_DOWN(2, 7),
//    CAST_RIGHT(3, 7),
//
//    WALK_RIGHT(11, 9),
//    WALK_LEFT(9, 9),
//    WALK_UP(8, 9),
//    WALK_DOWN(10, 9),
//
//    SLASH_UP(12, 6),
//    SLASH_LEFT(13, 6),
//    SLASH_DOWN(14, 6),
//    SLASH_RIGHT(15, 6),
//
//    SHOOT_UP(16, 13),
//    SHOOT_LEFT(17, 13),
//    SHOOT_DOWN(18, 13),
//    SHOOT_RIGHT(19, 13),
//
//    DEATH(20, 6);

    init {
//        val image = texture(textureName)
//                .subTexture(Rectangle2D(0.0, Config.tileSize * 10.0, Config.tileSize * 9.0, Config.tileSize * 1.0))
//                .image
//
//        val imageDeath = texture(textureName)
//                .subTexture(Rectangle2D(0.0, Config.tileSize * 20.0, Config.tileSize * 6.0, Config.tileSize * 1.0))
//                .image

        channelIdle = AnimationChannel(texture(textureName).image, 9, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 9 * 10, 9 * 10)

        channelWalkDown = AnimationChannel(texture(textureName).image, 9, Config.spriteSize, Config.spriteSize, Duration.seconds(0.5), 9 * 10, 9 * 10 +  9 -1)
        channelWalkRight = AnimationChannel(texture(textureName).image, 9, Config.spriteSize, Config.spriteSize, Duration.seconds(0.5), 9 * 11, 9 * 11 +  9 -1)
        channelWalkUp = AnimationChannel(texture(textureName).image, 9, Config.spriteSize, Config.spriteSize, Duration.seconds(0.5), 9 * 8, 9 * 8 +  9 -1)
        channelWalkLeft = AnimationChannel(texture(textureName).image, 9, Config.spriteSize, Config.spriteSize, Duration.seconds(0.5), 9 * 9, 9 * 9 +  9 -1)

        channelSlashDown = AnimationChannel(texture(textureName).image, 6, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 6 * 14, 6 * 14 +  6 -1)
        channelSlashRight = AnimationChannel(texture(textureName).image, 6, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 6 * 15, 6 * 15 +  6 -1)
        channelSlashUp = AnimationChannel(texture(textureName).image, 6, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 6 * 12, 6 * 12 +  6 -1)
        channelSlashLeft = AnimationChannel(texture(textureName).image, 6, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 6 * 13, 6 * 13 +  6 -1)

        channelDeath = AnimationChannel(texture(textureName).image, 6, Config.spriteSize, Config.spriteSize, Duration.seconds(1.2), 6*20, 6*20 + 6 -1)

        animatedTexture = AnimatedTexture(channelWalkDown)
        animatedTexture.loop()
    }

    override fun onAdded() {
        entity.viewComponent.addChild(animatedTexture)
    }

    fun playDeath(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelDeath)
        animatedTexture.onCycleFinished = onFinished
    }

    fun loopIdle() {
        if (animatedTexture.animationChannel === channelIdle)
            return

        animatedTexture.loopAnimationChannel(channelIdle)
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