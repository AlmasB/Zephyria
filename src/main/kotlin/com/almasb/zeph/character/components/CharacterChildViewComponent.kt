package com.almasb.zeph.character.components

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import com.almasb.fxgl.ui.ProgressBar
import com.almasb.zeph.Config
import com.almasb.zeph.EntityType
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

        val view = makeView()
        viewRoot.children += view

        if (!entity.isType(EntityType.PLAYER)) {
            viewRoot.visibleProperty().bind(entity.viewComponent.parent.hoverProperty())
        }
    }

    private fun makeView(): Node {
        val barHP = makeHPBar()
        val barSP = makeSkillBar()

        barHP.translateX = 0.0
        barHP.translateY = 60.0
        barHP.setWidth(Config.SPRITE_SIZE * 1.0)
        barHP.setHeight(6.0)
        barHP.isLabelVisible = false

        barSP.translateX = 0.0
        barSP.translateY = barHP.translateY + 6
        barSP.setWidth(Config.SPRITE_SIZE * 1.0)
        barSP.setHeight(6.0)
        barSP.isLabelVisible = false

        barHP.maxValueProperty().bind(char.hp.maxValueProperty())
        barHP.currentValueProperty().bind(char.hp.valueProperty())

        barSP.maxValueProperty().bind(char.sp.maxValueProperty())
        barSP.currentValueProperty().bind(char.sp.valueProperty())

        val text = Text()
        text.font = Font.font(14.0)
        text.fill = Color.WHITE
        text.textProperty().bind(SimpleStringProperty((entity as CharacterEntity).data.description.name).concat(" Lv. ").concat(char.baseLevel))
        text.translateX = Config.SPRITE_SIZE.toDouble() / 2 - text.layoutBounds.width / 2
        text.translateY = 85.0

        return Group(barHP, barSP).also {
            if (!entity.isType(EntityType.PLAYER)) {
                it.children += text
            }
        }
    }

    private fun makeHPBar(): ProgressBar {
        val bar = ProgressBar(false)
        bar.innerBar.stroke = Color.GRAY
        bar.innerBar.arcWidthProperty().unbind()
        bar.innerBar.arcHeightProperty().unbind()
        bar.innerBar.arcWidthProperty().value = 0.0
        bar.innerBar.arcHeightProperty().value = 0.0
        bar.innerBar.heightProperty().unbind()
        bar.innerBar.heightProperty().value = 6.0
        bar.backgroundBar.effect = null
        bar.backgroundBar.fill = null
        bar.backgroundBar.strokeWidth = 0.25

        with(bar) {
            setHeight(25.0)
            setFill(Color.GREEN.brighter())
            setTraceFill(Color.GREEN.brighter())
            isLabelVisible = true
        }

        bar.innerBar.effect = null

        return bar
    }

    private fun makeSkillBar(): ProgressBar {
        val bar = ProgressBar(false)

        bar.innerBar.stroke = Color.GRAY
        bar.innerBar.arcWidthProperty().unbind()
        bar.innerBar.arcHeightProperty().unbind()
        bar.innerBar.arcWidthProperty().value = 0.0
        bar.innerBar.arcHeightProperty().value = 0.0
        bar.innerBar.heightProperty().unbind()
        bar.innerBar.heightProperty().value = 6.0
        bar.backgroundBar.effect = null
        bar.backgroundBar.fill = null
        bar.backgroundBar.strokeWidth = 0.25

        with(bar) {
            setHeight(25.0)
            setFill(Color.BLUE.brighter().brighter())
            setTraceFill(Color.BLUE)
            isLabelVisible = true
        }

        bar.innerBar.effect = null

        return bar
    }
}