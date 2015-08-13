package com.almasb.zeph;

import java.util.List;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.GameSettings;
import com.almasb.fxgl.asset.Assets;
import com.almasb.fxgl.asset.Texture;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.FXGLEvent;
import com.almasb.fxgl.event.InputManager.Mouse;
import com.almasb.fxgl.event.UserAction;
import com.almasb.fxgl.time.TimerManager;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.Events.Event;
import com.almasb.zeph.combat.Damage;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.control.PassiveControl;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.GameEntity;
import com.almasb.zeph.entity.ID;
import com.almasb.zeph.entity.character.Enemy;
import com.almasb.zeph.entity.character.GameCharacter;
import com.almasb.zeph.entity.character.GameCharacterClass;
import com.almasb.zeph.entity.character.Player;
import com.almasb.zeph.entity.item.DroppableItem;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.util.Duration;

public class ZephyriaApp extends GameApplication {

    private Assets assets;

    private Entity player;
    private Player playerData;
    private Entity selected = null;
    private Point2D selectedPoint = null;

    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);

    @Override
    protected void initSettings(GameSettings settings) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        boolean full = true;
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
    }

    @Override
    protected void initAssets() throws Exception {
        assets = assetManager.cache();
        R.assets = assets;
    }

    @Override
    protected void initGame() {
        EntityManager.load();

        selectedEffect.setInput(new Glow(0.8));

        initPlayer();
        initInput();
        initEnemies();

        sceneManager.bindViewportOrigin(player, (int)getWidth() / 2, (int)getHeight() / 2);
    }

    @Override
    protected void initPhysics() {}

    private Text debug = new Text();

    @Override
    protected void initUI() {
        //sceneManager.getMainScene().setCursor(new ImageCursor(R.assets.getTexture("ui/cursors/main.png").getImage(), 52, 10));

        Texture hotbar = assets.getTexture("ui/hotbar.png");
        hotbar.setTranslateX(getWidth() / 2 - hotbar.getLayoutBounds().getWidth() / 2);
        hotbar.setTranslateY(getHeight() - hotbar.getLayoutBounds().getHeight());

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        sceneManager.addUINodes(hotbar,
                new VBox(new BasicInfoView(playerData),new CharInfoView(playerData)),
                new EquipmentView(playerData, getHeight()),
                new InventoryView(playerData, getWidth(), getHeight()),
                debug);
    }

    @Override
    protected void onUpdate() {
        if (selected != null) {
            startAttack(player, selected);
        }

        if (selectedPoint != null) {
            player.translate(selectedPoint.subtract(player.getPosition()).normalize().multiply(5));
            if (selectedPoint.distance(player.getPosition()) < 5)
                selectedPoint = null;
        }
    }

    private Mouse mouse = inputManager.getMouse();

    @Override
    protected void initInput() {
        inputManager.addAction(new UserAction("Exit") {
            @Override
            protected void onActionBegin() {
                exit();
            }
        }, KeyCode.L);

        inputManager.addAction(new UserAction("TargetSelection") {
            @Override
            protected void onAction() {
                selectedPoint = new Point2D(mouse.x, mouse.y);
            }
        }, MouseButton.PRIMARY);
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

    private void initPlayer() {
        player = new Player("Debug", GameCharacterClass.NOVICE).toEntity();
        player.setPosition(getWidth() / 2, getHeight() / 2);

        playerData = player.getControl(Player.class);

        Group vbox = new Group();

        Rectangle t = new Rectangle(40, 40);
        t.setFill(Color.BLUE);
        t.setArcWidth(15);
        t.setArcHeight(15);

        ProgressBar barHP = makeHPBar();
        ProgressBar barSP = makeSkillBar();

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

        barHP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_HP));
        barHP.currentValueProperty().bind(playerData.hpProperty());

        barSP.maxValueProperty().bind(playerData.statProperty(Stat.MAX_SP));
        barSP.currentValueProperty().bind(playerData.spProperty());

        Text text = new Text();
        text.setFont(Font.font(14));
        text.setFill(Color.WHITE);
        text.textProperty().bind(playerData.nameProperty().concat(" Lv. ").concat(playerData.baseLevelProperty()));
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

        Texture back = assets.getTexture("map1.png");
        //back.setFitWidth(getWidth());
        //back.setFitHeight(getHeight());

        bg.setGraphics(back);
        //bg.translateXProperty().bind(player.translateXProperty().subtract(getWidth() / 2));
        //bg.translateYProperty().bind(player.translateYProperty().subtract(getHeight() / 2));


        Entity bg0 = Entity.noType();
        bg0.setGraphics(assets.getTexture("background.png"));
        bg0.setTranslateX(-getWidth());

        Entity bg1 = Entity.noType();
        bg1.setGraphics(assets.getTexture("background.png"));
        bg1.setTranslateY(-getHeight());






        sceneManager.addEntities(bg0, bg1, bg);

        for (int i = 0; i < 20; i++) {
            Entity tree = Entity.noType();
            tree.setPosition(random.nextInt(1500), random.nextInt(1000));
            tree.setGraphics(assets.getTexture("tree.png"));

            sceneManager.addEntities(tree);
        }

        sceneManager.addEntities(player);


        playerData.getInventory().addItem(EntityManager.getWeaponByID(ID.Weapon.KNIFE));
        playerData.getInventory().addItem(EntityManager.getWeaponByID(ID.Weapon.GUT_RIPPER));
        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.DOMOVOI));
        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL));
        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.SOUL_BARRIER));
        playerData.getInventory().addItem(EntityManager.getArmorByID(ID.Armor.THANATOS_BODY_ARMOR));
    }

    private void initEnemies() {
        for (int i = 0; i < 10; i++) {
            Entity enemy = EntityManager.getEnemyByID(ID.Enemy.MINOR_EARTH_SPIRIT).toEntity();
            enemy.setPosition(random.nextInt(1000), random.nextInt(600));
            enemy.setOnMouseClicked(e -> {
                selected = enemy;
                selected.setEffect(selectedEffect);
            });
            enemy.addFXGLEventHandler(Event.ATTACKING, event -> {
                startAttack(enemy, event.getSource());
            });
            enemy.addFXGLEventHandler(Event.DEATH, event -> {
                Enemy enemyData = enemy.getControl(Enemy.class);

                playerData.rewardMoney(GameMath.random(enemyData.getBaseLevel() * 100));
                playerData.rewardXP(enemyData.getXP());

                List<DroppableItem> drops = enemyData.getDrops();
                for (DroppableItem drop : drops) {
                    if (GameMath.checkChance(drop.dropChance)) {
                        GameEntity item = EntityManager.getItemByID(drop.itemID);
                        dropItem(item, enemy.getPosition());
                    }
                }

                sceneManager.removeEntity(enemy);
                selected = null;
            });
            enemy.addControl(new PassiveControl(250));
            enemy.setCursor(Cursor.CROSSHAIR);

            sceneManager.addEntities(enemy);
        }
    }

    private void startAttack(Entity attacker, Entity target) {
        if (!attacker.isActive() || !target.isActive())
            return;

        GameCharacter a = attacker.getControl(GameCharacter.class);

        if (!a.canAttack())
            return;

        a.resetAtkTick();

        // TODO: this should be specific to each char type
        Entity proj = Entity.noType();

        proj.setGraphics(R.assets.getTexture("projectile.png"));
        proj.setPosition(attacker.getPosition());
        proj.addControl(new Control() {
            private Point2D vector = target.getCenter().subtract(proj.getCenter()).multiply(0.016);

            @Override
            public void onUpdate(Entity entity, long now) {
                entity.getTransforms().clear();
                entity.getTransforms().add(new Rotate(Math.toDegrees(Math.atan2(vector.getY(), vector.getX()))));
                entity.translate(vector);

                if (entity.getPosition().distance(attacker.getPosition()) >= 600)
                    sceneManager.removeEntity(proj);

                if (!target.isActive())
                    sceneManager.removeEntity(proj);

                if (entity.getBoundsInParent().intersects(target.getBoundsInParent())) {
                    sceneManager.removeEntity(proj);

                    target.fireFXGLEvent(new FXGLEvent(Event.BEING_ATTACKED, attacker));

                    Damage damage = a.attack(target.getControl(GameCharacter.class));
                    showDamage(damage, target.getPosition());
                }
            }
        });

        sceneManager.addEntities(proj);
    }

    private void dropItem(GameEntity item, Point2D position) {
        Entity e = item.toEntity();
        e.setPosition(position);
        e.setOnMouseClicked(event -> {
            sceneManager.removeEntity(e);
            playerData.getInventory().addItem(item);
        });

        e.setCursor(Cursor.CLOSED_HAND);

        sceneManager.addEntities(e);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.3), e);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setByX(random.nextInt(20) - 10);
        tt.setByY(10 + random.nextInt(10));
        tt.play();
    }

    private void showDamage(Damage damage, Point2D position) {
        Entity e = Entity.noType();
        e.setExpireTime(TimerManager.SECOND);
        Text text = new Text(damage.getValue() + (damage.isCritical() ? "!" : ""));
        text.setFill(damage.isCritical() ? Color.RED : Color.WHITE);
        text.setFont(Font.font(damage.isCritical() ? 18 : 16));

        e.setGraphics(text);
        e.setPosition(position);
        sceneManager.addEntities(e);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), e);
        tt.setByY(-30);
        tt.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
