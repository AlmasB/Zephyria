package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.zeph.Description
import com.almasb.zeph.getUITooltip
import com.almasb.zeph.item.Item
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Bounds
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextFlow

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TooltipView(val viewWidth: Double) : Parent(), ChangeListener<Bounds> {

    private val bg = Rectangle(viewWidth, 0.0)
    private val text = getUIFactoryService().newText("", Color.WHITE, 16.0)

    init {
        isMouseTransparent = true

        text.relocate(10.0, 10.0)
        text.wrappingWidth = viewWidth - 15*2

        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.fill = Color.color(0.0, 0.0, 0.0, 0.9)
        bg.stroke = Color.AQUA

        text.layoutBoundsProperty().addListener { _, _, newValue ->
            bg.height = newValue.height + 15*2
        }

        children += bg
        children += text
    }

    override fun changed(observable: ObservableValue<out Bounds>, oldValue: Bounds, newValue: Bounds) {
        if (newValue.height != 0.0) {
            bg.height = newValue.height + 15 * 2

            observable.removeListener(this)
        }
    }

    fun setNode(node: Node) {
        node.relocate(10.0, 10.0)

        if (node is TextFlow) {
            node.prefWidth = viewWidth - 15*2
        }

        children.set(1, node)

        if (node.layoutBounds.height == 0.0) {
            // layout hasn't been computed yet, so delay adjusting height
            node.layoutBoundsProperty().addListener(this)
        } else {
            bg.height = node.layoutBounds.height + 15*2
        }
    }

    fun setItem(item: Item) {
        setNode(item.dynamicTextFlow)
    }

    fun setText(text: String) {
        this.text.text = text
        children.set(1, this.text)
    }

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}

fun Node.setOnTooltipHover(tooltipAction: (TooltipView) -> Unit) {
    val tooltip = getUITooltip()

    this.hoverProperty().addListener { _, _, isHovering ->
        if (isHovering) {
            tooltipAction(tooltip)
            tooltip.show()
        } else {
            tooltip.hide()
        }
    }
}

private val NAME_COLOR = Color.WHITE
private val DESCRIPTION_COLOR = Color.DARKGRAY

private val NAME_FONT_SIZE = 16.0
private val DESCRIPTION_FONT_SIZE = 14.0

class TooltipTextFlow(val description: Description) : TextFlow() {

    init {
        children.setAll(
                getUIFactoryService().newText(description.name + "\n", NAME_COLOR, NAME_FONT_SIZE),
                getUIFactoryService().newText(description.description + "\n", DESCRIPTION_COLOR, DESCRIPTION_FONT_SIZE)
        )
    }
}