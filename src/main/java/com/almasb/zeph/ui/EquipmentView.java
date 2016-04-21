package com.almasb.zeph.ui;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.EquipPlace;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.control.PlayerControl;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public final class EquipmentView extends InGameWindow {

    private Map<EquipPlace, Group> groups = new HashMap<>();

    private PlayerEntity player;

    public EquipmentView(PlayerEntity player, double height) {
        super("Equipment", WindowDecor.MINIMIZE);

        relocate(100, 400);

        setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(202, 315);
        setResizableWindow(false);

        this.player = player;

        groups.put(EquipPlace.HELM, createGroup(88, 60));
        groups.put(EquipPlace.BODY, createGroup(88, 105));
        groups.put(EquipPlace.SHOES, createGroup(88, 150));
        groups.put(EquipPlace.LEFT_HAND, createGroup(133, 105));
        groups.put(EquipPlace.RIGHT_HAND, createGroup(43, 105));

        for (EquipPlace place : EquipPlace.values()) {
            setItem(place, player.getControl().getEquip(place));
            player.getControl().equipProperty(place).addListener((obs, old, newItem) -> {
                setItem(place, newItem);
            });
        }

        Pane pane = new Pane();

        Texture background = FXGL.getAssetLoader().loadTexture("ui/inventory_left.png");
        pane.getChildren().add(background);
        pane.getChildren().addAll(groups.values());

        setContentPane(pane);

        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
        getRightIcons().get(0).setOnAction(e -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), pane);
            st.setFromY(isMinimized() ? 0 : 1);
            st.setToY(isMinimized() ? 1 : 0);
            st.play();

            handler.handle(e);
        });
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

    private void setItem(EquipPlace place, Entity item) {
        Group group = groups.get(place);
        group.getChildren().clear();

        DescriptionComponent data = item.getComponentUnsafe(DescriptionComponent.class);

        Texture view = FXGL.getAssetLoader().loadTexture(data.getTextureName());

        view.setOnMouseClicked(event -> player.getControl().unEquipItem(place));
        view.setCursor(Cursor.HAND);

        group.getChildren().add(view);
        ((Text)group.getUserData()).setText(data.getDescription());
    }
}
