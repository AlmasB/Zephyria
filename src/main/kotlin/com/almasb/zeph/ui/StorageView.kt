package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.inventory.Inventory
import com.almasb.fxgl.inventory.ItemStack
import com.almasb.fxgl.ui.FXGLScrollPane
import com.almasb.zeph.Config
import com.almasb.zeph.Gameplay
import com.almasb.zeph.data.Data
import com.almasb.zeph.item.*
import javafx.collections.ListChangeListener
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.util.Pair

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class StorageView(val inventory: Inventory<Item>) : Parent() {

    private val slots: MutableMap<Int, Boolean> = HashMap()
    private val itemGroup = Group()
    private val listener: ListChangeListener<ItemStack<Item>>

    val closeButton = getUIFactoryService().newButton("Close")

    init {
        val bg = Rectangle(40.0 * 5, 40.0 * Config.MAX_STORAGE_SIZE / 5)
        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.AQUA
        bg.strokeWidth = 1.5

        itemGroup.children += bg

        closeButton.fontProperty().unbind()
        closeButton.font = Font.font(11.0)
        closeButton.setPrefSize(213.0, 13.0)
        closeButton.setOnAction {
            Gameplay.closeStorage()
        }

        for (y in 0 until Config.MAX_STORAGE_SIZE / 5) {
            for (x in 0 until 5) {
                val itemFrame = texture("ui/item_frame.png")
                itemFrame.translateX = x * 40.0
                itemFrame.translateY = y * 40.0

                itemGroup.children += itemFrame
            }
        }

        val scrollPane = FXGLScrollPane(itemGroup)
        scrollPane.setPrefSize(bg.width + bg.strokeWidth * 2 + 10.0, 40.0 * 10)

        val vbox = VBox(scrollPane, closeButton)
        vbox.alignment = Pos.TOP_CENTER

        children += vbox

        for (i in 0 until Config.MAX_STORAGE_SIZE) {
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

        inventory.itemsProperty().forEach { addItem(it) }
        inventory.itemsProperty().addListener(listener)
        
        // TEST

        inventory.add(UsableItem(Data.UsableItems.MANA_POTION))
        inventory.add(UsableItem(Data.UsableItems.HEALING_POTION))

        inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
        inventory.add(UsableItem(Data.UsableItems.TELEPORTATION_STONE))
        inventory.add(Weapon(Data.Weapons.OneHandedSwords.GUARD_SWORD))
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
                Gameplay.player.inventory.transferFrom(inventory, item)
            }
        }

        val text = FXGL.getUIFactoryService().newText("", Color.WHITE, 12.0)

        text.textProperty().bind(stack.quantityProperty().asString())
        text.strokeWidth = 1.5

        view.children.addAll(text)

        itemGroup.children.add(view)
    }
}