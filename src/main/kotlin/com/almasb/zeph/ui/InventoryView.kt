package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.inventory.ItemStack
import com.almasb.zeph.Config
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.item.*
import javafx.collections.ListChangeListener
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.util.Pair
import java.util.function.Consumer

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class InventoryView(private val player: CharacterEntity) : Parent() {

    private val BG_WIDTH = 200.0
    private val BG_HEIGHT = 240.0

    val minBtn = MinimizeButton("I", BG_WIDTH - 46.0, -22.0, 0.0, BG_HEIGHT, this)

    // K - index, V - if free? TODO: double check
    private val slots: MutableMap<Int, Boolean> = HashMap()

    private val itemGroup = Group()
    private val listener: ListChangeListener<ItemStack<Item>>

    init {
        relocate(FXGL.getAppWidth() - 200.toDouble(), FXGL.getAppHeight() - 240.toDouble())

        val border = Rectangle(BG_WIDTH * 2 + 3, BG_HEIGHT + 5)
        border.strokeWidth = 2.0
        border.arcWidth = 10.0
        border.arcHeight = 10.0

        val borderShape = Shape.union(border, Circle((BG_WIDTH * 2 + 3 - 30), 0.0, 30.0))
        borderShape.fill = Color.rgb(25, 25, 25, 0.8)
        borderShape.stroke = Color.WHITE
        borderShape.translateX = -BG_WIDTH - 3

        val background = texture("ui/inventory_right.png")

        val equipView = EquipmentView(player)
        equipView.translateX = -BG_WIDTH

        children.addAll(borderShape, background, equipView, itemGroup, minBtn)

        for (i in 0 until Config.MAX_INVENTORY_SIZE) {
            slots[i] = true
        }

        // TODO: refactor
        listener = ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    for (item in change.addedSubList) {
                        addItem(item)
                    }
                } else if (change.wasRemoved()) {
                    for (stack in change.removed) {
                        val it = itemGroup.children.iterator()
                        while (it.hasNext()) {
                            val node = it.next()
                            if (node.userData != null) {
                                val data = node.userData as Pair<ItemStack<Item>, Int>
                                if (data.key === stack) {
                                    slots[data.value] = true
                                    it.remove()
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }

        player.inventory.itemsProperty().forEach { addItem(it) }
        player.inventory.itemsProperty().addListener(listener)
    }

    private fun getNextFreeSlot(): Int {
        for (i in 0 until Config.MAX_INVENTORY_SIZE) {
            if (slots[i]!!)
                return i
        }
        return -1
    }

    private fun addItem(stack: ItemStack<Item>) {
        val item = stack.userItem

        val index = getNextFreeSlot()
        slots[index] = false

        val view = StackPane(FXGL.texture(item.description.textureName))
        view.alignment = Pos.BOTTOM_RIGHT
        view.userData = Pair(stack, index)
        view.translateX = index % 5 * 40 + 3.toDouble()
        view.translateY = index / 5 * 40 + 3.toDouble()
        view.isPickOnBounds = true
        view.cursor = Cursor.HAND

        view.setOnTooltipHover { t: TooltipView ->
            t.setItem(item)
        }

        view.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                when (item) {
                    is Weapon -> {
                        player.playerComponent!!.equipWeapon(item)
                    }

                    is Armor -> {
                        player.playerComponent!!.equipArmor(item)
                    }

                    is UsableItem -> {
                        player.characterComponent.useItem(item)
                    }

                    is MiscItem -> {
                        // ignore misc items, can't interact
                    }
                }
            }
        }

        val text = FXGL.getUIFactoryService().newText("", Color.WHITE, 12.0)

        text.textProperty().bind(player.inventory.getData(item).get().quantityProperty().asString())
        text.strokeWidth = 1.5

        view.children.addAll(text)

        itemGroup.children.add(view)
    }
}