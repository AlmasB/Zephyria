package com.almasb.zeph.ui.scenes

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.scene.SubScene
import com.almasb.zeph.CommandHandler
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.lang.Exception

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class DevScene : SubScene() {

    private val log = Logger.get(javaClass)

    private val field = TextField()

    init {
        val bg = Rectangle(getAppWidth().toDouble(), getAppHeight().toDouble(), Color.color(0.0, 0.0, 0.0, 0.5))

        field.prefWidth = getAppWidth() / 2.0
        field.translateX = 50.0
        field.translateY = getAppHeight() / 2.0

        field.setOnAction {
            try {
                handleCommand(field.text)
            } catch (e: Exception) {
                log.warning("Failed to handle command ${field.text}", e)
                e.printStackTrace()
            }

            FXGL.getSceneService().popSubScene()
        }

        field.sceneProperty().addListener { _, _, scene ->
            if (scene != null) {
                field.requestFocus()
            }
        }

        contentRoot.children += bg
        contentRoot.children += field
    }

    private fun handleCommand(input: String) {
        log.debug("Handling dev command: $input")

        val tokens = input.split(" +".toRegex())
        val cmd = tokens[0]

        val result = CommandHandler.call(cmd, tokens.drop(1).map { it.trim() }.toTypedArray())

        log.debug("Handled command: $input. Result: $result")
    }
}