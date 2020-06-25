package com.almasb.zeph.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Config
import com.almasb.zeph.Gameplay
import com.almasb.zeph.ZephyriaApp
import javafx.geometry.Point2D

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CellSelectionComponent : Component() {

    override fun onUpdate(tpf: Double) {
        val cellX = (FXGL.getInput().mouseXWorld / Config.TILE_SIZE).toInt()
        val cellY = (FXGL.getInput().mouseYWorld / Config.TILE_SIZE).toInt()

        val grid = Gameplay.getCurrentMap().grid

        if (!grid.isWithin(cellX, cellY)) {
            return
        }

        val cell = grid.get(cellX, cellY)

        if (cell.isWalkable) {
            entity.position = Point2D(cellX * Config.TILE_SIZE.toDouble(), cellY * Config.TILE_SIZE.toDouble())
        }
    }
}