package com.almasb.zeph.ui

import com.almasb.fxgl.animation.AnimatedValue
import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.app.scene.LoadingScene
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.dsl.getUIFactoryService
import javafx.animation.Interpolator
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.util.function.Consumer

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephLoadingScene : LoadingScene() {

    init {
        val text = getUIFactoryService().newText("Loading Zephyria...", Color.WHITE, 72.0)
        FXGL.centerText(text)

        animationBuilder(this)
                .repeatInfinitely()
                .duration(Duration.seconds(1.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .animate(AnimatedString("", "Loading Zephyria..."))
                .onProgress(Consumer { text.text = it })
                .buildAndPlay()

        contentRoot.children.addAll(Rectangle(appWidth.toDouble(), appHeight.toDouble()))
        contentRoot.children.add(text)
    }
}

private class AnimatedString(from: String, to: String)
    : AnimatedValue<String>(from, to) {

    override fun animate(val1: String, val2: String, progress: Double, interpolator: Interpolator): String {
        // case 1: val1 \in val2

        val index = val2.length * interpolator.interpolate(0.0, 1.0, progress)
        return val2.substring(0, index.toInt())
    }
}