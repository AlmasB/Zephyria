package com.almasb.zeph.ui

import com.almasb.fxgl.dsl.FXGL
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Stat
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.Cursor
import javafx.scene.ImageCursor
import javafx.scene.control.Separator
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharInfoView(player: CharacterEntity) : Region() {

    init {
        background = Background(BackgroundFill(Color.BLACK, null, null))

        val font = Font.font("Lucida Console", 14.0)
        val cursorQuestion: Cursor = ImageCursor(FXGL.image("ui/cursors/question.png"), 52.0, 10.0)
        val attrBox = VBox(5.0)
        attrBox.translateY = 10.0

        for (attr in Attribute.values()) {
            val text = Text()
            text.font = font
            text.fill = Color.WHITE
            text.cursor = cursorQuestion
            text.textProperty().bind(player.characterComponent.baseProperty(attr).asString("$attr: %-3d"))

            val bText = Text()
            bText.font = font
            bText.fill = Color.YELLOW
            bText.visibleProperty().bind(player.characterComponent.bonusProperty(attr).greaterThan(0))
            bText.textProperty().bind(player.characterComponent.bonusProperty(attr).asString("+%d ")
                    .concat(player.characterComponent.totalProperty(attr).asString("(%d)")))

            val btn = Text("+")
            btn.cursor = Cursor.HAND
            btn.stroke = Color.YELLOWGREEN.brighter()
            btn.strokeWidth = 3.0
            btn.font = font
            btn.visibleProperty().bind(player.playerComponent!!.attributePoints.greaterThan(0)
                    .and(player.characterComponent.baseProperty(attr).lessThan(100)))
            btn.setOnMouseClicked {
                player.playerComponent!!.increaseAttribute(attr)
            }

            val box = Pane()
            box.setPrefSize(160.0, 15.0)
            box.children.addAll(text, bText, btn)
            bText.translateX = 70.0
            btn.translateX = 155.0

            box.setOnTooltipHover {
                it.setText(attr.description)
            }

            attrBox.children.add(box)
        }

        val info = Text()
        info.font = font
        info.fill = Color.WHITE
        info.visibleProperty().bind(player.playerComponent!!.attributePoints.greaterThan(0))
        info.textProperty().bind(SimpleStringProperty("Attribute Points: ").concat(player.playerComponent!!.attributePoints)
                .concat("\nSkill Points: ").concat(player.playerComponent!!.skillPoints))
        attrBox.children.addAll(Separator(), info)

        val statBox = VBox(5.0)
        for (stat in Stat.values()) {
            val text = Text()
            text.font = font
            text.fill = Color.WHITE
            text.cursor = cursorQuestion

            val statName = stat.toString().replace("_", " ")

            text.textProperty().bind(player.characterComponent.baseProperty(stat).asString("$statName: %d"))

            val bText = Text()
            bText.font = font
            bText.fill = Color.YELLOW
            val textBinding = Bindings.`when`(player.characterComponent.bonusProperty(stat).isNotEqualTo(0))
                    .then(player.characterComponent.bonusProperty(stat).asString("+%d ")
                            .concat(player.characterComponent.baseProperty(stat).add(player.characterComponent.bonusProperty(stat)).asString("(%d)"))
                            .concat(stat.measureUnit))
                    .otherwise(stat.measureUnit)

            bText.textProperty().bind(textBinding)

            val box = HBox(5.0, text, bText)
            box.setOnTooltipHover {
                it.setText(stat.description)
            }

            statBox.children.add(box)
        }

        children.add(HBox(10.0, attrBox, Separator(Orientation.VERTICAL), statBox))
    }
}