package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.zeph.item.Item
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TooltipView : Parent() {

    private val MAX_WIDTH = 300.0
    private val MAX_HEIGHT = 300.0

    private val bg = Rectangle(MAX_WIDTH, MAX_HEIGHT)
    private val text = getUIFactoryService().newText("", Color.WHITE, 18.0)

    init {
        isMouseTransparent = true

        text.relocate(10.0, 10.0)
        text.wrappingWidth = MAX_WIDTH - 15*2

        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.AQUA

        text.layoutBoundsProperty().addListener { _, _, newBounds ->
            bg.height = newBounds.height + 15*2
        }

        children += bg
        children += text
    }

    fun setItem(item: Item) {
        text.text = item.dynamicDescription.value
    }
}