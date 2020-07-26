package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.ui.FXGLScrollPane
import com.almasb.zeph.Config
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class StorageView() : Parent() {

    init {
        val bg = Rectangle(40.0 * 5, 40.0 * Config.MAX_STORAGE_SIZE / 5)
        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.AQUA
        bg.strokeWidth = 1.5

        val group = Group(bg)

        for (y in 0 until Config.MAX_STORAGE_SIZE / 5) {
            for (x in 0 until 5) {
                val itemFrame = texture("ui/item_frame.png")
                itemFrame.translateX = x * 40.0
                itemFrame.translateY = y * 40.0

                group.children += itemFrame
            }
        }

        val scrollPane = FXGLScrollPane(group)
        scrollPane.setPrefSize(bg.width + bg.strokeWidth * 2 + 10.0, 40.0 * 10)

        children += scrollPane
    }
}