package com.almasb.zeph.entity

import com.almasb.fxgl.ecs.Entity
import com.almasb.zeph.Config
import javafx.collections.FXCollections

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Inventory {

    private val _items = FXCollections.observableArrayList<Entity>()

    //fun getItems() = FXCollections.unmodifiableObservableList(_items)

    fun getItems() = _items

    fun addItem(item: Entity) {
        _items.add(item)
    }

    fun removeItem(item: Entity) {
        _items.remove(item)
    }

    fun isFull() = _items.size >= Config.MAX_INVENTORY_SIZE
}