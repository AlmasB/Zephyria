package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.ui.FontType
import com.almasb.zeph.Config.Z_INDEX_DAMAGE_TEXT
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Gameplay {

    fun showDamage(damage: Int, isCritical: Boolean, position: Point2D) {
        val text = getUIFactoryService().newText(
                damage.toString() + if (isCritical) "!" else "",
                if (isCritical) Color.RED else Color.WHITE, FontType.GAME,
                if (isCritical) 28.0 else 26.0
        )

        text.stroke = if (isCritical) Color.RED else Color.WHITE
        val view = GameView(text, Z_INDEX_DAMAGE_TEXT)

        getGameScene().addGameView(view)

        animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .translate(text)
                .from(Point2D(position.x + FXGL.random(-25, 0), position.y))
                .to(position.add(FXGL.random(-25, 0).toDouble(), FXGL.random(-40, -25).toDouble()))
                .buildAndPlay()

        animationBuilder()
                .onFinished(Runnable { getGameScene().removeGameView(view) })
                .duration(Duration.seconds(2.15))
                .fadeOut(text)
                .buildAndPlay()
    }
}