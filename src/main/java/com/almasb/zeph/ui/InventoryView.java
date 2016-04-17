package com.almasb.zeph.ui;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.item.ArmorEntity;
import com.almasb.zeph.entity.item.WeaponEntity;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InventoryView extends Accordion {

    private Map<Integer, Boolean> slots = new HashMap<>();
    private Pane root = new Pane();

    private PlayerEntity player;

    public InventoryView(PlayerEntity player, double width, double height) {
        this.player = player;

        for (int i = 0; i < 30; i++) {
            slots.put(i, true);
        }

        player.getInventory().getItems().forEach(this::addItem);

        player.getInventory().getItems().addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Entity> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Entity item : change.getAddedSubList()) {
                            addItem(item);
                        }
                    }
                    else if (change.wasRemoved()) {
                        for (Entity item : change.getRemoved()) {

                            for (Iterator<Node> it = root.getChildren().iterator(); it.hasNext(); ) {
                                Node node = it.next();

                                if (node.getUserData() != null) {
                                    Pair<Entity, Integer> data = (Pair<Entity, Integer>) node.getUserData();

                                    if (data.getKey() == item) {
                                        slots.put(data.getValue(), true);
                                        it.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        Texture background = FXGL.getAssetLoader().loadTexture("ui/inventory_right.png");
        root.getChildren().add(background);

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

        setTranslateX(width - background.getLayoutBounds().getWidth() - 2);

        getPanes().add(new TitledPane("Inventory", root));

        setTranslateY(height - 25);
    }

    private int getNextFreeSlot() {
        for (int i = 0; i < 30; i++) {
            if (slots.get(i))
                return i;
        }

        return -1;
    }

    private void addItem(Entity item) {
        int index = getNextFreeSlot();
        slots.put(index, false);

        DescriptionComponent data = item.getComponentUnsafe(DescriptionComponent.class);

        Texture view = FXGL.getAssetLoader().loadTexture(data.getTextureName());

        view.setUserData(new Pair<>(item, index));
        view.setTranslateX((index % 5) * 40);
        view.setTranslateY((index / 5) * 40);
        view.setOnMouseClicked(event -> {
            if (item instanceof WeaponEntity) {
                player.getControl().equipWeapon((WeaponEntity) item);
            }
            else if (item instanceof ArmorEntity) {
                player.getControl().equipArmor((ArmorEntity) item);
            }

            // TODO: other usable types
        });
        view.setCursor(Cursor.HAND);

        Tooltip tooltip = new Tooltip();

        Text text = new Text();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);
        text.setWrappingWidth(200);
        text.setText(data.getDescription());

        tooltip.setGraphic(text);
        Tooltip.install(view, tooltip);

        root.getChildren().add(view);
    }
}
