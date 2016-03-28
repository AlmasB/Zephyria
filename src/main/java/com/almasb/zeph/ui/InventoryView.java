package com.almasb.zeph.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.texture.Texture;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerControl;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class InventoryView extends Accordion {

    private Map<Integer, Boolean> slots = new HashMap<>();
    private Pane root = new Pane();

    private PlayerControl playerData;

    public InventoryView(PlayerControl playerData, double width, double height) {
        this.playerData = playerData;
        for (int i = 0; i < 30; i++) {
            slots.put(i, true);
        }

        playerData.getInventory().itemsProperty().forEach(this::addItem);

//        playerData.getInventory().itemsProperty().addListener(new ListChangeListener<GameEntity>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends GameEntity> change) {
//                while (change.next()) {
//                    if (change.wasAdded()) {
//                        for (GameEntity item : change.getAddedSubList()) {
//                            addItem(item);
//                        }
//                    }
//                    else if (change.wasRemoved()) {
//                        for (GameEntity item : change.getRemoved()) {
//
//                            for (Iterator<Node> it = root.getChildren().iterator(); it.hasNext(); ) {
//                                Node node = it.next();
//                                if (node instanceof Texture) {
//                                    continue;
//                                }
//
//                                Entity e = (Entity) node;
//
//                                if (e.getProperty("data") == item) {
//                                    slots.put(e.getProperty("slot"), true);
//                                    it.remove();
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });



        Texture background = GameApplication.getService(ServiceType.ASSET_LOADER).loadTexture("ui/inventory_right.png");
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

    private void addItem(DescriptionComponent item) {
//        int index = getNextFreeSlot();
//        slots.put(index, false);
//
//        Entity e = item.toEntity();
//        e.setProperty("slot", index);
//        e.setTranslateX((index % 5) * 40);
//        e.setTranslateY((index / 5) * 40);
//        e.setOnMouseClicked(event -> {
//            if (item instanceof Weapon) {
//                playerData.equipWeapon((Weapon) item);
//            }
//            else if (item instanceof Armor) {
//                playerData.equipArmor((Armor) item);
//            }
//
//            // TODO: other usable types
//        });
//        e.setCursor(Cursor.HAND);
//
//        Tooltip tooltip = new Tooltip();
//
//        Text text = new Text();
//        text.setFont(Font.font(20));
//        text.setFill(Color.WHITE);
//        text.setWrappingWidth(200);
//        text.setText(item.getFullDescription());
//
//        tooltip.setGraphic(text);
//        Tooltip.install(e, tooltip);
//
//        root.getChildren().add(e);
    }
}
