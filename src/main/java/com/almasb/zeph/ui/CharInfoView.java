package com.almasb.zeph.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.MDIWindow;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Stat;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharInfoView extends MDIWindow {

    public CharInfoView(CharacterEntity player) {
        //super("Char Info", WindowDecor.MINIMIZE);

        relocate(0, 240);

        //setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(340, 340);
        setCanResize(false);

        Font font = Font.font("Lucida Console", 14);
        Font largerFont = Font.font("Lucida Console", 16);
        Cursor cursorQuestion = new ImageCursor(FXGL.image("ui/cursors/question.png"), 52, 10);

        VBox attrBox = new VBox(5);
        attrBox.setTranslateY(10);
        for (Attribute attr : Attribute.values()) {
            Text text = new Text();
            text.setFont(font);
            text.setFill(Color.WHITE);
            text.setCursor(cursorQuestion);
            text.textProperty().bind(player.getCharacterComponent().baseProperty(attr).asString(attr.toString() + ": %-3d"));

            Text tooltipText = new Text(attr.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(largerFont);
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.YELLOW);
            bText.visibleProperty().bind(player.getCharacterComponent().bonusProperty(attr).greaterThan(0));
            bText.textProperty().bind(player.getCharacterComponent().bonusProperty(attr).asString("+%d ")
                    .concat(player.getCharacterComponent().totalProperty(attr).asString("(%d)")));

            Text btn = new Text("+");
            btn.setCursor(Cursor.HAND);
            btn.setStroke(Color.YELLOWGREEN.brighter());
            btn.setStrokeWidth(3);
            btn.setFont(font);
            btn.visibleProperty().bind(player.getPlayerComponent().getAttributePoints().greaterThan(0)
                    .and(player.getCharacterComponent().baseProperty(attr).lessThan(100)));

            btn.setOnMouseClicked(event -> {
                player.getPlayerComponent().increaseAttribute(attr);
            });

            Pane box = new Pane();
            box.setPrefSize(160, 15);
            box.getChildren().addAll(text, bText, btn);

            bText.setTranslateX(70);
            btn.setTranslateX(155);

            attrBox.getChildren().add(box);
        }

        Text info = new Text();
        info.setFont(font);
        info.setFill(Color.WHITE);
        info.visibleProperty().bind(player.getPlayerComponent().getAttributePoints().greaterThan(0));
        info.textProperty().bind(new SimpleStringProperty("Attribute Points: ").concat(player.getPlayerComponent().getAttributePoints())
            .concat("\nSkill Points: ").concat(player.getPlayerComponent().getSkillPoints()));

        attrBox.getChildren().addAll(new Separator(), info);

        VBox statBox = new VBox(5);
        for (Stat stat : Stat.values()) {
            Text text = new Text();
            text.setFont(font);
            text.setFill(Color.WHITE);
            text.setCursor(cursorQuestion);
            text.textProperty().bind(player.getCharacterComponent().baseProperty(stat).asString(stat.toString() + ": %d"));

            Text tooltipText = new Text(stat.getDescription());
            tooltipText.setFill(Color.WHITE);
            tooltipText.setFont(largerFont);
            tooltipText.setWrappingWidth(200);

            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(tooltipText);

            Tooltip.install(text, tooltip);

            Text bText = new Text();
            bText.setFont(font);
            bText.setFill(Color.YELLOW);

            StringBinding textBinding = Bindings.when(player.getCharacterComponent().bonusProperty(stat).greaterThan(0))
                .then(player.getCharacterComponent().bonusProperty(stat).asString("+%d ")
                        .concat(player.getCharacterComponent().baseProperty(stat).add(player.getCharacterComponent().bonusProperty(stat)).asString("(%d)"))
                        .concat(stat.getMeasureUnit()))
                .otherwise(stat.getMeasureUnit());

            bText.textProperty().bind(textBinding);

            statBox.getChildren().add(new HBox(5, text, bText));
        }

        Pane root = new Pane(new HBox(10, attrBox, new Separator(Orientation.VERTICAL), statBox));

        setContentPane(root);

//        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
//        getRightIcons().get(0).setOnAction(e -> {
//            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), root);
//            st.setFromY(isMinimized() ? 0 : 1);
//            st.setToY(isMinimized() ? 1 : 0);
//            st.play();
//
//            handler.handle(e);
//        });
    }
}
