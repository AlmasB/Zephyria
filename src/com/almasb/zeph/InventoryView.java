package com.almasb.zeph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.almasb.fxgl.asset.AssetManager;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.GameEntity;
import com.almasb.zeph.entity.character.Player;
import com.almasb.zeph.entity.item.Armor;
import com.almasb.zeph.entity.item.Weapon;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class InventoryView extends Accordion {

    private Map<Integer, Boolean> slots = new HashMap<>();
    private Pane root = new Pane();

    private Player playerData;

    public InventoryView(Player playerData, double width, double height) {
        this.playerData = playerData;
        for (int i = 0; i < 30; i++) {
            slots.put(i, true);
        }

        playerData.getInventory().itemsProperty().forEach(this::addItem);

        playerData.getInventory().itemsProperty().addListener(new ListChangeListener<GameEntity>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GameEntity> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (GameEntity item : change.getAddedSubList()) {
                            addItem(item);
                        }
                    }
                    else if (change.wasRemoved()) {
                        for (GameEntity item : change.getRemoved()) {

                            for (Iterator<Node> it = root.getChildren().iterator(); it.hasNext(); ) {
                                Node node = it.next();
                                if (node instanceof Texture) {
                                    continue;
                                }

                                Entity e = (Entity) node;

                                if (e.getProperty("data") == item) {
                                    slots.put(e.getProperty("slot"), true);
                                    it.remove();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });




        try {
            Texture background = AssetManager.INSTANCE.loadTexture("ui/inventory_right.png");
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
        }
        catch (Exception e) {}

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

    private void addItem(GameEntity item) {
        int index = getNextFreeSlot();
        slots.put(index, false);

        Entity e = item.toEntity();
        e.setProperty("slot", index);
        e.setTranslateX((index % 5) * 40);
        e.setTranslateY((index / 5) * 40);
        e.setOnMouseClicked(event -> {
            if (item instanceof Weapon) {
                playerData.equipWeapon((Weapon) item);
            }
            else if (item instanceof Armor) {
                playerData.equipArmor((Armor) item);
            }

            // TODO: other usable types
        });
        e.setCursor(Cursor.HAND);

        root.getChildren().add(e);
    }
}
