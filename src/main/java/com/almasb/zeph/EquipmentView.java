package com.almasb.zeph;

import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.asset.AssetManager;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.EquipPlace;
import com.almasb.zeph.entity.character.PlayerControl;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public final class EquipmentView extends Accordion {

    private Map<EquipPlace, Group> groups = new HashMap<>();

    private PlayerControl playerData;

    public EquipmentView(PlayerControl playerData, double height) {
        this.playerData = playerData;

        groups.put(EquipPlace.HELM, createGroup(88, 60));
        groups.put(EquipPlace.BODY, createGroup(88, 105));
        groups.put(EquipPlace.SHOES, createGroup(88, 150));
        groups.put(EquipPlace.LEFT_HAND, createGroup(133, 105));
        groups.put(EquipPlace.RIGHT_HAND, createGroup(43, 105));

        for (EquipPlace place : EquipPlace.values()) {
            setItem(place, playerData.getEquip(place));
            playerData.equipProperty(place).addListener((obs, old, newItem) -> {
                setItem(place, newItem);
            });
        }

        Pane pane = new Pane();

        try {
            Texture background = AssetManager.INSTANCE.loadTexture("ui/inventory_left.png");
            pane.getChildren().add(background);

            expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
                if (newPane == null) {
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), this);
                    tt.setToY(height - 25);
                    tt.play();
                }
                else {
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), this);
                    tt.setToY(height - background.getLayoutBounds().getHeight() - 25);
                    tt.play();
                }
            });
        }
        catch (Exception e) {}

        pane.getChildren().addAll(groups.values());
        getPanes().add(new TitledPane("Equipment", pane));

        setTranslateY(height - 25);
    }

    private Group createGroup(int x, int y) {
        Group group = new Group();
        group.setTranslateX(x);
        group.setTranslateY(y);
        Tooltip tooltip = new Tooltip();

        Text text = new Text();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);
        text.setWrappingWidth(200);

        tooltip.setGraphic(text);
        Tooltip.install(group, tooltip);

        group.setUserData(text);
        return group;
    }

    private void setItem(EquipPlace place, DescriptionComponent item) {
        Group group = groups.get(place);
        group.getChildren().clear();

        Entity e = item.toEntity();
//        e.setOnMouseClicked(event -> playerData.unEquipItem(place));
//        e.setCursor(Cursor.HAND);
//
//        group.getChildren().add(e);
        ((Text)group.getUserData()).setText(item.getFullDescription());
    }
}
