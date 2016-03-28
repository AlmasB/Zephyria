package com.almasb.zeph.ui;

import com.almasb.ents.Entity;
import javafx.scene.control.Accordion;

public class BasicInfoView extends Accordion {

    public BasicInfoView(Entity player) {
//        ProgressBar barHPUI = ProgressBar.makeHPBar();
//        barHPUI.setTranslateX(160);
//        barHPUI.setTranslateY(5);
//        barHPUI.setWidth(100);
//        barHPUI.setHeight(15);
//        barHPUI.setLabelPosition(Position.RIGHT);
//        barHPUI.maxValueProperty().bind(player.statProperty(Stat.MAX_HP));
//        barHPUI.currentValueProperty().bind(player.hpProperty());
//
//        ProgressBar barSPUI = ProgressBar.makeSkillBar();
//        barSPUI.setTranslateX(160);
//        barSPUI.setTranslateY(25);
//        barSPUI.setWidth(100);
//        barSPUI.setHeight(15);
//        barSPUI.setLabelPosition(Position.RIGHT);
//        barSPUI.maxValueProperty().bind(player.statProperty(Stat.MAX_SP));
//        barSPUI.currentValueProperty().bind(player.spProperty());
//
//        Text textPlayerName = new Text();
//        textPlayerName.setTranslateX(15);
//        textPlayerName.setTranslateY(15);
//        textPlayerName.setFont(Font.font(18));
//        textPlayerName.textProperty().bind(player.nameProperty().concat("\n").concat(player.charClassProperty()));
//
//        Text textLevels = new Text();
//        textLevels.setTranslateX(15);
//        textLevels.setTranslateY(100);
//        textLevels.setFont(Font.font(16));
//        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(player.baseLevelProperty())
//                .concat("\nStat Lv. ").concat(player.statLevelProperty())
//                .concat("\nJob Lv. ").concat(player.jobLevelProperty()));
//
//        ProgressBar barXPBase = new ProgressBar();
//        barXPBase.setWidth(150);
//        barXPBase.setTranslateX(120);
//        barXPBase.setTranslateY(90);
//        barXPBase.currentValueProperty().bind(player.baseXPProperty());
//
//        ProgressBar barXPStat = new ProgressBar();
//        barXPStat.setWidth(150);
//        barXPStat.setTranslateX(120);
//        barXPStat.setTranslateY(110);
//        barXPStat.currentValueProperty().bind(player.statXPProperty());
//
//        ProgressBar barXPJob = new ProgressBar();
//        barXPJob.setWidth(150);
//        barXPJob.setTranslateX(120);
//        barXPJob.setTranslateY(130);
//        barXPJob.currentValueProperty().bind(player.jobXPProperty());
//
//        Text textMoney = new Text("");
//        textMoney.setTranslateX(200);
//        textMoney.setTranslateY(180);
//        textMoney.setFont(Font.font(14));
//        textMoney.textProperty().bind(new SimpleStringProperty("Money: ").concat(player.moneyProperty()).concat("G"));
//
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
//
//        Pane uiPane = new Pane();
//        uiPane.setPrefSize(350, 200);
//        uiPane.getChildren().addAll(textPlayerName, textLevels, textMoney, barHPUI, barSPUI, barXPBase, barXPStat, barXPJob);
//
//        getPanes().add(new TitledPane("Basic Info", uiPane));
//        setExpandedPane(getPanes().get(0));
    }
}
