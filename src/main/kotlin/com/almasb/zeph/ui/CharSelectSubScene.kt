package com.almasb.zeph.ui

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.Vec2
import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.scene.SubScene
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Rotate
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharSelectSubScene : SubScene() {

    init {
        val bg = Rectangle(getAppWidth() / 2.0, getAppHeight() / 2.0)

        val circle = Circle(bg.width / 2.0, bg.height / 2.0, 250.0)
        circle.stroke = Color.WHITE
        circle.strokeWidth = 2.5

        addChild(bg)

        val root = Pane()

        contentRoot.translateX = getAppWidth() / 2.0 - bg.width / 2.0
        contentRoot.translateY = getAppHeight() / 2.0 - bg.height / 2.0

        val vector = Point2D(0.0, 1.0).multiply(circle.radius)

        val firstPoint = Point2D(circle.centerX, circle.centerY).add(vector)



        root.children += circle
        root.children += Card(Color.BLUE).also {
            it.translateX = firstPoint.x - 120.0
            it.translateY = firstPoint.y - 150.0
        }

        // TODO: 120 is hardcoded = 360.0 / num_items
        val secondPoint = Point2D(circle.centerX, circle.centerY).add(
                Vec2.fromAngle(90.0 + 120).toPoint2D().multiply(circle.radius)
        )

        root.children += Card(Color.RED).also { card ->
            card.translateX = secondPoint.x - 120.0
            card.translateY = secondPoint.y - 150.0

            card.rotate = 120.0

            card.setOnMouseClicked {
                animationBuilder(this)
                        .duration(Duration.seconds(2.0))
                        .interpolator(Interpolators.BOUNCE.EASE_OUT())
                        .rotate(root)
                        .from(0.0)
                        .to(-card.rotate)
                        .origin(Point2D(circle.centerX, circle.centerY))
                        .buildAndPlay()

                //root.transforms += Rotate(-card.rotate, circle.centerX, circle.centerY)
            }
        }

        // 3rd
        // TODO: 120 is hardcoded = 360.0 / num_items
        val thirdPoint = Point2D(circle.centerX, circle.centerY).add(
                Vec2.fromAngle(90.0 + 120 + 120).toPoint2D().multiply(circle.radius)
        )

        root.children += Card(Color.GREEN).also {
            it.translateX = thirdPoint.x - 120.0
            it.translateY = thirdPoint.y - 150.0

            it.rotate = 120.0 + 120.0
        }

        addChild(root)


    }
}

private class Card(val color: Color) : Rectangle(240.0, 300.0) {
    init {
        fill = color
    }
}