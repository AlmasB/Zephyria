package com.almasb.zeph;

import com.almasb.astar.AStarGrid;
import com.almasb.astar.AStarNode;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.entity.control.OffscreenCleanControl;
import com.almasb.fxgl.entity.control.ProjectileControl;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.DynamicAnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.combat.Damage;
import com.almasb.zeph.combat.Experience;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.character.CharacterEntity;
import com.almasb.zeph.entity.character.EquipPlace;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.control.CharacterControl;
import com.almasb.zeph.entity.character.control.PlayerControl;
import com.almasb.zeph.entity.item.WeaponEntity;
import com.almasb.zeph.ui.BasicInfoView;
import com.almasb.zeph.ui.CharInfoView;
import com.almasb.zeph.ui.EquipmentView;
import com.almasb.zeph.ui.InventoryView;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ZephyriaApp extends GameApplication {

    private static final int TILE_SIZE = 64;

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
            settings.setHeight(768);
        }
        settings.setTitle("Zephyria RPG");
        settings.setVersion("0.0.1");
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setFullScreen(full);
        settings.setShowFPS(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    private List<AStarNode> path = new ArrayList<>();

    @Override
    protected void initInput() {
        Input input = getInput();

//        input.addAction(new UserAction("TargetSelection") {
//            @Override
//            protected void onActionBegin() {
//                int targetX = (int) (input.getMouseXWorld() / TILE_SIZE);
//                int targetY = (int) (input.getMouseYWorld() / TILE_SIZE);
//
//                int startX = getTileX(player);
//                int startY = getTileY(player);
//
//                //System.out.println(startX + " " + startY + " " + targetX +" " + targetY);
//
//                path = grid.getPath(startX, startY, targetX, targetY);
//
////                Entities.builder()
////                        .at(input.getMousePositionWorld())
////                        .viewFromNode(new Rectangle(5, 5))
////                        .buildAndAttach(getGameWorld());
//            }
//        }, MouseButton.PRIMARY);

        input.addAction(new UserAction("Reward XP") {
            @Override
            protected void onActionBegin() {
                playerControl.rewardXP(new Experience(30, 3, 5));
            }
        }, MouseButton.SECONDARY);

        input.addAction(new UserAction("Test Wear1") {
            WeaponEntity weapon = EntityManager.INSTANCE.getWeapon(4003);

            @Override
            protected void onActionBegin() {
                playerControl.equipWeapon(weapon);
            }

            @Override
            protected void onActionEnd() {
                playerControl.unEquipItem(EquipPlace.RIGHT_HAND);
            }
        }, KeyCode.F);
    }

    @Override
    protected void initAssets() {}

    private AStarGrid grid;

    @Override
    protected void initGame() {
        GameEntity bg = Entities.builder()
                .viewFromTexture("background.png")
                .buildAndAttach(getGameWorld());

        bg.getMainViewComponent().getView().setOnMouseClicked(e -> {
            int targetX = (int) (getInput().getMouseXWorld() / TILE_SIZE);
            int targetY = (int) (getInput().getMouseYWorld() / TILE_SIZE);

            int startX = getTileX(player);
            int startY = getTileY(player);

            path = grid.getPath(startX, startY, targetX, targetY);
        });

        grid = new AStarGrid(1280 / TILE_SIZE, 768 / TILE_SIZE);

        selectedEffect.setInput(new Glow(0.8));

        initPlayer();
        initEnemies();

        //showGrid();
        //getGameScene().getViewport().bindToEntity(player, getWidth() / 2, getHeight() / 2);
    }

    private void showGrid() {
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Rectangle r = new Rectangle(TILE_SIZE - 1, TILE_SIZE - 1, null);
                r.setTranslateX(x * TILE_SIZE);
                r.setTranslateY(y * TILE_SIZE);
                r.setStroke(Color.RED);

                getGameScene().addUINode(r);
            }
        }
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.CHARACTER) {
            @Override
            protected void onCollisionBegin(Entity proj, Entity character) {
                proj.removeFromWorld();

                Damage damage = player.getControl().attack(character);
                showDamage(damage, character.getComponentUnsafe(PositionComponent.class).getValue());
            }
        });
    }

    private Text debug = new Text();

    @Override
    protected void initUI() {
        // TODO: why does it not work?
        getGameScene().setUIMouseTransparent(false);

        getGameScene().setCursor("main.png", new Point2D(52, 10));

        Texture hotbar = getAssetLoader().loadTexture("ui/hotbar.png");
        hotbar.setTranslateX(getWidth() / 2 - hotbar.getLayoutBounds().getWidth() / 2);
        hotbar.setTranslateY(getHeight() - hotbar.getLayoutBounds().getHeight());

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        getGameScene().addUINodes(hotbar,
                new VBox(new BasicInfoView(player), new CharInfoView(player)),
                new InventoryView(player, getWidth(), getHeight()),
                new EquipmentView(player, getHeight()));
    }

    @Override
    protected void onUpdate(double tpf) {
        if (selected != null) {
            //startAttack(player, selected);
        }

        if (!path.isEmpty()) {
            AStarNode node = path.get(0);

            double dx = node.getX() * TILE_SIZE - (player).getPositionComponent().getX();
            double dy = node.getY() * TILE_SIZE - (player).getPositionComponent().getY();

            dx = Math.signum(dx);
            dy = Math.signum(dy);

            if (dx == 0 && dy == 0) {
                path.remove(0);
            } else if (dx > 0) {
                playerAnimation.setAnimationChannel(PlayerAnimation.WALK_RIGHT);
            } else if (dx < 0) {
                playerAnimation.setAnimationChannel(PlayerAnimation.WALK_LEFT);
            } else if (dy > 0) {
                playerAnimation.setAnimationChannel(PlayerAnimation.WALK_DOWN);
            } else if (dy < 0) {
                playerAnimation.setAnimationChannel(PlayerAnimation.WALK_UP);
            }

            dx *= 2;
            dy *= 2;

            player.getPositionComponent().translate(dx, dy);
        }
    }

    private int getTileX(GameEntity entity) {
        return (int) (entity.getPositionComponent().getX()) / TILE_SIZE;
    }

    private int getTileY(GameEntity entity) {
        return (int) (entity.getPositionComponent().getY()) / TILE_SIZE;
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
//        playerData.getInventory().addItem(EntityManagerOld.getWeaponByID(ID.Weapon.KNIFE));
//        playerData.getInventory().addItem(EntityManagerOld.getWeaponByID(ID.Weapon.GUT_RIPPER));
//        playerData.getInventory().addItem(EntityManagerOld.getArmorByID(ID.Armor.DOMOVOI));
//        playerData.getInventory().addItem(EntityManagerOld.getArmorByID(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL));
//        playerData.getInventory().addItem(EntityManagerOld.getArmorByID(ID.Armor.SOUL_BARRIER));
//        playerData.getInventory().addItem(EntityManagerOld.getArmorByID(ID.Armor.THANATOS_BODY_ARMOR));
//    }

//    private void initEnemies() {
//        for (int i = 0; i < 10; i++) {
//            Entity enemy = EntityManagerOld.getEnemyByID(ID.Enemy.MINOR_EARTH_SPIRIT).toEntity();
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
//                        DescriptionComponent item = EntityManagerOld.getItemByID(drop.itemID);
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
    private void showDamage(Damage damage, Point2D position) {
        Text text = new Text(damage.getValue() + (damage.isCritical() ? "!" : ""));
        text.setFill(damage.isCritical() ? Color.RED : Color.WHITE);
        text.setFont(Font.font(damage.isCritical() ? 22 : 16));

        EntityView view = new EntityView();
        view.addNode(text);
        view.setTranslateX(position.getX());
        view.setTranslateY(position.getY());

        getGameScene().addGameView(view);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), view);
        tt.setByY(-30);
        tt.setOnFinished(e -> getGameScene().removeGameView(view));
        tt.play();
    }

    private enum EntityType {
        PLAYER, CHARACTER, PROJECTILE
    }

    private enum PlayerAnimation implements AnimationChannel {
        WALK_RIGHT(11, 9),
        WALK_LEFT(9, 9),
        WALK_UP(8, 9),
        WALK_DOWN(10, 9),
        ATTACK_RIGHT(19, 13),
        ATTACK_LEFT(17, 13),
        ATTACK_UP(16, 13),
        ATTACK_DOWN(18, 13);

        int row;
        int cycle;

        PlayerAnimation(int row, int cycle) {
            this.row = row;
            this.cycle = cycle;
        }

        @Override
        public Rectangle2D area() {
            return new Rectangle2D(0, 64*row, 64*cycle, 64);
        }

        @Override
        public int frames() {
            return cycle;
        }

        @Override
        public Duration duration() {
            return Duration.seconds(1.2);
        }
    }

    private DynamicAnimatedTexture playerAnimation;

    private PlayerEntity initPlayer() {
        player = new PlayerEntity();
        player.addComponent(new DescriptionComponent(1, "Player", "Player Description", "chars/players/player_full.png"));
        player.addControl(new PlayerControl());

        player.getTypeComponent().setValue(EntityType.PLAYER);
        player.addComponent(new CollidableComponent(true));

        player.getPositionComponent().setValue(TILE_SIZE * 4, TILE_SIZE * 4);

        playerAnimation = FXGL.getAssetLoader().loadTexture("chars/players/player_full.png")
                .toDynamicAnimatedTexture(PlayerAnimation.WALK_RIGHT, PlayerAnimation.values());

        player.getMainViewComponent().setView(playerAnimation, true);

        playerControl = player.getControl();

        getGameWorld().addEntity(player);

        return player;
    }

    private void initEnemies() {
        CharacterEntity enemy = new CharacterEntity();
        enemy.addComponent(new DescriptionComponent(2, "Skeleton-Archer", "Description", "chars/enemies/enemy.png"));
        enemy.addControl(new CharacterControl());

        enemy.getTypeComponent().setValue(EntityType.CHARACTER);
        enemy.addComponent(new CollidableComponent(true));

        spawnEntity(2, 4, enemy);

        enemy.getMainViewComponent().getView().setOnMouseClicked(e -> {

            selectedPoint = null;
            e.consume();

            path = new ArrayList<>();

            Point2D vector = enemy.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld());

            if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
                if (vector.getX() >= 0) {
                    playerAnimation.setAnimationChannel(PlayerAnimation.ATTACK_RIGHT);
                } else {
                    playerAnimation.setAnimationChannel(PlayerAnimation.ATTACK_LEFT);
                }
            } else {
                if (vector.getY() >= 0) {
                    playerAnimation.setAnimationChannel(PlayerAnimation.ATTACK_DOWN);
                } else {
                    playerAnimation.setAnimationChannel(PlayerAnimation.ATTACK_UP);
                }
            }

            getMasterTimer().runOnceAfter(() -> {
                Entities.builder()
                        .type(EntityType.PROJECTILE)
                        .at(player.getBoundingBoxComponent().getCenterWorld())
                        .viewFromTextureWithBBox("projectiles/arrow2.png")
                        .with(new ProjectileControl(enemy.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld()), 5))
                        .with(new OffscreenCleanControl())
                        .with(new CollidableComponent(true))
                        .buildAndAttach(getGameWorld());
            }, Duration.seconds(0.8));
        });
    }

    private void spawnEntity(int x, int y, GameEntity entity) {
        entity.getPositionComponent().setValue(x * TILE_SIZE, y * TILE_SIZE);
        entity.getMainViewComponent().setTexture(entity.getComponentUnsafe(DescriptionComponent.class).getTextureName(), true);

        getGameWorld().addEntity(entity);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
