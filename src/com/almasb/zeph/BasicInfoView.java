package com.almasb.zeph;

import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.Player;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BasicInfoView extends Accordion {

    public BasicInfoView(Player playerData) {
        ProgressBar barHPUI = ProgressBar.makeHPBar();
        barHPUI.setTranslateX(160);
        barHPUI.setTranslateY(5);
        barHPUI.setWidth(100);
        barHPUI.setHeight(15);
        barHPUI.setLabelPosition(Position.RIGHT);
        barHPUI.maxValueProperty().bind(playerData.statProperty(Stat.MAX_HP));
        barHPUI.currentValueProperty().bind(playerData.hpProperty());

        ProgressBar barSPUI = ProgressBar.makeSkillBar();
        barSPUI.setTranslateX(160);
        barSPUI.setTranslateY(25);
        barSPUI.setWidth(100);
        barSPUI.setHeight(15);
        barSPUI.setLabelPosition(Position.RIGHT);
        barSPUI.maxValueProperty().bind(playerData.statProperty(Stat.MAX_SP));
        barSPUI.currentValueProperty().bind(playerData.spProperty());

        Text textPlayerName = new Text();
        textPlayerName.setTranslateX(15);
        textPlayerName.setTranslateY(15);
        textPlayerName.setFont(Font.font(18));
        textPlayerName.textProperty().bind(playerData.nameProperty().concat("\n").concat(playerData.charClassProperty()));

        Text textLevels = new Text();
        textLevels.setTranslateX(15);
        textLevels.setTranslateY(100);
        textLevels.setFont(Font.font(16));
        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(playerData.baseLevelProperty())
                .concat("\nStat Lv. ").concat(playerData.statLevelProperty())
                .concat("\nJob Lv. ").concat(playerData.jobLevelProperty()));

        ProgressBar barXPBase = new ProgressBar();
        barXPBase.setWidth(150);
        barXPBase.setTranslateX(120);
        barXPBase.setTranslateY(90);
        barXPBase.currentValueProperty().bind(playerData.baseXPProperty());

        ProgressBar barXPStat = new ProgressBar();
        barXPStat.setWidth(150);
        barXPStat.setTranslateX(120);
        barXPStat.setTranslateY(110);
        barXPStat.currentValueProperty().bind(playerData.statXPProperty());

        ProgressBar barXPJob = new ProgressBar();
        barXPJob.setWidth(150);
        barXPJob.setTranslateX(120);
        barXPJob.setTranslateY(130);
        barXPJob.currentValueProperty().bind(playerData.jobXPProperty());

        Text textMoney = new Text("Money: 1000G");
        textMoney.setTranslateX(200);
        textMoney.setTranslateY(180);
        textMoney.setFont(Font.font(14));
        textMoney.textProperty().bind(new SimpleStringProperty("Money: ").concat(playerData.moneyProperty()).concat("G"));

        playerData.baseLevelProperty().addListener((obs, old, newValue) -> {
            barXPBase.setMaxValue(playerData.expNeededForNextBaseLevel());
        });

        playerData.statLevelProperty().addListener((obs, old, newValue) -> {
            barXPStat.setMaxValue(playerData.expNeededForNextStatLevel());
        });

        playerData.jobLevelProperty().addListener((obs, old, newValue) -> {
            barXPJob.setMaxValue(playerData.expNeededForNextJobLevel());
        });

        Pane uiPane = new Pane();
        uiPane.setPrefSize(350, 200);
        uiPane.getChildren().addAll(textPlayerName, textLevels, textMoney, barHPUI, barSPUI, barXPBase, barXPStat, barXPJob);

        getPanes().add(new TitledPane("Basic Info", uiPane));
        setExpandedPane(getPanes().get(0));
    }
}
