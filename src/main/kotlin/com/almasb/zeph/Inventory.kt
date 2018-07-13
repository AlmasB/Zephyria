package com.almasb.zeph

import com.almasb.zeph.item.Item
import javafx.collections.FXCollections

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Inventory {

    val items = FXCollections.observableArrayList<Item>()

    fun addItem(item: Item) {
        items.add(item)
    }

    fun removeItem(item: Item) {
        items.remove(item)
    }

    fun isFull() = items.size >= Config.MAX_INVENTORY_SIZE
}