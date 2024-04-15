package com.almasb.zeph.ui.scenes

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.Vec2
import com.almasb.fxgl.dsl.animationBuilder
import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.scene.SubScene
import com.almasb.zeph.character.CharacterClass
import javafx.animation.FillTransition
import javafx.animation.ParallelTransition
import javafx.animation.TranslateTransition
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Glow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Rotate
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharSelectSubScene(val first: CharacterClass, vararg classes: CharacterClass) : SubScene() {

    private var selected: CharClassView

    var onSelected: (CharacterClass) -> Unit = {}

    init {
        // CIRCLE BEGIN
        val circleRoot = Pane()

        val bg = Rectangle(getAppWidth() / 1.5, getAppHeight() / 1.5)
        bg.strokeWidth = 2.0
        bg.stroke = Color.LIGHTBLUE

        contentRoot.translateX = getAppWidth() / 2.0 - bg.width / 2.0
        contentRoot.translateY = getAppHeight() / 2.0 - bg.height / 2.0

        val circle = Circle(bg.width / 2.0, bg.height / 2.0, 250.0, null)
        circle.stroke = Color.WHITE
        circle.strokeWidth = 2.5

        for (angle in 0..359 step 10) {
            val v = Vec2.fromAngle(angle.toDouble()).toPoint2D()

            val pt = Point2D(circle.centerX, circle.centerY).add(v.multiply(circle.radius))

            val line = Line(pt.x, pt.y, pt.x + -v.x * 20, pt.y + -v.y * 20)
            line.stroke = circle.stroke

            circleRoot.children += line
        }

        circleRoot.children += circle

        addChild(bg)

        // CIRCLE END

        val numItems = 1 + classes.size
        val interval = 360.0 / numItems

        val views = (listOf(first) + classes).mapIndexed { index, type ->

            val point = Point2D(circle.centerX, circle.centerY).add(
                    Vec2.fromAngle(90.0 + interval * (index)).toPoint2D().multiply(circle.radius)
            )

            val view = CharClassView(type)
            view.translateX = point.x - view.frameWidth / 2.0
            view.translateY = point.y - view.frameHeight / 2.0

            view.rotate = interval * (index)

            view.setOnMouseClicked {
                selected = view

                animationBuilder(this)
                        .duration(Duration.seconds(1.2))
                        .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                        .rotate(circleRoot)
                        .from(
                                if (circleRoot.transforms.isNotEmpty())
                                    (circleRoot.transforms[0] as Rotate).angle
                                else
                                    0.0
                        )
                        .to(-view.rotate)
                        .origin(Point2D(circle.centerX, circle.centerY))
                        .buildAndPlay()
            }

            circleRoot.children += view

            view
        }

        selected = views.first()

        addChild(circleRoot)





        val switch = MaleFemaleToggleSwitch()
        switch.translateX = bg.width - 200.0
        switch.translateY = 50.0
        addChild(switch)

        val btnSelect = getUIFactoryService().newButton("Select")
        btnSelect.translateX = bg.width - 220.0
        btnSelect.translateY = bg.height - 60.0
        btnSelect.setOnAction {
            onSelected.invoke(selected.type)
        }

        addChild(btnSelect)

        contentRoot.effect = DropShadow(30.0, Color.BLACK)
    }
}

private class CharClassView(val type: CharacterClass) : Parent() {

    private val glow = Glow(0.8)

    val frameWidth = 250.0
    val frameHeight = 300.0

    init {
        val frame = Rectangle(frameWidth, frameHeight, Color.BLACK)
        frame.stroke = Color.MEDIUMAQUAMARINE

        val title = getUIFactoryService().newText(type.toString(), 22.0)

        val text = getUIFactoryService().newText(type.description)
        text.wrappingWidth = frame.width - 15.0

        val stack = StackPane(frame, VBox(title, text).also { it.alignment = Pos.CENTER })

        children.addAll(stack)

        setOnMouseEntered {
            effect = glow
        }

        setOnMouseExited {
            effect = null
        }

//        setOnMouseClicked {
//            isMale = !isMale
//
//            stack.children.removeAt(1)
//
//            val rt = RotateTransition(Duration.seconds(0.4), stack)
//            rt.axis = Rotate.Y_AXIS
//            rt.fromAngle = if (!isMale) 0.0 else 180.0
//            rt.toAngle = if (!isMale) 180.0 else 0.0
//            rt.setOnFinished { stack.children.add(if (isMale) t1 else t2) }
//            rt.play()
//        }

        // TODO: isSelected
//        scaleXProperty().bind(
//                Bindings.`when`(hoverProperty()).then(1.0).otherwise(0.75)
//        )
//
//        scaleYProperty().bind(
//                Bindings.`when`(hoverProperty()).then(1.0).otherwise(0.75)
//        )


    }
}
























/**
 * On/Off toggle switch.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
class MaleFemaleToggleSwitch : Parent() {

    private var animating = false
    private val switchedOn = ReadOnlyBooleanWrapper(false)

    fun switchedOnProperty(): ReadOnlyBooleanProperty {
        return switchedOn.readOnlyProperty
    }

    /**
     * @return true if switch is ON, false - if OFF
     */
    fun isSwitchedOn(): Boolean {
        return switchedOnProperty().get()
    }

    private val translateAnimation = TranslateTransition(Duration.seconds(0.25))
    private val fillAnimation = FillTransition(Duration.seconds(0.25))
    private val animation = ParallelTransition(translateAnimation, fillAnimation)
    private var colorOn = Color.LIGHTGREEN

    fun setFill(colorOn: Color) {
        this.colorOn = colorOn
    }

    init {
        val background = Rectangle(100.0, 50.0)
        background.fill = Color.WHITE
        background.stroke = Color.LIGHTGRAY
        background.arcWidth = 50.0
        background.arcHeight = 50.0

        val trigger = Circle(25.0)
        trigger.centerX = 25.0
        trigger.centerY = 25.0
        trigger.fill = Color.WHITE
        trigger.stroke = Color.LIGHTGRAY

        translateAnimation.node = trigger
        fillAnimation.shape = background

        val M = getUIFactoryService().newText("M")
        val F = getUIFactoryService().newText("F")

        M.translateX = -25.0
        F.translateX = background.width + 15.0

        children.addAll(background, trigger, M, F)

        switchedOnProperty().addListener { obs: ObservableValue<out Boolean>?, oldState: Boolean?, isOn: Boolean ->
            if (animating) {
                animation.stop()
            }
            animating = true
            translateAnimation.setToX(if (isOn) 100 - 50.0 else 0.toDouble())
            fillAnimation.fromValue = if (isOn) Color.WHITE else colorOn
            fillAnimation.toValue = if (isOn) colorOn else Color.WHITE
            animation.onFinished = EventHandler { e: ActionEvent? -> animating = false }
            animation.play()
        }

        onMouseClicked = EventHandler { event: MouseEvent? ->
            if (!animating)
                switchedOn.set(!isSwitchedOn())
        }
    }
}