package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.image
import com.almasb.fxgl.inventory.Inventory
import com.almasb.fxgl.inventory.ItemStack
import com.almasb.fxgl.ui.FXGLScrollPane
import com.almasb.zeph.Gameplay
import com.almasb.zeph.MouseGestures
import com.almasb.zeph.data.Data
import com.almasb.zeph.item.Item
import com.almasb.zeph.item.UsableItem
import com.almasb.zeph.item.Weapon
import javafx.collections.ListChangeListener
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class InventoryView(val inventory: Inventory<Item>,

                    /**
                     * Width and height in number of cells.
                     */
                    val widthInCells: Int, val heightInCells: Int) : Parent() {

    private val contentPane = Pane()

    private val scrollPane = FXGLScrollPane(contentPane)

    private val itemGroup = Group()
    private val listener: ListChangeListener<ItemStack<Item>>

    private val itemPanes
        get() = itemGroup.children.map { it as ItemPane }

    private val mouseGestures = MouseGestures(itemGroup)

    var onItemClicked: (Item) -> Unit = {}

    val layoutWidth: Double
        get() = scrollPane.prefWidth

    val layoutHeight: Double
        get() = scrollPane.prefHeight

    init {
        val bg = Rectangle(40.0 * widthInCells, inventory.capacity / widthInCells * 40.0)
        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.AQUA
        bg.strokeWidth = 1.5


        scrollPane.setPrefSize(bg.width + bg.strokeWidth * 2 + 10.0, 40.0 * heightInCells)
        scrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        contentPane.background = Background(BackgroundImage(image("ui/item_frame.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, null, null))

        children += scrollPane

        contentPane.setPrefSize(bg.width, bg.height)
        contentPane.maxHeight = bg.height

        // populate item panes
        for (y in 0 until inventory.capacity / widthInCells) {
            for (x in 0 until widthInCells) {
                val pane = ItemPane()
                pane.translateX = x * 40 + 3.toDouble()
                pane.translateY = y * 40 + 3.toDouble()

                mouseGestures.makeDraggable(pane) {
                    swap(pane)
                }

                pane.setOnMouseClicked {
                    if (pane.isEmpty)
                        return@setOnMouseClicked

                    if (it.button == MouseButton.PRIMARY && it.clickCount == 2) {
                        val item = pane.itemStack!!.userItem
                        onItemClicked(item)
                    }
                }

                itemGroup.children += pane
            }
        }

        contentPane.children += itemGroup

        listener = ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    for (item in change.addedSubList) {
                        addItem(item)
                    }
                } else if (change.wasRemoved()) {
                    for (stack in change.removed) {
                        itemPanes.filter { it.itemStack === stack}
                                .forEach {
                                    it.itemStack = null
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

    private fun swap(pane: ItemPane) {
        val closestPane = itemPanes
                .sortedBy {
                    Point2D(it.translateX, it.translateY)
                            .distance(Point2D(pane.layoutX + pane.translateX, pane.layoutY + pane.translateY))
                }
                .first()

        pane.relocate(0.0, 0.0)

        if (closestPane === pane)
            return

        // TODO: implement proper swap, now we need to set to null to clean up
        val stack = pane.itemStack

        pane.itemStack = null
        pane.itemStack = closestPane.itemStack
        closestPane.itemStack = null
        closestPane.itemStack = stack
    }

    private fun getNextFreeSlot(): ItemPane? {
        return itemPanes.find { it.isEmpty }
    }

    private fun addItem(stack: ItemStack<Item>) {
        getNextFreeSlot()?.let {
            it.itemStack = stack
        }
    }
}

private class ItemPane : StackPane() {

    private val text = FXGL.getUIFactoryService().newText("", Color.WHITE, 12.0)

    var itemStack: ItemStack<Item>? = null
        set(value) {
            if (value == null) {

                if (itemStack != null) {
                    children.removeAt(0)

                    text.textProperty().unbind()
                    text.text = ""
                }

            } else {
                val view = FXGL.texture(value.userItem.description.textureName)

                children.add(0, view)

                text.textProperty().bind(value.quantityProperty().asString())

                // TODO: remove when value == null?
                setOnTooltipHover { t: TooltipView ->
                    t.setItem(value.userItem)
                }
            }

            field = value
        }

    val isEmpty: Boolean
        get() = itemStack == null

    init {
        alignment = Pos.BOTTOM_RIGHT
        isPickOnBounds = true
        cursor = Cursor.HAND

        text.strokeWidth = 1.5

        children += text
    }
}

class StorageView(val inventory: Inventory<Item>) : Parent() {
    val inventoryView = InventoryView(inventory, 20, 4)

    val closeButton = getUIFactoryService().newButton("Close")

    init {
        closeButton.fontProperty().unbind()
        closeButton.font = Font.font(11.0)
        closeButton.setPrefSize(213.0, 13.0)
        closeButton.setOnAction {
            Gameplay.closeStorage()
        }

        val vbox = VBox(inventoryView, closeButton)
        vbox.alignment = Pos.TOP_CENTER

        children += vbox
    }
}