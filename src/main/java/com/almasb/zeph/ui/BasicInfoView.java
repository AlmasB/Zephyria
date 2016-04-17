package com.almasb.zeph.ui;

import com.almasb.ents.Entity;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.PlayerEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BasicInfoView extends Accordion {

    public BasicInfoView(PlayerEntity player) {
        ProgressBar barHPUI = ProgressBar.makeHPBar();
        barHPUI.setTranslateX(160);
        barHPUI.setTranslateY(5);
        barHPUI.setWidth(100);
        barHPUI.setHeight(15);
        barHPUI.setLabelPosition(Position.RIGHT);
        barHPUI.maxValueProperty().bind(player.getStats().statProperty(Stat.MAX_HP));
        barHPUI.currentValueProperty().bind(player.getHp().valueProperty());

        ProgressBar barSPUI = ProgressBar.makeSkillBar();
        barSPUI.setTranslateX(160);
        barSPUI.setTranslateY(25);
        barSPUI.setWidth(100);
        barSPUI.setHeight(15);
        barSPUI.setLabelPosition(Position.RIGHT);
        barSPUI.maxValueProperty().bind(player.getStats().statProperty(Stat.MAX_SP));
        barSPUI.currentValueProperty().bind(player.getSp().valueProperty());

        Text textPlayerName = new Text();
        textPlayerName.setTranslateX(15);
        textPlayerName.setTranslateY(15);
        textPlayerName.setFont(Font.font(18));
        textPlayerName.setText(player.getDescription().getName());
        //textPlayerName.textProperty().bind(player.nameProperty().concat("\n").concat(player.charClassProperty()));

        Text textLevels = new Text();
        textLevels.setTranslateX(15);
        textLevels.setTranslateY(100);
        textLevels.setFont(Font.font(16));
        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(player.getBaseLevel().levelProperty())
                .concat("\nStat Lv. ").concat(player.getStatLevel())
                .concat("\nJob Lv. ").concat(player.getJobLevel()));

        ProgressBar barXPBase = new ProgressBar();
        barXPBase.setWidth(150);
        barXPBase.setTranslateX(120);
        barXPBase.setTranslateY(90);
        //barXPBase.currentValueProperty().bind(player.baseXPProperty());

        ProgressBar barXPStat = new ProgressBar();
        barXPStat.setWidth(150);
        barXPStat.setTranslateX(120);
        barXPStat.setTranslateY(110);
        //barXPStat.currentValueProperty().bind(player.statXPProperty());

        ProgressBar barXPJob = new ProgressBar();
        barXPJob.setWidth(150);
        barXPJob.setTranslateX(120);
        barXPJob.setTranslateY(130);
        //barXPJob.currentValueProperty().bind(player.jobXPProperty());
//
        Text textMoney = new Text("");
        textMoney.setTranslateX(200);
        textMoney.setTranslateY(180);
        textMoney.setFont(Font.font(14));
        textMoney.textProperty().bind(new SimpleStringProperty("Money: ").concat(player.getMoney().valueProperty()).concat("G"));

//        player.baseLevelProperty().addListener((obs, old, newValue) -> {
//            barXPBase.setMaxValue(player.expNeededForNextBaseLevel());
//        });
//
//        player.statLevelProperty().addListener((obs, old, newValue) -> {
//            barXPStat.setMaxValue(player.expNeededForNextStatLevel());
//        });
//
//        player.jobLevelProperty().addListener((obs, old, newValue) -> {
//            barXPJob.setMaxValue(player.expNeededForNextJobLevel());
//        });

        Pane uiPane = new Pane();
        uiPane.setPrefSize(350, 200);
        uiPane.getChildren().addAll(textPlayerName, textLevels, textMoney, barHPUI, barSPUI, barXPBase, barXPStat, barXPJob);

        getPanes().add(new TitledPane("Basic Info", uiPane));
        setExpandedPane(getPanes().get(0));
    }
}
