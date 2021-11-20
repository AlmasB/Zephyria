package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.scene.SubScene
import javafx.scene.effect.BlendMode
import javafx.scene.input.KeyCode
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class VideoSubScene : SubScene() {

    init {
        try {
            // TODO: allow adding custom loaders to asset loader?
            val video = Media(javaClass.getResource("/assets/videos/codefest_xmas.mp4").toExternalForm())

            val player = MediaPlayer(video)
            player.isAutoPlay = true
            player.setOnEndOfMedia {
                FXGL.getSceneService().popSubScene()
            }

            val view = MediaView(player)
            view.fitWidth = getAppWidth().toDouble()
            view.fitHeight = getAppHeight().toDouble()
            view.blendMode = BlendMode.HARD_LIGHT

            contentRoot.children += view

            val text = getUIFactoryService().newText("Press F to skip", Color.BLACK, 40.0)
            FXGL.centerText(text, getAppWidth() / 2.0, 60.0)

            contentRoot.children += text

            FXGL.onKeyBuilder(input, KeyCode.F)
                    .onActionBegin {
                        player.stop()
                        FXGL.getSceneService().popSubScene()
                    }
        } catch (e: Exception) {
            println("Could not load video: ")
            e.printStackTrace()

            runOnce({
                FXGL.getSceneService().popSubScene()
            }, Duration.millis(15.0))
        }
    }
}