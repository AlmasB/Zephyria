package com.almasb.zeph.components

import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Config.TILE_SIZE
import javafx.geometry.Rectangle2D
import javafx.scene.image.ImageView

/**
 * TODO: properly document what this does.
 *
 * There is a javafx bug (limitation?) wrt large textures being drawn at the same time.
 * This is likely to occur when dealing with large tmx maps since we (per layer) construct
 * one giant image. Storage of the image seems fine, but drawing causes problems.
 * Hence, we optimize drawing by pre-cropping the image view to an appropriate viewport.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class TiledMapLayerOptimizerComponent : Component() {

    private lateinit var mapView: ImageView

    override fun onAdded() {
        mapView = entity.viewComponent.children[0] as ImageView
    }

    override fun onUpdate(tpf: Double) {
        val viewportOrigin = getGameScene().viewport.origin

        // +- TILE_SIZE to leave some offscreen offset for seamless rendering
        mapView.relocate(viewportOrigin.x - TILE_SIZE, viewportOrigin.y - TILE_SIZE)

        mapView.viewport = Rectangle2D(
                viewportOrigin.x - TILE_SIZE,
                viewportOrigin.y - TILE_SIZE,
                getAppWidth() / getGameScene().viewport.getZoom() + TILE_SIZE * 2,
                getAppHeight() / getGameScene().viewport.getZoom() + TILE_SIZE * 2
        )
    }
}