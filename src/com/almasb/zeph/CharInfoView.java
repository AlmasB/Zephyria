package com.almasb.zeph;

import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.Player;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharInfoView extends Accordion {

    public CharInfoView(Player playerData) {
        Font font = Font.font("Lucida Console", 14);

        VBox attrBox = new VBox(5);
        //attrBox.setAlignment(Pos.CENTER_LEFT);
        for (Attribute attr : Attribute.values()) {
            Text text = new Text();
            text.setFont(font);
            text.textProperty().bind(new SimpleStringProperty(attr.toString())
                    .concat(": ").concat(playerData.attributeProperty(attr))
                    .concat(" + ").concat(playerData.bAttributeProperty(attr)));

            Text btn = new Text("+");
            btn.setStroke(Color.BLUE);
            btn.setStrokeWidth(3);
            btn.setFont(font);
            btn.visibleProperty().bind(playerData.attributePointsProperty().greaterThan(0)
                    .and(playerData.attributeProperty(attr).lessThan(100)));
            btn.setOnMouseClicked(event -> {
                playerData.increaseAttr(attr);
            });

            attrBox.getChildren().add(new HBox(10, text, btn));
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
            text.textProperty().bind(new SimpleStringProperty(stat.toString())
                    .concat(": ").concat(playerData.statProperty(stat))
                    .concat(" + ").concat(playerData.bStatProperty(stat)));

            statBox.getChildren().add(text);
        }

        getPanes().add(new TitledPane("Char Info", new HBox(10, attrBox, new Separator(Orientation.VERTICAL), statBox)));
    }
}
