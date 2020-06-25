package com.almasb.zeph.ui;

import com.almasb.fxgl.texture.Texture;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.character.EquipPlace;
import com.almasb.zeph.item.Item;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.texture;

public final class EquipmentView extends Parent {

    private Map<EquipPlace, Group> groups = new HashMap<>();

    private CharacterEntity player;

    public EquipmentView(CharacterEntity player) {
        this.player = player;

        groups.put(EquipPlace.HELM, createGroup(88, 60));
        groups.put(EquipPlace.BODY, createGroup(88, 105));
        groups.put(EquipPlace.SHOES, createGroup(88, 150));
        groups.put(EquipPlace.LEFT_HAND, createGroup(133, 105));
        groups.put(EquipPlace.RIGHT_HAND, createGroup(43, 105));

        for (EquipPlace place : EquipPlace.values()) {
            setItem(place, player.getPlayerComponent().getEquip(place));
            player.getPlayerComponent().equipProperty(place).addListener((obs, old, newItem) -> {
                setItem(place, newItem);
            });
        }

        Texture background = texture("ui/inventory_left.png");

        Rectangle border = new Rectangle(200, 240);
        border.setStrokeWidth(2);
        border.setArcWidth(10);
        border.setArcHeight(10);
        border.setFill(Color.rgb(25, 25, 25, 0.8));
        border.setStroke(Color.WHITE);

        getChildren().addAll(background);
        getChildren().addAll(groups.values());
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

    private void setItem(EquipPlace place, Item item) {
        Texture view = texture(item.getDescription().getTextureName());
        view.setOnMouseClicked(event -> player.getPlayerComponent().unEquipItem(place));
        view.setCursor(Cursor.HAND);

        Group group = groups.get(place);
        group.getChildren().setAll(view);

        ((Text)group.getUserData()).textProperty().bind(item.getDynamicDescription());
    }
}
