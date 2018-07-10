package com.almasb.zeph.character.components

import com.almasb.fxgl.app.texture
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

    private val channelWalkDown: AnimationChannel
    private val channelDeath: AnimationChannel

    init {
        val image = texture(textureName)
                .subTexture(Rectangle2D(0.0, Config.tileSize * 10.0, Config.tileSize * 9.0, Config.tileSize * 1.0))
                .image

        val imageDeath = texture(textureName)
                .subTexture(Rectangle2D(0.0, Config.tileSize * 20.0, Config.tileSize * 6.0, Config.tileSize * 1.0))
                .image

        channelWalkDown = AnimationChannel(image, 9, Config.tileSize, Config.tileSize, Duration.seconds(1.2), 0,  9 - 1)
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
        entity.viewComponent.setView(animatedTexture)
    }

    fun playDeath(onFinished: Runnable) {
        animatedTexture.playAnimationChannel(channelDeath)
        animatedTexture.onCycleFinished = onFinished
    }
}