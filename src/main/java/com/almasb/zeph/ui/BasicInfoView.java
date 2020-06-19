package com.almasb.zeph.ui;

import com.almasb.fxgl.ui.MDIWindow;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.combat.Stat;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BasicInfoView extends MDIWindow {

    public BasicInfoView(CharacterEntity player) {
        //super("Basic Info", WindowDecor.MINIMIZE);



        //setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(340, 240);
        setCanResize(false);

        ProgressBar barHPUI = ProgressBar.makeHPBar();
        barHPUI.setTranslateX(160);
        barHPUI.setTranslateY(5);
        barHPUI.setWidth(100);
        barHPUI.setHeight(15);
        barHPUI.setLabelPosition(Position.RIGHT);
        barHPUI.maxValueProperty().bind(player.getStats().totalStatProperty(Stat.MAX_HP));
        barHPUI.currentValueProperty().bind(player.getHp().valueProperty());

        ProgressBar barSPUI = ProgressBar.makeSkillBar();
        barSPUI.setTranslateX(160);
        barSPUI.setTranslateY(25);
        barSPUI.setWidth(100);
        barSPUI.setHeight(15);
        barSPUI.setLabelPosition(Position.RIGHT);
        barSPUI.maxValueProperty().bind(player.getStats().totalStatProperty(Stat.MAX_SP));
        barSPUI.currentValueProperty().bind(player.getSp().valueProperty());

        Text textPlayerName = new Text();
        textPlayerName.setTranslateX(15);
        textPlayerName.setTranslateY(15);
        textPlayerName.setFont(Font.font(18));
        textPlayerName.setFill(Color.WHITESMOKE);
        textPlayerName.textProperty().bind(
                player.getCharClass().asString(player.getName() + "\n%s")
        );

        Text textLevels = new Text();
        textLevels.setTranslateX(15);
        textLevels.setTranslateY(100);
        textLevels.setFont(Font.font(16));
        textLevels.setFill(Color.WHITESMOKE);
        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(player.getBaseLevel())
                .concat("\nAttr Lv. ").concat(player.getStatLevel())
                .concat("\nJob Lv. ").concat(player.getJobLevel()));

        ProgressBar barXPBase = new ProgressBar();
        barXPBase.setWidth(150);
        barXPBase.setTranslateX(120);
        barXPBase.setTranslateY(90);
        barXPBase.setMaxValue(player.getPlayerComponent().expNeededForNextBaseLevel());
        barXPBase.currentValueProperty().bind(player.getBaseXP());

        ProgressBar barXPStat = new ProgressBar();
        barXPStat.setWidth(150);
        barXPStat.setTranslateX(120);
        barXPStat.setTranslateY(110);
        barXPStat.setMaxValue(player.getPlayerComponent().expNeededForNextStatLevel());
        barXPStat.currentValueProperty().bind(player.getStatXP());

        ProgressBar barXPJob = new ProgressBar();
        barXPJob.setWidth(150);
        barXPJob.setTranslateX(120);
        barXPJob.setTranslateY(130);
        barXPJob.setMaxValue(player.getPlayerComponent().expNeededForNextJobLevel());
        barXPJob.currentValueProperty().bind(player.getJobXP());

        Text textMoney = new Text("");
        textMoney.setTranslateX(200);
        textMoney.setTranslateY(180);
        textMoney.setFont(Font.font(14));
        textMoney.setFill(Color.WHITESMOKE);
        textMoney.textProperty().bind(new SimpleStringProperty("Money: ").concat(player.getPlayerComponent().getMoney()).concat("G"));

        player.getBaseLevel().addListener((obs, old, newValue) -> {
            barXPBase.setMaxValue(player.getPlayerComponent().expNeededForNextBaseLevel());
        });

        player.getStatLevel().addListener((obs, old, newValue) -> {
            barXPStat.setMaxValue(player.getPlayerComponent().expNeededForNextStatLevel());
        });

        player.getJobLevel().addListener((obs, old, newValue) -> {
            barXPJob.setMaxValue(player.getPlayerComponent().expNeededForNextJobLevel());
        });

        Pane uiPane = new Pane();
        uiPane.setPrefSize(350, 200);
        uiPane.getChildren().addAll(textPlayerName, textLevels, textMoney, barHPUI, barSPUI, barXPBase, barXPStat, barXPJob);

        setContentPane(uiPane);


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


//        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
//        getRightIcons().get(0).setOnAction(e -> {
//            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), uiPane);
//            st.setFromY(isMinimized() ? 0 : 1);
//            st.setToY(isMinimized() ? 1 : 0);
//            st.play();
//
//            handler.handle(e);
//        });
    }
}
