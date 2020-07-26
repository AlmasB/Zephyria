package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.texture
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.EquipPlace
import com.almasb.zeph.character.EquipPlace.*
import com.almasb.zeph.item.Item
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.Parent

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class EquipmentView(private val player: CharacterEntity) : Parent() {

    companion object {
        private val equipViewLocations = mapOf(
                HELM to Point2D(88.0, 60.0),
                BODY to Point2D(88.0, 105.0),
                SHOES to Point2D(88.0, 150.0),
                LEFT_HAND to Point2D(133.0, 105.0),
                RIGHT_HAND to Point2D(43.0, 105.0)
        )
    }

    init {
        children.addAll(
                texture("ui/inventory_left.png")
        )

        for (place in EquipPlace.values()) {
            val p = equipViewLocations[place]!!
            val equipView = EquipmentItemView(place, player.playerComponent!!.getEquip(place), p.x, p.y)

            player.playerComponent!!.equipProperty(place).addListener { _, _, newItem: Item ->
                equipView.item = newItem
                equipView.updateView()
            }

            children += equipView
        }
    }

    private inner class EquipmentItemView(val place: EquipPlace, var item: Item, x: Double, y: Double) : Parent() {

        init {
            relocate(x, y)

            isPickOnBounds = true
            cursor = Cursor.HAND

            updateView()

            setOnMouseClicked {
                player.playerComponent!!.unEquipItem(place)
            }

            setOnTooltipHover {
                it.setItem(item)
            }
        }

        fun updateView() {
            val itemView = FXGL.texture(item.description.textureName)

            children.setAll(itemView)
        }
    }
}
