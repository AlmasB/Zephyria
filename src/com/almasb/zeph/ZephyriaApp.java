package com.almasb.zeph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Damage;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.ID;
import com.almasb.zeph.entity.character.Enemy;
import com.almasb.zeph.entity.character.GameCharacterClass;
import com.almasb.zeph.entity.character.Player;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

public class ZephyriaApp extends GameApplication {

    private Entity player;
    private Assets assets;

    @Override
    protected void initSettings(GameSettings settings) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        //settings.setWidth((int)bounds.getWidth());
        //settings.setHeight((int)bounds.getHeight());
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Orion RPG");
        settings.setVersion("0.0.1");
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        //settings.setFullScreen(true);
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
    }

    private Player playerData;
    private Entity selected = null;

    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);

    @Override
    protected void initGame() {
        EntityManager.load();

        selectedEffect.setInput(new Glow(0.8));

        initPlayer();
        initInput();
        initEnemies();

        bindViewportOrigin(player, (int)getWidth() / 2, (int)getHeight() / 2);
    }

    @Override
    protected void initPhysics() {}

    private ProgressBar barHP = ProgressBar.makeHPBar();
    private ProgressBar barSP = ProgressBar.makeSkillBar();
    ProgressBar barHPUI = ProgressBar.makeHPBar();
    ProgressBar barSPUI = ProgressBar.makeSkillBar();

    private Text debug = new Text();

    @Override
    protected void initUI() {

        //barHP.setShowChanges(false);
        //barSP.setShowChanges(false);


        barHP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_HP));
        barHP.currentValueProperty().bind(playerData.hpProperty());


        barSP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_SP));
        barSP.currentValueProperty().bind(playerData.spProperty());


        Texture hotbar = assets.getTexture("ui/hotbar.png");
        hotbar.setTranslateX(getWidth() / 2 - hotbar.getLayoutBounds().getWidth() / 2);
        hotbar.setTranslateY(getHeight() - hotbar.getLayoutBounds().getHeight());



        barHPUI.setTranslateX(160);
        barHPUI.setTranslateY(5);
        barHPUI.setWidth(100);
        barHPUI.setHeight(15);
        barHPUI.setLabelPosition(Position.RIGHT);

        barHPUI.maxValueProperty().bind(playerData.statProperty(Stat.MAX_HP));
        barHPUI.currentValueProperty().bind(playerData.hpProperty());


        barSPUI.setTranslateX(160);
        barSPUI.setTranslateY(25);
        barSPUI.setWidth(100);
        barSPUI.setHeight(15);
        barSPUI.setLabelPosition(Position.RIGHT);

        barSPUI.maxValueProperty().bind(playerData.statProperty(Stat.MAX_SP));
        barSPUI.currentValueProperty().bind(playerData.spProperty());


        Text textPlayerName = new Text(playerData.getName() + "\n" + playerData.getGameCharacterClass());
        textPlayerName.setTranslateX(15);
        textPlayerName.setTranslateY(15);
        textPlayerName.setFont(Font.font(18));



        Text textLevels = new Text();
        textLevels.setTranslateX(15);
        textLevels.setTranslateY(100);
        textLevels.setFont(Font.font(16));
        textLevels.textProperty().bind(new SimpleStringProperty("Base Lv. ").concat(playerData.baseLevelProperty())
                .concat("\nStat Lv. ").concat(playerData.statLevelProperty())
                .concat("\nJob Lv. ").concat(playerData.jobLevelProperty()));

        ProgressBar barXPBase = new ProgressBar();
        barXPBase.setWidth(150);
        barXPBase.setTranslateX(120);
        barXPBase.setTranslateY(90);
        barXPBase.setCurrentValue(50);

        ProgressBar barXPStat = new ProgressBar();
        barXPStat.setWidth(150);
        barXPStat.setTranslateX(120);
        barXPStat.setTranslateY(110);

        ProgressBar barXPJob = new ProgressBar();
        barXPJob.setWidth(150);
        barXPJob.setTranslateX(120);
        barXPJob.setTranslateY(130);

        Text textMoney = new Text("Money: 1000G");
        textMoney.setTranslateX(200);
        textMoney.setTranslateY(180);
        textMoney.setFont(Font.font(14));



        Pane uiPane = new Pane();
        uiPane.setPrefSize(350, 200);
        uiPane.setStyle("-fx-background-color: white;");
        uiPane.getChildren().addAll(textPlayerName, textLevels, textMoney, barHPUI, barSPUI, barXPBase, barXPStat, barXPJob);


        TitledPane pane = new TitledPane("Basic Info", uiPane);

        Accordion uiBasicInfo = new Accordion(pane);
        uiBasicInfo.setExpandedPane(pane);


        Texture invRight = assets.getTexture("ui/inventory_right.png");

        TitledPane inventoryPane = new TitledPane("Inventory", invRight);
        Accordion inventory = new Accordion(inventoryPane);
        inventory.setTranslateX(getWidth() - invRight.getLayoutBounds().getWidth() - 2);
        inventory.setTranslateY(getHeight() - 25);
        inventory.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
            if (newPane == null) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.2), inventory);
                tt.setToY(getHeight() - 25);
                tt.play();
            }
            else {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), inventory);
                tt.setToY(getHeight() - invRight.getLayoutBounds().getHeight() - 25);
                tt.play();
            }
        });

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);


        Font font = Font.font("Lucida Console", 14);

        VBox attrBox = new VBox(5);
        for (Attribute attr : Attribute.values()) {
            Text text = new Text();
            text.setFont(font);
            text.textProperty().bind(new SimpleStringProperty(attr.toString())
                    .concat(": ").concat(playerData.attributeProperty(attr))
                    .concat(" + ").concat(playerData.bAttributeProperty(attr)));

            attrBox.getChildren().add(text);
        }

        VBox statBox = new VBox(5);
        for (Stat stat : Stat.values()) {
            Text text = new Text();
            text.setFont(font);
            text.textProperty().bind(new SimpleStringProperty(stat.toString())
                    .concat(": ").concat(playerData.statProperty(stat))
                    .concat(" + ").concat(playerData.bStatProperty(stat)));

            statBox.getChildren().add(text);
        }

        TitledPane charInfoPane = new TitledPane("Char Info", new HBox(50, attrBox, statBox));
        Accordion uiCharInfo = new Accordion(charInfoPane);
        //uiCharInfo.setTranslateX(350);

        VBox infos = new VBox(uiBasicInfo, uiCharInfo);

        addUINodes(hotbar, infos, inventory, debug);
    }

    @Override
    protected void onUpdate() {
        playerData.update();

        for (Iterator<Entity> it = enemies.iterator(); it.hasNext(); ) {
            Entity enemy = it.next();
            Enemy enemyData = enemy.getProperty("enemy_data");
            enemyData.update();

            if (enemyData.getHP() <= 0) {
                it.remove();
                enemyData.onDeath(playerData);
                removeEntity(enemy);
                selected = null;
            }
        }

        if (selected != null && playerData.canAttack()) {
            playerData.resetAtkTick();

            Entity target = selected;

            Entity proj = Entity.noType();
            Circle graphics = new Circle(10);
            graphics.setFill(Color.GRAY);
            proj.setGraphics(graphics);

            proj.setPosition(player.getPosition());
            proj.addControl(new Control() {

                Point2D vector = target.getCenter().subtract(proj.getCenter()).multiply(0.016);

                @Override
                public void onUpdate(Entity entity, long now) {
                    entity.translate(vector);

                    if (entity.getBoundsInParent().intersects(target.getBoundsInParent())) {
                        removeEntity(proj);

                        Damage dmg = playerData.attack(target.getProperty("enemy_data"));
                        Entity e = Entity.noType();
                        e.setExpireTime(SECOND);
                        Text text = new Text(dmg.getValue() + (dmg.isCritical() ? "!" : ""));
                        text.setFill(dmg.isCritical() ? Color.RED : Color.WHITE);
                        text.setFont(Font.font(16));

                        e.setGraphics(text);
                        e.setPosition(target.getPosition().add(0, -40));
                        addEntities(e);

                        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), e);
                        tt.setByY(-30);
                        tt.play();
                    }
                }

            });

            addEntities(proj);
        }
    }

    private void openChat() {
        TextField field = new TextField();
        field.setTranslateX(150);
        field.setTranslateY(300);
        field.setOnAction(event -> {
            System.out.println(field.getText());
        });

        addUINodes(field);
    }

    @Override
    protected void initInput() {
        inputManager.addKeyPressBinding(KeyCode.W, () -> {
            player.translate(0, -5);
        });

        inputManager.addKeyPressBinding(KeyCode.S, () -> {
            player.translate(0, 5);
        });

        inputManager.addKeyPressBinding(KeyCode.A, () -> {
            player.translate(-5, 0);
        });

        inputManager.addKeyPressBinding(KeyCode.D, () -> {
            player.translate(5, 0);
        });

        inputManager.addKeyTypedBinding(KeyCode.ENTER, () -> {
            exit();
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
        player = new Player("Character Name", GameCharacterClass.NOVICE).toEntity();
        player.setPosition(100, 100);

        playerData = player.getProperty("player_data");

        Group vbox = new Group();

        Rectangle t = new Rectangle(40, 40);
        t.setFill(Color.BLUE);
        t.setArcWidth(15);
        t.setArcHeight(15);

        barHP.setTranslateX(-20);
        barHP.setTranslateY(60);
        barHP.setWidth(80);
        barHP.setHeight(10);
        barHP.setLabelVisible(false);

        barSP.setTranslateX(-20);
        barSP.setTranslateY(70);
        barSP.setWidth(80);
        barSP.setHeight(10);
        barSP.setLabelVisible(false);

        Text text = new Text(playerData.getName() + " Lv. 1");
        text.setFont(Font.font(14));
        text.setFill(Color.WHITE);
        text.setTranslateX(20 - text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(55);


        vbox.getChildren().addAll(t, text, barHP, barSP);

        player.setGraphics(vbox);

        Entity bg = Entity.noType();
        bg.setOnMouseClicked(event -> {
            if (selected != null) {
                selected.setEffect(null);
                selected = null;
            }
        });
        bg.setGraphics(new Rectangle(getWidth(), getHeight()));
        bg.translateXProperty().bind(player.translateXProperty().subtract(getWidth() / 2));
        bg.translateYProperty().bind(player.translateYProperty().subtract(getHeight() / 2));

        addEntities(bg, player);


    }

    private List<Entity> enemies = new ArrayList<>();

    private void initEnemies() {
        for (int i = 0; i < 10; i++) {
            Entity enemy = EntityManager.getEnemyByID(ID.Enemy.MINOR_EARTH_SPIRIT).toEntity();
            enemies.add(enemy);

            enemy.setOnMouseClicked(e -> {
                selected = enemy;
                selected.setEffect(selectedEffect);

                System.out.println("selected enemy");
            });

            Enemy enemyData = enemy.getProperty("enemy_data");

            Group vbox = new Group();
            Rectangle rect = new Rectangle(40, 40);
            rect.setFill(Color.RED);

            Text text = new Text(enemyData.getName());
            text.setFont(Font.font(14));
            text.setFill(Color.WHITE);
            text.setTranslateX(20 - text.getLayoutBounds().getWidth() / 2);
            text.setTranslateY(55);

            vbox.getChildren().addAll(rect, text);

            enemy.setPosition(random.nextInt(1000), random.nextInt(600)).setGraphics(vbox);
            addEntities(enemy);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
