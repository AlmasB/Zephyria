package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import com.almasb.fxgl.ui.ProgressBar
import com.almasb.zeph.Config
import com.almasb.zeph.character.CharacterEntity
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterChildViewComponent : ChildViewComponent(0.0, 10.0, isTransformApplied = false) {

    private lateinit var char: CharacterComponent

    override fun onAdded() {
        super.onAdded()

        char = entity.getComponentOptional(CharacterComponent::class.java)
                .orElseGet { entity.getComponent(PlayerComponent::class.java) }

        val view = makeView()
        viewRoot.children += view

        if (char !is PlayerComponent) {
            viewRoot.visibleProperty().bind(entity.viewComponent.parent.hoverProperty())
        }
    }

    private fun makeView(): Node {
        val barHP = makeHPBar()
        val barSP = makeSkillBar()

        barHP.translateX = 0.0
        barHP.translateY = 80.0
        barHP.setWidth(Config.tileSize.toDouble())
        barHP.setHeight(10.0)
        barHP.isLabelVisible = false

        barSP.translateX = 0.0
        barSP.translateY = 90.0
        barSP.setWidth(Config.tileSize.toDouble())
        barSP.setHeight(10.0)
        barSP.isLabelVisible = false

        barHP.maxValueProperty().bind(char.hp.maxValueProperty())
        barHP.currentValueProperty().bind(char.hp.valueProperty())

        barSP.maxValueProperty().bind(char.sp.maxValueProperty())
        barSP.currentValueProperty().bind(char.sp.valueProperty())

        val text = Text()
        text.font = Font.font(14.0)
        text.fill = Color.WHITE
        text.textProperty().bind(SimpleStringProperty((entity as CharacterEntity).data.description.name).concat(" Lv. ").concat(char.baseLevel))
        text.translateX = Config.tileSize.toDouble() / 2 - text.layoutBounds.width / 2
        text.translateY = 75.0

        return Group(barHP, barSP, text)
    }

    private fun makeHPBar(): ProgressBar {
        val bar = ProgressBar(false)

        with(bar) {
            setHeight(25.0)
            setFill(Color.GREEN.brighter())
            setTraceFill(Color.GREEN.brighter())
            isLabelVisible = true
        }

        return bar
    }

    private fun makeSkillBar(): ProgressBar {
        val bar = ProgressBar(false)

        with(bar) {
            setHeight(25.0)
            setFill(Color.BLUE.brighter().brighter())
            setTraceFill(Color.BLUE)
            isLabelVisible = true
        }

        return bar
    }
}