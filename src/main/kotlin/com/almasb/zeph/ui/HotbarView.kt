package com.almasb.zeph.ui

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.dsl.*
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.skill.Skill
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Hotbar skills UI.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class HotbarView(private val player: CharacterEntity) : Parent() {

    private val BG_WIDTH = 767.0
    private val BG_HEIGHT = 110.0

    val minBtn = MinimizeButton("S", 368.0, -22.0, 0.0, BG_HEIGHT - 5, this)

    private val framesRoot = HBox()

    init {
        initView()
        initSkillFrames()
        initSkillListener()
    }

    private fun initView() {
        layoutX = FXGL.getAppWidth() / 2.0 - BG_WIDTH / 2.0
        layoutY = FXGL.getAppHeight() - BG_HEIGHT + 5.toDouble()

        val border = Rectangle(BG_WIDTH, BG_HEIGHT)
        border.strokeWidth = 2.0
        border.arcWidth = 10.0
        border.arcHeight = 10.0

        val borderShape = Shape.union(border, Circle(BG_WIDTH / 2.0, 0.0, 30.0))
        borderShape.fill = Color.rgb(25, 25, 25, 0.8)
        borderShape.stroke = Color.WHITE
        framesRoot.layoutX = 1.0

        children.addAll(borderShape, framesRoot, minBtn)
    }

    private fun initSkillFrames() {
        framesRoot.children.addAll(
                ItemView(KeyCode.Q),
                ItemView(KeyCode.W)
        )
        for (i in 1..9) {
            val view = SkillView(i)
            framesRoot.children.addAll(view)
        }
        framesRoot.children.addAll(
                ItemView(KeyCode.E),
                ItemView(KeyCode.R)
        )

        //framesRoot.setMouseTransparent(true);
        framesRoot.alignment = Pos.BASELINE_CENTER
    }

    private class SkillView(index: Int) : VBox(5.0) {
        val stack = StackPane()
        private var skill: Skill? = null

        fun setSkill(skill: Skill) {
            this.skill = skill

            stack.children[1] = texture(skill.data.description.textureName, 64.0, 64.0)

            cursor = Cursor.HAND

            // TODO: indicate skill is on cooldown or no mana
//        view.visibleProperty().bind(player.getSp().valueProperty().greaterThan(skill.getManaCost())
//                .and(skill.getCurrentCooldown().lessThanOrEqualTo(0)));

            setOnTooltipHover {
                it.setNode(skill.dynamicTextFlow)
            }
        }

        init {
            val frame = Rectangle(64.0, 64.0, null)
            frame.stroke = Color.AQUAMARINE.darker()
            frame.strokeWidth = 5.0

            stack.children.addAll(frame, Rectangle())
            val text = FXGL.getUIFactoryService().newText(index.toString() + "", Color.WHITE, 16.0)
            alignment = Pos.BASELINE_CENTER

            children.addAll(stack, text)
        }
    }

    private class ItemView(key: KeyCode) : VBox(5.0) {
        init {
            val frame = Rectangle(34.0, 34.0, null)
            frame.stroke = Color.YELLOW.darker()
            frame.strokeWidth = 2.0
            alignment = Pos.BASELINE_CENTER

            children.addAll(frame, getUIFactoryService().newText(key.toString(), Color.WHITE, 16.0))
        }
    }

    private fun initSkillListener() {
        player.characterComponent.skills.forEach {
            addSkill(it)
        }

        player.characterComponent.skills.addListener(ListChangeListener { c: ListChangeListener.Change<out Skill> ->
            while (c.next()) {
                if (c.wasAdded()) {
                    c.addedSubList.forEach { addSkill(it) }
                } else {
                    check(!c.wasRemoved()) { "Skills must never be removed" }
                }
            }
        } as ListChangeListener<in Skill>)
    }

    // this is OK since we don't remove skills, only add
    private var index = 0

    private fun addSkill(skill: Skill) {
        val frame = framesRoot.children.filterIsInstance(SkillView::class.java)[index]
        frame.setSkill(skill)

        val bg = Rectangle(64.0, 64.0, Color.color(0.0, 0.0, 0.0, 0.5))
        val btn = Text("+")
        btn.cursor = Cursor.HAND
        btn.stroke = Color.YELLOWGREEN.brighter()
        btn.strokeWidth = 3.0
        btn.font = Font.font(16.0)

        val stack = StackPane(bg, btn)
        stack.visibleProperty().bind(player.playerComponent!!.skillPoints.greaterThan(0).and(skill.levelProperty.lessThan(10)))
        frame.stack.children.add(stack)

        val skillIndex = index
        btn.setOnMouseClicked {
            player.playerComponent!!.increaseSkillLevel(skillIndex)
            FXGL.animationBuilder()
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .duration(Duration.seconds(1.0))
                    .scale(frame)
                    .from(Point2D(1.3, 1.3))
                    .to(Point2D(1.0, 1.0))
                    .buildAndPlay()
        }

        index++
    }
}
