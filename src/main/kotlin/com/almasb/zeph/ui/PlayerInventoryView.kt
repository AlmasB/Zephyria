package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.components.PlayerWorldComponent
import com.almasb.zeph.item.*
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerInventoryView(private val player: CharacterEntity) : Parent() {

    private val BG_WIDTH = 200.0
    private val BG_HEIGHT = 240.0

    val minBtn = MinimizeButton("I", BG_WIDTH - 46.0, -22.0, 0.0, BG_HEIGHT, this)

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

        val equipView = EquipmentView(player)
        equipView.translateX = -BG_WIDTH

        val inventoryView = InventoryView(player.inventory, widthInCells = 5, heightInCells = 6)
        inventoryView.onItemClicked = this::onItemClicked

        val sortBtn = SortButton(inventoryView)
        sortBtn.translateX = -40.0
        sortBtn.translateY = 5.0

        children.addAll(borderShape, equipView, inventoryView, sortBtn, minBtn)
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
}

