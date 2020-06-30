package com.almasb.zeph.components

import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.Config.TILE_SIZE
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import kotlin.math.max

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

    private val MIN_MARGIN = 2
    private val MARGIN_TILES = 5

    private val MIN_MARGIN_PIXELS = MIN_MARGIN * TILE_SIZE

    // TODO: hardcoded to zoom, i.e. no response to changes in zoom
    private val W = (getAppWidth() / getGameScene().viewport.getZoom() + TILE_SIZE * MARGIN_TILES * 2).toInt()
    private val H = (getAppHeight() / getGameScene().viewport.getZoom() + TILE_SIZE * MARGIN_TILES * 2).toInt()

    private val frontBuffer: WritableImage = WritableImage(W, H)
    private var frontBufferView = ImageView(frontBuffer)

    private val backBuffer: WritableImage = WritableImage(W, H)
    private var backBufferView = ImageView(backBuffer)

    private lateinit var mapView: ImageView

    override fun onAdded() {
        mapView = entity.viewComponent.children[0] as ImageView
        entity.viewComponent.removeChild(mapView)

        // relocate to center
        entity.viewComponent.addChild(frontBufferView)

        val viewportOrigin = getGameScene().viewport.origin

        val srcX = maxOf((viewportOrigin.x - TILE_SIZE * MARGIN_TILES).toInt(), 0)
        val srcY = maxOf((viewportOrigin.y - TILE_SIZE * MARGIN_TILES).toInt(), 0)

        (frontBufferView.image as WritableImage).pixelWriter.setPixels(
                if (srcX == 0) MARGIN_TILES * TILE_SIZE else 0,
                if (srcY == 0) MARGIN_TILES * TILE_SIZE else 0,
                if (srcX == 0) W - MARGIN_TILES * TILE_SIZE else W,
                if (srcY == 0) H - MARGIN_TILES * TILE_SIZE else H,
                mapView.image.pixelReader,
                srcX, srcY
        )

        frontBufferView.layoutX = viewportOrigin.x - TILE_SIZE * MARGIN_TILES
        frontBufferView.layoutY = viewportOrigin.y - TILE_SIZE * MARGIN_TILES
        backBufferView.layoutX = viewportOrigin.x - TILE_SIZE * MARGIN_TILES
        backBufferView.layoutY = viewportOrigin.y - TILE_SIZE * MARGIN_TILES
    }

    override fun onUpdate(tpf: Double) {
        val viewportOrigin = getGameScene().viewport.origin

        if (frontBufferView.layoutX + W - (viewportOrigin.x + getAppWidth() / getGameScene().viewport.getZoom()) < MIN_MARGIN_PIXELS) {

            flipBuffers()
        } else if (viewportOrigin.x - frontBufferView.layoutX < MIN_MARGIN_PIXELS) {

            flipBuffers()
        } else if (viewportOrigin.y - frontBufferView.layoutY < MIN_MARGIN_PIXELS) {

            flipBuffers()
        } else if (frontBufferView.layoutY + H - (viewportOrigin.y + getAppHeight() / getGameScene().viewport.getZoom()) < MIN_MARGIN_PIXELS) {

            flipBuffers()
        }
    }

    // TODO: potentially load dynamically, generate image on the fly from sprite sheet?
    private fun flipBuffers() {
        val viewportOrigin = getGameScene().viewport.origin

        val srcX = (viewportOrigin.x - TILE_SIZE * MARGIN_TILES).toInt()
        val srcY = (viewportOrigin.y - TILE_SIZE * MARGIN_TILES).toInt()

        val dstX = if (srcX < 0) -srcX else 0
        val dstY = if (srcY < 0) -srcY else 0

        (backBufferView.image as WritableImage).pixelWriter.setPixels(
                dstX,
                dstY,
                minOf(W - dstX, mapView.image.width.toInt() - srcX),
                minOf(H - dstY, mapView.image.height.toInt() - srcY),
                mapView.image.pixelReader,
                maxOf(srcX, 0),
                maxOf(srcY, 0)
        )

        entity.viewComponent.addChild(backBufferView)
        entity.viewComponent.removeChild(frontBufferView)

        val tmp = frontBufferView
        frontBufferView = backBufferView
        backBufferView = tmp

        frontBufferView.layoutX = viewportOrigin.x - TILE_SIZE * MARGIN_TILES
        frontBufferView.layoutY = viewportOrigin.y - TILE_SIZE * MARGIN_TILES
    }
}