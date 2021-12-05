package com.almasb.zeph.character.components

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.inventory.Inventory
import com.almasb.fxgl.ui.MDIWindow
import com.almasb.zeph.Config
import com.almasb.zeph.ui.StorageView
import javafx.scene.layout.Pane

/**
 * For now, this stores various player-related gameplay data.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerWorldComponent : Component() {

    val storage = StorageView(Inventory(Config.MAX_STORAGE_SIZE))

    val storageWindow = MDIWindow().also {
        it.title = "Storage"
        it.canClose = false
        it.canResize = false
        it.canMinimize = false
        it.canMove = true
        it.contentPane = Pane(storage)

        it.setPrefSize(213.0, 420.0)
    }

    val isStorageOpen: Boolean
        get() = storage.scene != null
}