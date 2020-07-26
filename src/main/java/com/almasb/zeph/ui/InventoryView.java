package com.almasb.zeph.ui;

import com.almasb.fxgl.texture.Texture;
import com.almasb.zeph.Config;
import com.almasb.zeph.ConfigKt;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.item.Armor;
import com.almasb.zeph.item.Item;
import com.almasb.zeph.item.UsableItem;
import com.almasb.zeph.item.Weapon;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

    // K - index, V - if free? TODO: double check
    private Map<Integer, Boolean> slots = new HashMap<>();

    private Group itemGroup = new Group();

    private TooltipView tooltip = ConfigKt.getUITooltip();
    private BooleanBinding isTooltipVisible;

    private ListChangeListener<Item> listener;

    private CharacterEntity player;

    public InventoryView(CharacterEntity player) {
        relocate(getAppWidth() - 200, getAppHeight() - 240);

        var btn = new Rectangle(30, 20);
        btn.setStroke(Color.WHITE);
        btn.relocate(BG_WIDTH - 46, -22);
        btn.setOnMouseClicked(e -> {

            if (isReleaseMode()) {
                play("ui_slide.wav");
            }

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

        getChildren().addAll(borderShape, background, equipView, itemGroup, btn);

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

                            for (Iterator<Node> it = itemGroup.getChildren().iterator(); it.hasNext(); ) {
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

        player.getInventory().itemsProperty().forEach(this::addItem);

        player.getInventory().itemsProperty().addListener(listener);

        // TODO: should have 1 tooltip object for everything in the game
        // TODO: description should have TextFlow, so we can color some text, e.g. 10% damage, runes, etc.

        isTooltipVisible = Bindings.createBooleanBinding(() -> false, player.getInventory().itemsProperty());

        // TODO: and NEWly added items
        itemGroup.getChildren().forEach(node -> {
            isTooltipVisible = isTooltipVisible.or(node.hoverProperty());
        });


        //tooltip.visibleProperty().bind(isTooltipVisible);

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

        StackPane view = new StackPane(texture(item.getDescription().getTextureName()));
        view.setAlignment(Pos.BOTTOM_RIGHT);
        view.setUserData(new Pair<>(item, index));
        view.setTranslateX((index % 5) * 40 + 3);
        view.setTranslateY((index / 5) * 40 + 3);
        view.setPickOnBounds(true);

        view.hoverProperty().addListener((o, old, isHover) -> {
            if (isHover) {
                tooltip.setItem(item);
            }
        });

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

        var text = getUIFactoryService().newText("", Color.WHITE, 12.0);
        text.textProperty().bind(player.getInventory().getData(item).get().quantityProperty().asString());
        text.setStrokeWidth(1.5);

        view.getChildren().addAll(text);

        itemGroup.getChildren().add(view);
    }
}
