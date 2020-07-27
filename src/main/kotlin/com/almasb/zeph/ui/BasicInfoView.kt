package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.ui.ProgressBar
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Stat
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.Separator
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text

// TODO: UI for monsters to see attrs / stats

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class BasicInfoView(player: CharacterEntity) : Parent() {

    private val BG_WIDTH = 180.0
    private val BG_HEIGHT = 440.0

    private val infoView = CharInfoView(player)

    val minBtn = MinimizeButton("C", 15.0, BG_HEIGHT - 25.0, 0.0, -(BG_HEIGHT - 25.0) + 25, this)

    init {
        val textLevels = Text()
        textLevels.translateX = 15.0
        textLevels.translateY = 20.0
        textLevels.font = Font.font(14.0)
        textLevels.fill = Color.WHITESMOKE
        textLevels.textProperty().bind(SimpleStringProperty("Base Lv. ").concat(player.baseLevel)
                .concat("\nAttr Lv. ").concat(player.statLevel)
                .concat("\nJob Lv. ").concat(player.jobLevel))

        val barXPBase = ProgressBar()
        barXPBase.setWidth(150.0)
        barXPBase.translateX = 120.0
        barXPBase.translateY = 10.0
        barXPBase.setMaxValue(player.playerComponent!!.expNeededForNextBaseLevel().toDouble())
        barXPBase.currentValueProperty().bind(player.baseXP)

        val barXPStat = ProgressBar()
        barXPStat.setWidth(150.0)
        barXPStat.translateX = 120.0
        barXPStat.translateY = barXPBase.translateY + 20
        barXPStat.setMaxValue(player.playerComponent!!.expNeededForNextStatLevel().toDouble())
        barXPStat.currentValueProperty().bind(player.attrXP)

        val barXPJob = ProgressBar()
        barXPJob.setWidth(150.0)
        barXPJob.translateX = 120.0
        barXPJob.translateY = barXPStat.translateY + 20
        barXPJob.setMaxValue(player.playerComponent!!.expNeededForNextJobLevel().toDouble())
        barXPJob.currentValueProperty().bind(player.jobXP)

        player.baseLevel.addListener { _, _, _ -> barXPBase.setMaxValue(player.playerComponent!!.expNeededForNextBaseLevel().toDouble()) }
        player.statLevel.addListener { _, _, _ -> barXPStat.setMaxValue(player.playerComponent!!.expNeededForNextStatLevel().toDouble()) }
        player.jobLevel.addListener { _, _, _ -> barXPJob.setMaxValue(player.playerComponent!!.expNeededForNextJobLevel().toDouble()) }

        val separator = Separator()
        val uiPane = Pane()
        uiPane.setPrefSize(350.0, 65.0)
        uiPane.children.addAll(textLevels, barXPBase, barXPStat, barXPJob)

        val border = Rectangle((BG_WIDTH * 2 + 3), (BG_HEIGHT + 5))
        border.strokeWidth = 2.0
        border.arcWidth = 10.0
        border.arcHeight = 10.0

        val borderShape = Shape.union(border, Circle((BG_WIDTH * 2 + 3 - 30), 0.0, 30.0))
        borderShape.fill = Color.rgb(25, 25, 25, 0.8)
        borderShape.stroke = Color.WHITE
        borderShape.scaleX = -1.0
        borderShape.scaleY = -1.0

        val text = getUIFactoryService().newText("", Color.WHITE, 12.0)
        text.textProperty().bind(
                player.charClass.asString("%s    ")
                        .concat(SimpleStringProperty("Lv. ").concat(player.baseLevel.asString("%d    ")))
                        .concat(player.hp.valueProperty().asString("HP: %.0f/"))
                        .concat(player.characterComponent.totalProperty(Stat.MAX_HP).asString("%d    "))
                        .concat(player.sp.valueProperty().asString("SP: %.0f/"))
                        .concat(player.characterComponent.totalProperty(Stat.MAX_SP).asString("%d    "))
                        .concat(player.playerComponent!!.money.asString("%dG"))
        )

        val vBox = VBox(5.0)
        vBox.padding = Insets(10.0)
        vBox.children.addAll(infoView, separator, uiPane, text)

        children.addAll(borderShape, vBox, minBtn)
    }
}


// TODO: if effects are added constantly we need to optimize
//        player.getCharConrol().getEffects().addListener((ListChangeListener<? super EffectEntity>) c -> {
//            while (c.next()) {
//                if (c.wasAdded()) {
//                    c.getAddedSubList().forEach(effect -> {
//
//                        // TODO: this is where effect view is added + tooltip
//
//                        DescriptionComponent desc = effect.getDesc();
//
//                        Texture view = FXGL.getAssetLoader().loadTexture(desc.getTextureName().get());
//                        view.setTranslateX(10);
//                        view.setTranslateY(170);
//
//                        uiPane.getChildren().addAll(view);
//
//                    });
//                } else if (c.wasRemoved()) {
//                    // TODO: effect view is removed
//                }
//            }
//        })