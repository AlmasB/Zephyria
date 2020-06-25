package com.almasb.zeph.ui;

import com.almasb.fxgl.texture.Texture;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.item.Armor;
import com.almasb.zeph.item.Item;
import com.almasb.zeph.item.UsableItem;
import com.almasb.zeph.item.Weapon;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class InventoryView extends Parent {

    private static final int BG_WIDTH = 200;
    private static final int BG_HEIGHT = 240;

    private boolean isMinimized = false;

    private Map<Integer, Boolean> slots = new HashMap<>();

    private ListChangeListener<Item> listener;

    private CharacterEntity player;

    public InventoryView(CharacterEntity player) {
        relocate(getAppWidth() - 200, getAppHeight() - 240);

        var btn = new Rectangle(30, 20);
        btn.setStroke(Color.WHITE);
        btn.relocate(BG_WIDTH - 46, -22);
        btn.setOnMouseClicked(e -> {
            animationBuilder()
                    .duration(Duration.seconds(0.33))
                    .translate(this)
                    .from(isMinimized ? new Point2D(0.0, BG_HEIGHT) : new Point2D(0.0, 0.0))
                    .to(isMinimized ? new Point2D(0.0, 0.0) : new Point2D(0.0, BG_HEIGHT))
                    .buildAndPlay();

            isMinimized = !isMinimized;
        });

        Rectangle border = new Rectangle(BG_WIDTH * 2 + 3, BG_HEIGHT + 5);
        border.setStrokeWidth(2);
        border.setArcWidth(10);
        border.setArcHeight(10);

        Shape borderShape = Shape.union(border, new Circle(BG_WIDTH*2 + 3 - 30, 0.0, 30));
        borderShape.setFill(Color.rgb(25, 25, 25, 0.8));
        borderShape.setStroke(Color.WHITE);
        borderShape.setTranslateX(-BG_WIDTH - 3);

        Texture background = getAssetLoader().loadTexture("ui/inventory_right.png");

        EquipmentView equipView = new EquipmentView(player);
        equipView.setTranslateX(-BG_WIDTH);

        getChildren().addAll(borderShape, background, equipView, btn);

        this.player = player;

        for (int i = 0; i < 30; i++) {
            slots.put(i, true);
        }

        listener = new ListChangeListener<Item>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Item> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Item item : change.getAddedSubList()) {
                            addItem(item);
                        }
                    }
                    else if (change.wasRemoved()) {
                        for (Item item : change.getRemoved()) {

                            for (Iterator<Node> it = getChildren().iterator(); it.hasNext(); ) {
                                Node node = it.next();

                                if (node.getUserData() != null) {
                                    Pair<Item, Integer> data = (Pair<Item, Integer>) node.getUserData();

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
        };

        player.getInventory().getItems().forEach(this::addItem);

        player.getInventory().getItems().addListener(listener);
    }

    private int getNextFreeSlot() {
        for (int i = 0; i < 30; i++) {
            if (slots.get(i))
                return i;
        }

        return -1;
    }

    private void addItem(Item item) {
        int index = getNextFreeSlot();
        slots.put(index, false);

        Texture view = texture(item.getDescription().getTextureName());

        view.setUserData(new Pair<>(item, index));
        view.setTranslateX((index % 5) * 40 + 3);
        view.setTranslateY((index / 5) * 40 + 3);
        view.setPickOnBounds(true);
        view.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                if (item instanceof Weapon) {
                    player.getPlayerComponent().equipWeapon((Weapon) item);
                } else if (item instanceof Armor) {
                    player.getPlayerComponent().equipArmor((Armor) item);
                } else if (item instanceof UsableItem) {
                    player.getCharacterComponent().useItem((UsableItem) item);
                }

                // TODO: other usable types
            }
        });
        view.setCursor(Cursor.HAND);

        Tooltip tooltip = new Tooltip();

        Text text = new Text();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);
        text.setWrappingWidth(200);
        text.textProperty().bind(item.getDynamicDescription());

        tooltip.setGraphic(text);
        Tooltip.install(view, tooltip);

        getChildren().add(view);
    }
}
