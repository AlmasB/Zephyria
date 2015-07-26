package com.almasb.zeph;

import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.asset.AssetManager;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.GameEntity;
import com.almasb.zeph.entity.character.EquipPlace;
import com.almasb.zeph.entity.character.Player;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public final class EquipmentView extends Accordion {

    //private Group headGroup, bodyGroup, shoesGroup, leftHandGroup, rightHandGroup;

    private Map<EquipPlace, Group> groups = new HashMap<>();

    private Player playerData;

    // TODO: why cant we set tooltip font size?
    public EquipmentView(Player playerData, double height) {
        this.playerData = playerData;

        groups.put(EquipPlace.HELM, createHeadGroup());
        groups.put(EquipPlace.BODY, createBodyGroup());
        groups.put(EquipPlace.SHOES, createShoesGroup());
        groups.put(EquipPlace.LEFT_HAND, createLeftHandGroup());
        groups.put(EquipPlace.RIGHT_HAND, createRightHandGroup());

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

    private Group createHeadGroup() {
        Group group = new Group();
        group.setTranslateX(88);
        group.setTranslateY(60);
        Tooltip tooltip = new Tooltip();
        group.setUserData(tooltip);
        Tooltip.install(group, tooltip);
        return group;
    }

    private Group createBodyGroup() {
        Group group = new Group();
        group.setTranslateX(88);
        group.setTranslateY(105);

        Tooltip tooltip = new Tooltip();
        group.setUserData(tooltip);
        Tooltip.install(group, tooltip);

        return group;
    }

    private Group createShoesGroup() {
        Group group = new Group();
        group.setTranslateX(88);
        group.setTranslateY(150);
        Tooltip tooltip = new Tooltip();
        group.setUserData(tooltip);
        Tooltip.install(group, tooltip);
        return group;
    }

    private Group createLeftHandGroup() {
        Group group = new Group();
        group.setTranslateX(133);
        group.setTranslateY(105);
        Tooltip tooltip = new Tooltip();
        group.setUserData(tooltip);
        Tooltip.install(group, tooltip);
        return group;
    }

    private Group createRightHandGroup() {
        Group group = new Group();
        group.setTranslateX(43);
        group.setTranslateY(105);
        Tooltip tooltip = new Tooltip();
        group.setUserData(tooltip);
        Tooltip.install(group, tooltip);
        return group;
    }

    private void setItem(EquipPlace place, GameEntity item) {
        Group group = groups.get(place);
        group.getChildren().clear();

        Entity e = item.toEntity();
        e.setOnMouseClicked(event -> playerData.unEquipItem(place));

        group.getChildren().add(e);
        ((Tooltip)group.getUserData()).setText(item.getDescription());
    }
}
