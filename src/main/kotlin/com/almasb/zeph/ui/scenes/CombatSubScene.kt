package com.almasb.zeph.ui.scenes

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.scene.SubScene
import com.almasb.fxgl.ui.FontType
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.*
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

    private val playerMoves = arrayListOf<CombatMove>()
    private val combatMoveAI: CombatMoveAI = CounterLastCombatMoveAI()

    init {
        contentRoot.children += combatRoot
    }

    fun start(char1: CharacterEntity, char2: CharacterEntity) {
        val view1 = CombatView(char1)
        val view2 = CombatView(char2)

        view1.translateX = 50.0
        view1.translateY = 50.0

        view2.translateX = 690.0
        view2.translateY = 50.0

        val bg = Rectangle(1000.0, 600.0, Color.color(0.0, 0.0, 0.0, 0.95))
        bg.arcWidth = 25.0
        bg.arcHeight = 25.0
        bg.stroke = Color.AQUAMARINE
        bg.strokeWidth = 5.0

        val closeBtn = getUIFactoryService().newButton("Dev Close")
        closeBtn.setOnAction {
            FXGL.getSceneService().popSubScene()
        }

        closeBtn.translateX = 0.0
        closeBtn.translateY = -100.0

        val btnSelect = getUIFactoryService().newButton("Finish Turn")
        btnSelect.translateX = bg.width / 2.0 - 200.0 / 2.0
        btnSelect.translateY = 520.0
        btnSelect.setOnAction {
            btnSelect.isDisable = true

            val playerMove = view1.choiceBox.selectionModel.selectedItem
            val aiMove = combatMoveAI.nextMove(playerMoves)

            playerMoves += playerMove

            val playerDamageMod = playerMove.getDamageModifierAgainst(aiMove)
            val aiDamageMod = aiMove.getDamageModifierAgainst(playerMove)

            println("$playerMove vs $aiMove: $playerDamageMod vs $aiDamageMod")

            animationBuilder(this)
                    .onFinished {
                        val result1 = char1.characterComponent.attack(char2, playerDamageMod)

                        println("Player dealt: " + result1.value)

                        if (char2.hp.isZero) {
                            btnSelect.isDisable = false
                            FXGL.getSceneService().popSubScene()

                        } else {



                            // TODO: check if dead
                            // TODO: getTotal(ASPD) / 100.0 to determine how many attacks per turn

                            animationBuilder(this)
                                    .onFinished {
                                        val result2 = char2.characterComponent.attack(char1, aiDamageMod)

                                        println("AI dealt: " + result2.value)

                                        view2.lastMoveText.text = "Last move: $aiMove"

                                        // TODO: update tick for both
                                        char1.components.forEach { it.onUpdate(3.0) }
                                        char2.components.forEach { it.onUpdate(3.0) }

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

        val lastMoveText = getUIFactoryService().newText("", Color.WHITE, 18.0)

        init {
            val text = getUIFactoryService().newText(char.name, Color.WHITE, FontType.GAME, 28.0)

            choiceBox.prefWidth = 250.0
            choiceBox.selectionModel.selectFirst()

            val textFlow = getUIFactoryService().newTextFlow()

            textFlow.children.addAll(
                getUIFactoryService().newText("HP: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.GREEN, 22.0).also {
                    it.textProperty().bind(char.hp.valueProperty().asString("%.0f\n"))
                },

                getUIFactoryService().newText("SP: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.BLUE, 22.0).also {
                    it.textProperty().bind(char.sp.valueProperty().asString("%.0f\n"))
                },

                getUIFactoryService().newText("ATK: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.ATK).asString("%d\n"))
                },

                getUIFactoryService().newText("MATK: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.MATK).asString("%d\n"))
                },

                getUIFactoryService().newText("DEF: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.DEF).asString("%d\n"))
                },

                getUIFactoryService().newText("MDEF: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.MDEF).asString("%d\n"))
                },

                getUIFactoryService().newText("ARM: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.ARM).asString("%d\n"))
                },

                getUIFactoryService().newText("MARM: ", Color.WHITE, 14.0),
                getUIFactoryService().newText("", Color.WHITE, 22.0).also {
                    it.textProperty().bind(char.characterComponent.totalProperty(Stat.MARM).asString("%d\n"))
                },
            )

            children += text
            children += textFlow

            if (char.isPlayer) {
                children += choiceBox
            } else {
                children += lastMoveText
            }
        }
    }
}