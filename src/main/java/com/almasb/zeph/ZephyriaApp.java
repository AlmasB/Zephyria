package com.almasb.zeph;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.entity.Data;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.control.PlayerControl;
import com.almasb.zeph.entity.item.WeaponEntity;
import com.almasb.zeph.ui.BasicInfoView;
import com.almasb.zeph.ui.CharInfoView;
import com.almasb.zeph.ui.InventoryView;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class ZephyriaApp extends GameApplication {

    private PlayerEntity player;
    private PlayerControl playerControl;
    private Entity selected = null;
    private Point2D selectedPoint = null;

    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);

    @Override
    protected void initSettings(GameSettings settings) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        boolean full = false;
        if (full) {
            settings.setWidth((int)bounds.getWidth());
            settings.setHeight((int)bounds.getHeight());
        }
        else {
            settings.setWidth(1280);
            settings.setHeight(720);
        }
        settings.setTitle("Orion RPG");
        settings.setVersion("0.0.1");
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setFullScreen(full);
        settings.setShowFPS(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("TargetSelection") {
            @Override
            protected void onAction() {
                selectedPoint = input.getMousePositionWorld();
            }
        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Test Wear1") {
            WeaponEntity weapon = new WeaponEntity(Data.Weapon.INSTANCE.HANDS());

            @Override
            protected void onActionBegin() {
                weapon.getData().onEquip(player);
            }

            @Override
            protected void onActionEnd() {
                weapon.getData().onUnEquip(player);
            }
        }, KeyCode.F);
    }

    @Override
    protected void initAssets() {}

    @Override
    protected void initGame() {
        Entity bg = Entities.builder()
                .viewFromTexture("background.png")
                .buildAndAttach(getGameWorld());


        EntityManager.load();

        selectedEffect.setInput(new Glow(0.8));

        player = initPlayer();
        //initEnemies();

//        getGameScene().getViewport().bindToEntity(player, getWidth() / 2, getHeight() / 2);
    }

    @Override
    protected void initPhysics() {}

    private Text debug = new Text();

    @Override
    protected void initUI() {
        getGameScene().setCursor("main.png", new Point2D(52, 10));

        Texture hotbar = getAssetLoader().loadTexture("ui/hotbar.png");
        hotbar.setTranslateX(getWidth() / 2 - hotbar.getLayoutBounds().getWidth() / 2);
        hotbar.setTranslateY(getHeight() - hotbar.getLayoutBounds().getHeight());

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        getGameScene().addUINodes(hotbar,
                new VBox(new BasicInfoView(player), new CharInfoView(player)),
                new InventoryView(null, getWidth(), getHeight()));
    }

    @Override
    protected void onUpdate(double tpf) {
        if (selected != null) {
            //startAttack(player, selected);
        }

//        if (selectedPoint != null) {
//            player.translate(selectedPoint.subtract(player.getPosition()).normalize().multiply(5));
//            if (selectedPoint.distance(player.getPosition()) < 5)
//                selectedPoint = null;
//        }
    }

    public static ProgressBar makeHPBar() {
        ProgressBar bar = new ProgressBar(false);
        bar.setHeight(25);
        bar.setFill(Color.GREEN.brighter());
        bar.setTraceFill(Color.GREEN.brighter());
        bar.setLabelVisible(true);
        return bar;
    }

    public static ProgressBar makeSkillBar() {
        ProgressBar bar = new ProgressBar(false);
        bar.setHeight(25);
        bar.setFill(Color.BLUE.brighter().brighter());
        bar.setTraceFill(Color.BLUE);
        bar.setLabelVisible(true);
        return bar;
    }

//    private void initPlayerOld() {
//        player = new PlayerControl("Debug", GameCharacterClass.NOVICE).toEntity();
//        player.setPosition(getWidth() / 2, getHeight() / 2);
//
//        playerData = player.getControl(PlayerControl.class).get();
//
//        Group vbox = new Group();
//
//        Rectangle t = new Rectangle(40, 40);
//        t.setFill(Color.BLUE);
//        t.setArcWidth(15);
//        t.setArcHeight(15);
//
//        ProgressBar barHP = makeHPBar();
//        ProgressBar barSP = makeSkillBar();
//
//        barHP.setTranslateX(-20);
//        barHP.setTranslateY(60);
//        barHP.setWidth(80);
//        barHP.setHeight(10);
//        barHP.setLabelVisible(false);
//
//        barSP.setTranslateX(-20);
//        barSP.setTranslateY(70);
//        barSP.setWidth(80);
//        barSP.setHeight(10);
//        barSP.setLabelVisible(false);
//
//        barHP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_HP));
//        barHP.currentValueProperty().bind(playerData.hpProperty());
//
//        barSP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_SP));
//        barSP.currentValueProperty().bind(playerData.spProperty());
//
//        Text text = new Text();
//        text.setFont(Font.font(14));
//        text.setFill(Color.WHITE);
//        text.textProperty().bind(playerData.nameProperty().concat(" Lv. ").concat(playerData.baseLevelProperty()));
//        text.setTranslateX(20 - text.getLayoutBounds().getWidth() / 2);
//        text.setTranslateY(55);
//
//
//        vbox.getChildren().addAll(t, text, barHP, barSP);
//
//        player.setSceneView(vbox);
//
//        Entity bg = Entity.noType();
////        bg.setOnMouseClicked(event -> {
////            if (selected != null) {
////                selected.setEffect(null);
////                selected = null;
////            }
////        });
//
//        Texture back = assets.getTexture("map1.png");
//        //back.setFitWidth(getWidth());
//        //back.setFitHeight(getHeight());
//
//        bg.setSceneView(back);
//        //bg.translateXProperty().bind(player.translateXProperty().subtract(getWidth() / 2));
//        //bg.translateYProperty().bind(player.translateYProperty().subtract(getHeight() / 2));
//
//
//        Entity bg0 = Entity.noType();
//        bg0.setSceneView(assets.getTexture("background.png"));
//        bg0.setX(-getWidth());
//
//        Entity bg1 = Entity.noType();
//        bg1.setSceneView(assets.getTexture("background.png"));
//        bg1.setY(-getHeight());
//
//
//
//
//
//
//        getGameWorld().addEntities(bg0, bg1, bg);
//
//        for (int i = 0; i < 20; i++) {
//            Entity tree = Entity.noType();
//            tree.setPosition(new Random().nextInt(1500), new Random().nextInt(1000));
//            tree.setSceneView(assets.getTexture("tree.png"));
//
//            getGameWorld().addEntities(tree);
//        }
//
//        getGameWorld().addEntities(player);
//
//
//        playerData.getInventory().addItem(EntityManager.getWeaponByID(ID.Weapon.KNIFE));
//        playerData.getInventory().addItem(EntityManager.getWeaponByID(ID.Weapon.GUT_RIPPER));
//        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.DOMOVOI));
//        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL));
//        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.SOUL_BARRIER));
//        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.THANATOS_BODY_ARMOR));
//    }

//    private void initEnemies() {
//        for (int i = 0; i < 10; i++) {
//            Entity enemy = EntityManager.getEnemyByID(ID.Enemy.MINOR_EARTH_SPIRIT).toEntity();
//            enemy.setPosition(new Random().nextInt(1000), new Random().nextInt(600));
////            enemy.getSceneView().ifPresent().setOnMouseClicked(e -> {
////                selected = enemy;
////                selected.setEffect(selectedEffect);
////            });
//            enemy.addFXGLEventHandler(Event.ATTACKING, event -> {
//                startAttack(enemy, event.getSource());
//            });
//            enemy.addFXGLEventHandler(Event.DEATH, event -> {
//                EnemyControl enemyData = enemy.getControl(EnemyControl.class).get();
//
//                playerData.rewardMoney(GameMath.random(enemyData.getBaseLevel() * 100));
//                playerData.rewardXP(enemyData.getXP());
//
//                List<DroppableItem> drops = enemyData.getDrops();
//                for (DroppableItem drop : drops) {
//                    if (GameMath.checkChance(drop.dropChance)) {
//                        DescriptionComponent item = EntityManager.getItemByID(drop.itemID);
//                        dropItem(item, enemy.getPosition());
//                    }
//                }
//
//                getGameWorld().removeEntity(enemy);
//                selected = null;
//            });
//            enemy.addControl(new PassiveControl(250));
//            enemy.getSceneView().get().setCursor(Cursor.CROSSHAIR);
//
//            getGameWorld().addEntities(enemy);
//        }
//    }

//    private void startAttack(Entity attacker, Entity target) {
//        if (!attacker.isActive() || !target.isActive())
//            return;
//
//        CharacterControl a = attacker.getControl(CharacterControl.class).get();
//
//        if (!a.canAttack())
//            return;
//
//        a.resetAtkTick();
//
//        // TODO: this should be specific to each char type
//        Entity proj = Entity.noType();
//
//        proj.setSceneView(R.assets.getTexture("projectile.png"));
//        proj.setPosition(attacker.getPosition());
//        //proj.addControl(new ProjectileControl());
//
//        getGameWorld().addEntities(proj);
//    }

//    private class ProjectileControl implements Control {
//        private Point2D vector = target.getCenter().subtract(proj.getCenter()).multiply(0.016);
//
//        @Override
//        public void onUpdate(Entity entity) {
//            entity.setRotation(Math.toDegrees(Math.atan2(vector.getY(), vector.getX())));
//            entity.translate(vector);
//
//            if (entity.getPosition().distance(attacker.getPosition()) >= 600)
//                getGameWorld().removeEntity(proj);
//
//            if (!target.isActive())
//                getGameWorld().removeEntity(proj);
//
//            if (entity.isCollidingWith(target)) {
//                getGameWorld().removeEntity(proj);
//
//                target.fireFXGLEvent(new FXGLEvent(Event.BEING_ATTACKED, attacker));
//
//                Damage damage = a.attack(target.getControl(GameCharacter.class).get());
//                showDamage(damage, target.getPosition());
//            }
//        }
//    }

//    private void dropItem(DescriptionComponent item, Point2D position) {
//        Entity e = item.toEntity();
//        e.setPosition(position);
////        e.setOnMouseClicked(event -> {
////            sceneManager.removeEntity(e);
////            playerData.getInventory().addItem(item);
////        });
////
////        e.setCursor(Cursor.CLOSED_HAND);
//
//        getGameWorld().addEntities(e);
//
//        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.3), e.getSceneView().get());
//        tt.setInterpolator(Interpolator.EASE_IN);
//        tt.setByX(new Random().nextInt(20) - 10);
//        tt.setByY(10 + new Random().nextInt(10));
//        tt.play();
//    }
//
//    private void showDamage(Damage damage, Point2D position) {
//        Entity e = Entity.noType();
//        e.setExpireTime(Duration.seconds(1));
//        Text text = new Text(damage.getValue() + (damage.isCritical() ? "!" : ""));
//        text.setFill(damage.isCritical() ? Color.RED : Color.WHITE);
//        text.setFont(Font.font(damage.isCritical() ? 18 : 16));
//
//        e.setSceneView(text);
//        e.setPosition(position);
//        getGameWorld().addEntities(e);
//
//        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), e.getSceneView().get());
//        tt.setByY(-30);
//        tt.play();
//    }

    private PlayerEntity initPlayer() {
        PlayerEntity player = new PlayerEntity();
        player.addComponent(new DescriptionComponent(1, "Player", "Player Description", "chars/enemies/enemy.png"));
        player.addControl(new PlayerControl());

        player.getPositionComponent().setValue(400, 400);
        player.getMainViewComponent().setView(new Rectangle(40, 40));

        playerControl = player.getControl();

        getGameWorld().addEntity(player);

        return player;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
