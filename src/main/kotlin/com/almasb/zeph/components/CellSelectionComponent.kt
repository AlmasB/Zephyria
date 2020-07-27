package com.almasb.zeph.components

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Config
import com.almasb.zeph.Gameplay
import com.almasb.zeph.ZephyriaApp
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CellSelectionComponent : Component() {

    override fun onUpdate(tpf: Double) {
        val cellX = (FXGL.getInput().mouseXWorld / Config.TILE_SIZE).toInt()
        val cellY = (FXGL.getInput().mouseYWorld / Config.TILE_SIZE).toInt()

        val grid = Gameplay.currentMap.grid

        if (!grid.isWithin(cellX, cellY)) {
            return
        }

        val cell = grid.get(cellX, cellY)

        if (cell.isWalkable) {
            entity.position = Point2D(cellX * Config.TILE_SIZE.toDouble(), cellY * Config.TILE_SIZE.toDouble())
        }
    }

    fun onClick() {
        val view = Rectangle(Config.TILE_SIZE * 1.0, Config.TILE_SIZE * 1.0, null)
        view.stroke = Color.GOLD

        entity.viewComponent.addChild(view)

        animationBuilder()
                .onFinished(Runnable { entity.viewComponent.removeChild(view) })
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.SMOOTH.EASE_OUT())
                .scale(view)
                .from(Point2D(1.0, 1.0))
                .to(Point2D.ZERO)
                .buildAndPlay()
    }
}