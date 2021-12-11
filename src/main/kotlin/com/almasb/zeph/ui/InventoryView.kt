package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.texture
import com.almasb.fxgl.inventory.ItemStack
import com.almasb.zeph.MouseGestures
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.components.PlayerWorldComponent
import com.almasb.zeph.item.*
import javafx.collections.ListChangeListener
import javafx.geometry.Point2D
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

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class InventoryView(private val player: CharacterEntity) : Parent() {

    private val BG_WIDTH = 200.0
    private val BG_HEIGHT = 240.0

    val minBtn = MinimizeButton("I", BG_WIDTH - 46.0, -22.0, 0.0, BG_HEIGHT, this)

    // K - index, V - if free? TODO: double check
    //private val slots: MutableMap<Int, Boolean> = HashMap()

    private val itemGroup = Group()
    private val listener: ListChangeListener<ItemStack<Item>>

    private val itemPanes
        get() = itemGroup.children.map { it as ItemPane }

    private val mouseGestures = MouseGestures(itemGroup)

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

        // populate item panes
        for (y in 0..5) {
            for (x in 0..4) {
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

        player.inventory.itemsProperty().forEach { addItem(it) }
        player.inventory.itemsProperty().addListener(listener)
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

    private fun onItemClicked(item: Item) {
        if (player.getComponent(PlayerWorldComponent::class.java).isStorageOpen) {

            player.getComponent(PlayerWorldComponent::class.java)
                    .storage
                    .inventory
                    .transferFrom(player.inventory, item)

        } else {

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