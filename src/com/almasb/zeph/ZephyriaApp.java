package com.almasb.zeph;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.orion.EquipPlace;
import com.almasb.zeph.entity.orion.GameCharacterClass;
import com.almasb.zeph.entity.orion.ObjectManager;
import com.almasb.zeph.entity.orion.Player;

public class ZephyriaApp extends GameApplication {

    private Entity player;
    private Assets assets;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Zephyria");
        settings.setVersion("0.0.1");
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
    }

    @Override
    protected void initGame(Pane gameRoot) {
        ObjectManager.load();

        initPlayer();
        initInput();

        Player playerData = player.getProperty("player_data");
        System.out.println(playerData.getHP() + " " + playerData.getSP()
                + " " + playerData.getWeaponElement() + " " + playerData.getEquip(EquipPlace.RIGHT_HAND).name
                + " " + playerData.getArmorElement());

        for (EquipPlace p : EquipPlace.values()) {
            System.out.println(playerData.getEquip(p).toString());
        }
    }

    @Override
    protected void initUI(Pane uiRoot) {

    }

    @Override
    protected void onUpdate(long now) {
        //player.getControl(GameCharacterControl.class).damage(1);
    }

    @Override
    protected void initInput() {
        addKeyPressBinding(KeyCode.W, () -> {
            player.translate(0, -5);
        });

        addKeyPressBinding(KeyCode.S, () -> {
            player.translate(0, 5);
        });

        addKeyPressBinding(KeyCode.A, () -> {
            player.translate(-5, 0);
        });

        addKeyPressBinding(KeyCode.D, () -> {
            player.translate(5, 0);
        });
    }

//    private void dropItem(Entity enemy) {
//        List<Item> dropItems = enemy.getProperty(EnemyProperty.DROP_ITEMS);
//        Item randomItem = dropItems.get((int)(Math.random() * dropItems.size()));
//
//        Entity item = randomItem.toEntity();
//        item.setPosition(enemy.getTranslateX(), enemy.getTranslateY());
//
//        addEntities(item);
//    }
//
//    private void addItem(Item item) {
//        Entity inventory = player.getProperty(PlayerProperty.INVENTORY);
//        ObservableList<Entity> list = inventory.getProperty(InventoryProperty.LIST);
//        list.add(item.toEntity());
//    }
//
    private void initPlayer() {
        player = new Player("Test", GameCharacterClass.ARCHMAGE).toEntity();
        player.setPosition(100, 100);

        VBox vbox = new VBox();

        Texture t = assets.getTexture("ic_skill_bash.png");
        t.setFitWidth(40);
        t.setFitHeight(40);

//        ProgressBar hpBar = new ProgressBar();
//        hpBar.setPrefWidth(40);
//        hpBar.progressProperty().bind(player.<IntegerProperty>getProperty(GameCharacterProperty.HP)
//                .multiply(1.0).divide(player.getProperty(GameCharacterProperty.HP_MAX)));
//
//        vbox.getChildren().addAll(t, hpBar);
        player.setGraphics(t);

        addEntities(player);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
