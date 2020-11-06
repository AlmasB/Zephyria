package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.isReleaseMode
import com.almasb.fxgl.dsl.play
import javafx.geometry.Point2D
import javafx.scene.Parent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MinimizeButton(key: String, x: Double, y: Double, minimizedX: Double, private val minimizedY: Double, private val root: Parent) : StackPane() {

    var isMinimized = true
        private set

    private val bg = Rectangle(30.0, 20.0)

    init {
        bg.stroke = Color.WHITE

        val btnText = getUIFactoryService().newText(key)

        relocate(x, y)
        root.translateX = minimizedX
        root.translateY = minimizedY

        setOnMouseClicked { onClick() }

        children.addAll(bg, btnText)
    }

    fun onClick() {
        if (isReleaseMode()) {
            play("ui_slide.wav")
        }

        animationBuilder()
                .duration(Duration.seconds(0.33))
                .translate(root)
                .from(if (isMinimized) Point2D(0.0, minimizedY) else Point2D(0.0, 0.0))
                .to(if (isMinimized) Point2D(0.0, 0.0) else Point2D(0.0, minimizedY))
                .buildAndPlay()

        animationBuilder()
                .duration(Duration.seconds(0.33))
                .animate(bg.fillProperty())
                .from(Color.GOLD)
                .to(Color.BLACK)
                .buildAndPlay()

        isMinimized = !isMinimized
    }
}