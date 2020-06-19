package com.almasb.zeph.ui;

import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.MDIWindow;
import com.almasb.zeph.character.PlayerEntity;
import com.almasb.zeph.item.Armor;
import com.almasb.zeph.item.Item;
import com.almasb.zeph.item.UsableItem;
import com.almasb.zeph.item.Weapon;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.texture;

public class InventoryView extends MDIWindow {

    private Map<Integer, Boolean> slots = new HashMap<>();
    private Pane root = new Pane();

    private ListChangeListener<Item> listener;

    private PlayerEntity player;

    public InventoryView(PlayerEntity player, double width, double height) {
        //super("Inventory", WindowDecor.MINIMIZE);

        relocate(width - 202, height - 315);

        //setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(202, 315);
        setCanResize(false);

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

                            for (Iterator<Node> it = root.getChildren().iterator(); it.hasNext(); ) {
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

        Texture background = getAssetLoader().loadTexture("ui/inventory_right.png");
        root.getChildren().add(background);

        setContentPane(root);

//        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
//        getRightIcons().get(0).setOnAction(e -> {
//            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), root);
//            st.setFromY(isMinimized() ? 0 : 1);
//            st.setToY(isMinimized() ? 1 : 0);
//            st.play();
//
//            handler.handle(e);
//        });
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
        view.setTranslateX((index % 5) * 40);
        view.setTranslateY((index / 5) * 40);
        view.setOnMouseClicked(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                if (item instanceof Weapon) {
                    player.getPlayerComponent().equipWeapon((Weapon) item);
                } else if (item instanceof Armor) {
                    player.getPlayerComponent().equipArmor((Armor) item);
                } else if (item instanceof UsableItem) {
                    player.getPlayerComponent().useItem((UsableItem) item);
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

        root.getChildren().add(view);
    }
}
