package com.almasb.zeph.ui.scenes

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.scene.SubScene
import com.almasb.fxgl.ui.FontType
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.CombatMove
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration

/**
 * @author Almas Baim (https://github.com/AlmasB)
 */
class CombatSubScene : SubScene() {

    private val combatRoot = Group()

    init {
        contentRoot.children += combatRoot
    }

    fun start(char1: CharacterEntity, char2: CharacterEntity) {
        val view1 = CombatView(char1)
        val view2 = CombatView(char2)

        view1.translateX = 50.0
        view1.translateY = 50.0

        view2.translateX = 750.0
        view2.translateY = 50.0

        val closeBtn = getUIFactoryService().newButton("Close")
        closeBtn.setOnAction {
            FXGL.getSceneService().popSubScene()
        }

        closeBtn.translateX = 400.0
        closeBtn.translateY = 350.0

        val bg = Rectangle(1000.0, 600.0, Color.BLACK)
        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.AQUAMARINE
        bg.strokeWidth = 5.0

        val btnSelect = getUIFactoryService().newButton("Select Move")
        btnSelect.translateX = 100.0
        btnSelect.translateY = 350.0
        btnSelect.setOnAction {
            btnSelect.isDisable = true

            val playerMove = view1.choiceBox.selectionModel.selectedItem

            val aiMove = FXGLMath.random(CombatMove.values()).get()

            val playerDamage = playerMove.getDamageModifierAgainst(aiMove)
            val aiDamage = aiMove.getDamageModifierAgainst(playerMove)

            println("$playerMove vs $aiMove: $playerDamage vs $aiDamage")

            animationBuilder(this)
                    .onFinished {
                        char1.characterComponent.attack(char2)

                        // TODO: check if dead

                        animationBuilder(this)
                                .onFinished {
                                    char2.characterComponent.attack(char1)

                                    btnSelect.isDisable = false
                                }
                                .duration(Duration.seconds(0.5))
                                .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                                .repeat(2)
                                .autoReverse(true)
                                .translate(view2)
                                .to(Point2D(view1.translateX, view1.translateY))
                                .buildAndPlay()
                    }
                    .duration(Duration.seconds(0.5))
                    .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                    .repeat(2)
                    .autoReverse(true)
                    .translate(view1)
                    .to(Point2D(view2.translateX, view2.translateY))
                    .buildAndPlay()
        }

        combatRoot.children.setAll(bg, view1, view2, btnSelect, closeBtn)

        combatRoot.translateX = getAppWidth() / 2.0 - bg.width / 2.0
        combatRoot.translateY = getAppHeight() / 2.0 - bg.height / 2.0
    }

    private class CombatView(val char: CharacterEntity) : VBox() {

        val choiceBox = getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(CombatMove.values().toList()))

        init {
            val text = getUIFactoryService().newText("", Color.WHITE, FontType.GAME, 28.0)
            text.textProperty().bind(SimpleStringProperty(char.name).concat(char.characterComponent.hp.valueProperty()))

            choiceBox.selectionModel.selectFirst()

            children += text
            children += choiceBox
        }
    }
}