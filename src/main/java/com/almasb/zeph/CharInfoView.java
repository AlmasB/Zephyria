package com.almasb.zeph;

import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.Player;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharInfoView extends Accordion {

    public CharInfoView(Player playerData) {
        Font font = Font.font("Lucida Console", 14);
        Cursor cursorQuestion = new ImageCursor(R.assets.getTexture("ui/cursors/question.png").getImage(), 52, 10);

        VBox attrBox = new VBox(5);
        for (Attribute attr : Attribute.values()) {
            Text text = new Text();
            text.setFont(font);
            text.setCursor(cursorQuestion);
            text.textProperty().bind(playerData.attributeProperty(attr).asString(attr.toString() + ": %d"));

            Text tooltipText = new Text(attr.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(Font.font(16));
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.DARKGREEN);
            bText.visibleProperty().bind(playerData.bAttributeProperty(attr).greaterThan(0));
            bText.textProperty().bind(playerData.bAttributeProperty(attr).asString("+%d ")
                    .concat(playerData.attributeProperty(attr).add(playerData.bAttributeProperty(attr)).asString("(%d)")));

            Text btn = new Text("+");
            btn.setCursor(Cursor.HAND);
            btn.setStroke(Color.BLUE);
            btn.setStrokeWidth(3);
            btn.setFont(font);
            btn.visibleProperty().bind(playerData.attributePointsProperty().greaterThan(0)
                    .and(playerData.attributeProperty(attr).lessThan(100)));
            btn.setOnMouseClicked(event -> {
                playerData.increaseAttr(attr);
            });

            attrBox.getChildren().add(new HBox(5, text, bText, btn));
        }

        Text info = new Text();
        info.setFont(font);
        info.visibleProperty().bind(playerData.attributePointsProperty().greaterThan(0));
        info.textProperty().bind(new SimpleStringProperty("Points: ").concat(playerData.attributePointsProperty()));

        attrBox.getChildren().addAll(new Separator(), info);

        VBox statBox = new VBox(5);
        for (Stat stat : Stat.values()) {
            Text text = new Text();
            text.setFont(font);
            text.setCursor(cursorQuestion);
            text.textProperty().bind(playerData.statProperty(stat).asString(stat.toString() + ": %d"));

            Text tooltipText = new Text(stat.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(Font.font(16));
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.DARKGREEN);

            StringBinding textBinding = Bindings.when(playerData.bStatProperty(stat).greaterThan(0))
                .then(playerData.bStatProperty(stat).asString("+%d ")
                        .concat(playerData.statProperty(stat).add(playerData.bStatProperty(stat)).asString("(%d)"))
                        .concat(stat.getMeasureUnit()))
                .otherwise(stat.getMeasureUnit());

            bText.textProperty().bind(textBinding);

            statBox.getChildren().add(new HBox(5, text, bText));
        }

        getPanes().add(new TitledPane("Char Info", new HBox(10, attrBox, new Separator(Orientation.VERTICAL), statBox)));
    }
}
