package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.ui.FXGLScrollPane
import com.almasb.fxgl.ui.FontType
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

/**
 * UI for displaying in-game messages.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class MessagesView() : Parent() {

    private val BG_WIDTH = 450.0
    private val BG_HEIGHT = 170.0

    val minBtn = MinimizeButton("V", BG_WIDTH / 2.0 - 15.5, -22.0, 0.0, BG_HEIGHT - 5, this)

    private val text = getUIFactoryService().newText(
            "In-game messages area:\n",
            Color.WHITE, FontType.TEXT, 14.0
    )

    init {
        initView()
        initScrollPane()
    }

    private fun initView() {
        layoutX = 0.0
        layoutY = FXGL.getAppHeight() - BG_HEIGHT + 5.toDouble()

        val border = Rectangle(BG_WIDTH, BG_HEIGHT)
        border.strokeWidth = 2.0
        border.arcWidth = 10.0
        border.arcHeight = 10.0

        val borderShape = Shape.union(border, Circle(BG_WIDTH / 2.0, 0.0, 30.0))
        borderShape.fill = Color.rgb(25, 25, 25, 0.8)
        borderShape.stroke = Color.WHITE

        children.addAll(borderShape, minBtn)
    }

    private fun initScrollPane() {
        text.wrappingWidth = BG_WIDTH - 50.0

        val scrollPane = FXGLScrollPane(text)
        scrollPane.layoutX = 10.0
        scrollPane.layoutY = 10.0
        scrollPane.setPrefSize(BG_WIDTH - 20.0, BG_HEIGHT - 20.0)
        //scrollPane.padding = Insets(10.0)

        children += scrollPane
    }

    fun appendMessage(message: String) {
        text.text += "$message\n"
    }
}
