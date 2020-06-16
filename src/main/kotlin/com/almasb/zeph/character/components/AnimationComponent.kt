package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.zeph.Config
import javafx.geometry.Rectangle2D
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
        val image = texture(textureName)
                .subTexture(Rectangle2D(0.0, Config.tileSize * 10.0, Config.tileSize * 9.0, Config.tileSize * 1.0))
                .image

        val imageDeath = texture(textureName)
                .subTexture(Rectangle2D(0.0, Config.tileSize * 20.0, Config.tileSize * 6.0, Config.tileSize * 1.0))
                .image

        channelIdle = AnimationChannel(texture(textureName).image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 9 * 10, 9 * 10)

        channelWalkDown = AnimationChannel(texture(textureName).image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 9 * 10, 9 * 10 +  9 -1)
        channelWalkRight = AnimationChannel(texture(textureName).image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 9 * 11, 9 * 11 +  9 -1)
        channelWalkUp = AnimationChannel(texture(textureName).image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 9 * 8, 9 * 8 +  9 -1)
        channelWalkLeft = AnimationChannel(texture(textureName).image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 9 * 9, 9 * 9 +  9 -1)

        channelSlashDown = AnimationChannel(texture(textureName).image, 6, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 6 * 14, 6 * 14 +  6 -1)
        channelSlashRight = AnimationChannel(texture(textureName).image, 6, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 6 * 15, 6 * 15 +  6 -1)
        channelSlashUp = AnimationChannel(texture(textureName).image, 6, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 6 * 12, 6 * 12 +  6 -1)
        channelSlashLeft = AnimationChannel(texture(textureName).image, 6, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 6 * 13, 6 * 13 +  6 -1)

        channelDeath = AnimationChannel(imageDeath, 6, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 0, 6 - 1)

        animatedTexture = AnimatedTexture(channelWalkDown)
        animatedTexture.loop()


        //    CharacterAnimation(int row, int cycle) {
//        this.row = row;
//        this.cycle = cycle;
//    }
//
//    @Override
//    public Rectangle2D area() {
//        return new Rectangle2D(0, Config.INSTANCE.getTileSize() * row, Config.INSTANCE.getTileSize() * cycle, Config.INSTANCE.getTileSize());
//    }
//
//    @Override
//    public int frames() {
//        return cycle;
//    }
//
//    @Override
//    public Duration duration() {
//        return Duration.seconds(1.2);
//    }
    }

    override fun onAdded() {
        entity.viewComponent.addChild(animatedTexture)
    }

    fun playDeath(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelDeath)
        animatedTexture.onCycleFinished = onFinished
    }

    fun loopIdle() {
        println("IDLE TODO")
        animatedTexture.loopAnimationChannel(channelIdle)
    }

    fun loopWalkUp() {
        println("loop up")
        animatedTexture.loopAnimationChannel(channelWalkUp)
    }

    fun loopWalkDown() {
        println("loop down")
        animatedTexture.loopAnimationChannel(channelWalkDown)
    }

    fun loopWalkRight() {
        println("loop right")
        animatedTexture.loopAnimationChannel(channelWalkRight)
    }

    fun loopWalkLeft() {
        println("loop left")
        animatedTexture.loopAnimationChannel(channelWalkLeft)
    }

    fun loopAttack() {
        println("ATTACK")
        animatedTexture.loopAnimationChannel(channelSlashDown)
    }
}