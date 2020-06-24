package com.almasb.zeph.ui;

import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.combat.Stat;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// TODO: UI for monsters to see attrs / stats

public class BasicInfoView extends Region {

    private CharInfoView infoView;

    public BasicInfoView(CharacterEntity player) {
        getStyleClass().add("dev-pane");
        setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        infoView = new CharInfoView(player);

        Text textLevels = new Text();
        textLevels.setTranslateX(15);
        textLevels.setTranslateY(20);
        textLevels.setFont(Font.font(14));
        textLevels.setFill(Color.WHITESMOKE);
        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(player.getBaseLevel())
                .concat("\nAttr Lv. ").concat(player.getStatLevel())
                .concat("\nJob Lv. ").concat(player.getJobLevel()));

        ProgressBar barXPBase = new ProgressBar();
        barXPBase.setWidth(150);
        barXPBase.setTranslateX(120);
        barXPBase.setTranslateY(10);
        barXPBase.setMaxValue(player.getPlayerComponent().expNeededForNextBaseLevel());
        barXPBase.currentValueProperty().bind(player.getBaseXP());

        ProgressBar barXPStat = new ProgressBar();
        barXPStat.setWidth(150);
        barXPStat.setTranslateX(120);
        barXPStat.setTranslateY(barXPBase.getTranslateY() + 20);
        barXPStat.setMaxValue(player.getPlayerComponent().expNeededForNextStatLevel());
        barXPStat.currentValueProperty().bind(player.getAttrXP());

        ProgressBar barXPJob = new ProgressBar();
        barXPJob.setWidth(150);
        barXPJob.setTranslateX(120);
        barXPJob.setTranslateY(barXPStat.getTranslateY() + 20);
        barXPJob.setMaxValue(player.getPlayerComponent().expNeededForNextJobLevel());
        barXPJob.currentValueProperty().bind(player.getJobXP());

        Text textMoney = new Text("");
        textMoney.setTranslateX(200);
        textMoney.setTranslateY(90);
        textMoney.setFont(Font.font(14));
        textMoney.setFill(Color.WHITESMOKE);
        textMoney.textProperty().bind(new SimpleStringProperty("Money: ").concat(player.getPlayerComponent().getMoney()).concat("G"));

        Button btnInfo = new Button("Info");
        btnInfo.setTranslateX(15);
        btnInfo.setTranslateY(80);

        player.getBaseLevel().addListener((obs, old, newValue) -> {
            barXPBase.setMaxValue(player.getPlayerComponent().expNeededForNextBaseLevel());
        });

        player.getStatLevel().addListener((obs, old, newValue) -> {
            barXPStat.setMaxValue(player.getPlayerComponent().expNeededForNextStatLevel());
        });

        player.getJobLevel().addListener((obs, old, newValue) -> {
            barXPJob.setMaxValue(player.getPlayerComponent().expNeededForNextJobLevel());
        });

        var separator = new Separator();

        Pane uiPane = new Pane();
        uiPane.setPrefSize(350, 110);
        uiPane.getChildren().addAll(textLevels, textMoney, barXPBase, barXPStat, barXPJob, btnInfo);

        var root = new VBox(5);
        root.getChildren().addAll(uiPane, separator);

        btnInfo.setOnAction(e -> {
            if (root.getChildren().contains(infoView)) {
                root.getChildren().remove(infoView);
            } else {
                root.getChildren().add(infoView);
            }
        });

        var pane = new TitledPane("", root);
        pane.textProperty().bind(
                player.getCharClass().asString("%s    ")
                        .concat(new SimpleStringProperty("Lv. ").concat(player.getBaseLevel().asString("%d    ")))
                        .concat(player.getHp().valueProperty().asString("HP: %.0f/"))
                        .concat(player.getCharacterComponent().totalProperty(Stat.MAX_HP).asString("%d    "))
                        .concat(player.getSp().valueProperty().asString("SP: %.0f/"))
                        .concat(player.getCharacterComponent().totalProperty(Stat.MAX_SP).asString("%d    "))
        );

        Accordion accordion = new Accordion();
        accordion.getPanes().add(pane);

        getChildren().add(accordion);
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
//        });