package com.almasb.zeph;

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

    private Group headGroup, bodyGroup, shoesGroup, leftHandGroup, rightHandGroup;
//    private Tooltip headTooltip = new Tooltip(),
//            bodyTooltip = new Tooltip(),
//            shoesTooltip = new Tooltip(),
//            leftHandTooltip = new Tooltip(),
//            rightHandTooltip = new Tooltip();

    // TODO: why cant we set tooltip font size?
    public EquipmentView(Player playerData) {
        headGroup = createHeadGroup();
        bodyGroup = createBodyGroup();
        shoesGroup = createShoesGroup();
        leftHandGroup = createLeftHandGroup();
        rightHandGroup = createRightHandGroup();

        playerData.equipProperty(EquipPlace.HELM).addListener((obs, old, newItem) -> {
            setItem(headGroup, newItem);
        });

        playerData.equipProperty(EquipPlace.BODY).addListener((obs, old, newItem) -> {
            setItem(bodyGroup, newItem);
        });

        playerData.equipProperty(EquipPlace.SHOES).addListener((obs, old, newItem) -> {
            setItem(shoesGroup, newItem);
        });

        playerData.equipProperty(EquipPlace.LEFT_HAND).addListener((obs, old, newItem) -> {
            setItem(leftHandGroup, newItem);
        });

        playerData.equipProperty(EquipPlace.RIGHT_HAND).addListener((obs, old, newItem) -> {
            setItem(rightHandGroup, newItem);
        });




        // head
        Entity head = playerData.getEquip(EquipPlace.HELM).toEntity();
        head.setTranslateX(88);
        head.setTranslateY(60);

        Tooltip tooltip = new Tooltip(playerData.getEquip(EquipPlace.HELM).getDescription());
        Tooltip.install(head, tooltip);

        // body
        Entity body = playerData.getEquip(EquipPlace.BODY).toEntity();
        body.setTranslateX(88);
        body.setTranslateY(105);

        tooltip = new Tooltip(playerData.getEquip(EquipPlace.BODY).getDescription());
        Tooltip.install(body, tooltip);


        // shoes
        Entity shoes = playerData.getEquip(EquipPlace.SHOES).toEntity();
        shoes.setTranslateX(88);
        shoes.setTranslateY(150);

        tooltip = new Tooltip(playerData.getEquip(EquipPlace.SHOES).getDescription());
        Tooltip.install(shoes, tooltip);

        // left hand
        Entity left = playerData.getEquip(EquipPlace.LEFT_HAND).toEntity();
        left.setTranslateX(133);
        left.setTranslateY(105);

        tooltip = new Tooltip(playerData.getEquip(EquipPlace.LEFT_HAND).getDescription());
        Tooltip.install(left, tooltip);

        // right hand
        Entity right = playerData.getEquip(EquipPlace.RIGHT_HAND).toEntity();
        right.setTranslateX(43);
        right.setTranslateY(105);

        tooltip = new Tooltip(playerData.getEquip(EquipPlace.RIGHT_HAND).getDescription());
        Tooltip.install(right, tooltip);


        Texture background = AssetManager.INSTANCE.loadTexture("ui/inventory_left.png");
        Pane pane = new Pane(background, head, body, shoes, left, right);

        TitledPane equipPane = new TitledPane("Equipment", pane);
        Accordion equip = new Accordion(equipPane);
        equip.setTranslateY(getHeight() - 25);
        equip.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
            if (newPane == null) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), equip);
                tt.setToY(getHeight() - 25);
                tt.play();
            }
            else {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), equip);
                tt.setToY(getHeight() - background.getLayoutBounds().getHeight() - 25);
                tt.play();
            }
        });
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

    private void setItem(Group group, GameEntity item) {
        group.getChildren().clear();
        group.getChildren().add(item.toEntity());
        ((Tooltip)group.getUserData()).setText(item.getDescription());
    }
}
