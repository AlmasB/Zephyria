package com.almasb.zeph;

import com.almasb.astar.AStarGrid;
import com.almasb.astar.AStarNode;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.MainViewComponent;
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
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.zeph.combat.Damage;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.entity.Data;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.character.CharacterEntity;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.component.CharacterDataComponent;
import com.almasb.zeph.entity.character.control.CharacterControl;
import com.almasb.zeph.entity.character.control.PlayerControl;
import com.almasb.zeph.entity.item.ArmorEntity;
import com.almasb.zeph.entity.item.WeaponEntity;
import com.almasb.zeph.entity.item.WeaponType;
import com.almasb.zeph.entity.item.component.OwnerComponent;
import com.almasb.zeph.entity.skill.SkillEntity;
import com.almasb.zeph.entity.skill.SkillTargetType;
import com.almasb.zeph.entity.skill.SkillType;
import com.almasb.zeph.entity.skill.SkillUseResult;
import com.almasb.zeph.ui.*;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZephyriaApp extends GameApplication {

    private static final int TILE_SIZE = 64;
    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;

    private PlayerEntity player;
    private PlayerControl playerControl;

    private ObjectProperty<Entity> selected = new SimpleObjectProperty<>();

    private boolean selectingSkillTargetArea = false;
    private boolean selectingSkillTargetChar = false;
    private int selectedSkillIndex = -1;

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

        input.addAction(new UserAction("Test Attack") {
            @Override
            protected void onActionBegin() {
                getGameWorld().getEntitiesByType(EntityType.CHARACTER)
                        .forEach(ch -> startAttack(ch, player));
            }
        }, KeyCode.O);

        input.addAction(new UserAction("Test Drop") {
            @Override
            protected void onActionBegin() {
                dropItem(new WeaponEntity(Data.Weapon.INSTANCE.BROADSWORD()), new Point2D(500, 300));
            }
        }, KeyCode.K);

        for (int i = 1; i <= 9; i++) {
            KeyCode key = KeyCode.getKeyCode(i + "");

            final int index = i - 1;

            input.addAction(new UserAction("Hotbar Skill " + i) {
                @Override
                protected void onActionBegin() {
                    onHotbarSkill(index);
                }
            }, key);
        }
    }

    private void onHotbarSkill(int index) {
        if (index < player.getSkills().size()) {
            SkillEntity skill = player.getSkills().get(index);

            if (skill.getData().getType() == SkillType.PASSIVE) {
                // skill is passive and is always on
                return;
            }

            if (skill.getData().getTargetTypes().contains(SkillTargetType.SELF)) {

                // use skill immediately since player is the target
                SkillUseResult result = playerControl.useSelfSkill(index);
            } else if (skill.getData().getTargetTypes().contains(SkillTargetType.AREA)) {

                // let player select the area
                selectingSkillTargetArea = true;
                selectedSkillIndex = index;
            } else {

                selectingSkillTargetChar = true;
                selectedSkillIndex = index;
            }
        }
    }

    private void useAreaSkill() {
        // TODO: we should fire projectile based on skill data component
        SkillUseResult result = playerControl.useAreaSkill(selectedSkillIndex, getInput().getMousePositionWorld());
    }

    private void useTargetSkill(CharacterEntity ch) {
        // TODO: we should fire projectile based on skill data component
        SkillUseResult result = playerControl.useTargetSkill(selectedSkillIndex, ch);
        showDamage(result.getDamage(), ch.getPositionComponent().getValue());
    }

    @Override
    protected void initAssets() {}

    private AStarGrid grid;

    @Override
    protected void initGame() {
        initBackground();

        grid = new AStarGrid(MAP_WIDTH, MAP_HEIGHT);

        selectedEffect.setInput(new Glow(0.8));

        initPlayer();
        initEnemies();

        //showGrid();
        getGameScene().getViewport().setBounds(0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        getGameScene().getViewport().bindToEntity(player, getWidth() / 2, getHeight() / 2);

        selected.addListener((observable, oldValue, newEntity) -> {
            if (oldValue != null) {
                oldValue.getComponent(MainViewComponent.class).ifPresent(c -> {
                    c.getView().setEffect(null);
                });
            }

            if (newEntity != null) {
                newEntity.getComponent(MainViewComponent.class).ifPresent(c -> {
                    c.getView().setEffect(selectedEffect);
                });

                // TODO: at some point we need to check if it's ally or enemy based on skill target type
                if (selectingSkillTargetChar) {
                    if (newEntity instanceof CharacterEntity) {
                        useTargetSkill((CharacterEntity) newEntity);
                    }

                    selectingSkillTargetChar = false;
                    selectedSkillIndex = -1;
                }
            }
        });
    }

    private void initBackground() {
        GameEntity bg = Entities.builder()
                .buildAndAttach(getGameWorld());

        Region region = new Region();
        region.setPrefSize(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);

        BackgroundImage bgImg = new BackgroundImage(getAssetLoader().loadTexture("tile.png").getImage(),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        region.setBackground(new Background(bgImg));

        bg.getMainViewComponent().setView(region);

        bg.getMainViewComponent().getView().setOnMouseClicked(e -> {

            if (selectingSkillTargetArea) {
                useAreaSkill();

                selectingSkillTargetArea = false;
                selectedSkillIndex = -1;
                return;
            }

            selected.set(null);

            int targetX = (int) (getInput().getMouseXWorld() / TILE_SIZE);
            int targetY = (int) (getInput().getMouseYWorld() / TILE_SIZE);

            int startX = getTileX(player);
            int startY = getTileY(player);

            path = grid.getPath(startX, startY, targetX, targetY);
        });

        bg.getMainViewComponent().setRenderLayer(new RenderLayer() {
            @Override
            public String name() {
                return "BACKGROUND";
            }

            @Override
            public int index() {
                return 0;
            }
        });

        spawnTree(0, 0);
        spawnTree(0, MAP_HEIGHT - 1);
        spawnTree(MAP_WIDTH - 1, 0);
        spawnTree(MAP_WIDTH - 1, MAP_HEIGHT - 1);
    }

    private void spawnTree(int x, int y) {
        GameEntity tree = Entities.builder()
                .at(x * TILE_SIZE, y * TILE_SIZE - 85 + 64)
                .viewFromTexture("tree2.png")
                .buildAndAttach(getGameWorld());
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
            protected void onCollisionBegin(Entity proj, Entity target) {
                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
                    return;

                proj.removeFromWorld();

                CharacterEntity character = (CharacterEntity) target;

                Damage damage = player.getControl().attack(character);
                showDamage(damage, character.getComponentUnsafe(PositionComponent.class).getValue());

                if (character.getHp().getValue() <= 0) {
                    // TODO: reward based on level differences
                    playerControl.rewardMoney(new Random().nextInt(character.getBaseLevel().get()));
                    playerControl.rewardXP(character.getData().getRewardXP());

                    List<Pair<Integer, Integer> > drops = character.getData().getDropItems();
                    drops.forEach(p -> {
                        if (GameMath.checkChance(p.getSecond())) {
                            dropItem(EntityManager.INSTANCE.getItem(p.getFirst()),
                                    character.getPositionComponent().getValue());
                        }
                    });

                    character.getMainViewComponent().getView().setOnMouseClicked(null);
                    selected.set(null);

                    character.getData().getAnimation().setAnimationChannel(CharacterAnimation.DEATH);

                    getMasterTimer().runOnceAfter(character::removeFromWorld, Duration.seconds(0.9));

                    initEnemies();
                }
            }
        });
    }

    private Text debug = new Text();

    @Override
    protected void initUI() {
        getGameScene().setUIMouseTransparent(false);
        getGameScene().setCursor("main.png", new Point2D(52, 10));

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        getGameScene().addUINodes(
                new HotbarView(player),
                new BasicInfoView(player),
                new CharInfoView(player),
                new InventoryView(player, getWidth(), getHeight()),
                new EquipmentView(player, getWidth(), getHeight()));
    }

    @Override
    protected void onUpdate(double tpf) {

        // TODO: not all selected entities should be attacked, e.g. merchants
        if (selected.get() != null) {
            startAttack(player, selected.get());
        }

        while (selected.get() == null && !path.isEmpty()) {
            AStarNode node = path.get(0);

            double dx = node.getX() * TILE_SIZE - (player).getPositionComponent().getX();
            double dy = node.getY() * TILE_SIZE - (player).getPositionComponent().getY();

            dx = Math.signum(dx);
            dy = Math.signum(dy);

            if (dx == 0 && dy == 0) {
                path.remove(0);
                continue;
            } else if (dx > 0) {
                playerAnimation.setAnimationChannel(CharacterAnimation.WALK_RIGHT);
            } else if (dx < 0) {
                playerAnimation.setAnimationChannel(CharacterAnimation.WALK_LEFT);
            } else if (dy > 0) {
                playerAnimation.setAnimationChannel(CharacterAnimation.WALK_DOWN);
            } else if (dy < 0) {
                playerAnimation.setAnimationChannel(CharacterAnimation.WALK_UP);
            }

            dx *= 2;
            dy *= 2;

            player.getPositionComponent().translate(dx, dy);
            break;
        }
    }

    private int getTileX(GameEntity entity) {
        return (int) (entity.getPositionComponent().getX()) / TILE_SIZE;
    }

    private int getTileY(GameEntity entity) {
        return (int) (entity.getPositionComponent().getY()) / TILE_SIZE;
    }

    private void startAttack(Entity attacker, Entity target) {
        if (!attacker.isActive() || !target.isActive())
            return;

        CharacterControl a = attacker.getControlUnsafe(CharacterControl.class);

        if (!a.canAttack())
            return;

        a.resetAtkTick();

        attack((CharacterEntity) attacker, (CharacterEntity) target);
    }

    private void attack(CharacterEntity attacker, GameEntity target) {
        Point2D vector = target.getBoundingBoxComponent().getCenterWorld().subtract(attacker.getBoundingBoxComponent().getCenterWorld());

        DynamicAnimatedTexture animation = attacker.getData().getAnimation();

        if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
            if (vector.getX() >= 0) {
                animation.setAnimationChannel(CharacterAnimation.SLASH_RIGHT);
            } else {
                animation.setAnimationChannel(CharacterAnimation.SLASH_LEFT);
            }
        } else {
            if (vector.getY() >= 0) {
                animation.setAnimationChannel(CharacterAnimation.SLASH_DOWN);
            } else {
                animation.setAnimationChannel(CharacterAnimation.SLASH_UP);
            }
        }

        // TODO: generalize

        attacker.getControl(PlayerControl.class).ifPresent(c -> {
            if (playerControl.getRightWeapon().getData().getType() == WeaponType.BOW) {
                if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
                    if (vector.getX() >= 0) {
                        animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT);
                    } else {
                        animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT);
                    }
                } else {
                    if (vector.getY() >= 0) {
                        animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN);
                    } else {
                        animation.setAnimationChannel(CharacterAnimation.SHOOT_UP);
                    }
                }
            }
        });

        getMasterTimer().runOnceAfter(() -> {
            if (!attacker.isActive() || !target.isActive())
                return;

            Entities.builder()
                    .type(EntityType.PROJECTILE)
                    .at(attacker.getBoundingBoxComponent().getCenterWorld())
                    .viewFromTextureWithBBox("projectiles/arrow2.png")
                    .with(new ProjectileControl(target.getBoundingBoxComponent().getCenterWorld().subtract(attacker.getBoundingBoxComponent().getCenterWorld()), 5))
                    .with(new OffscreenCleanControl())
                    .with(new OwnerComponent(attacker))
                    .with(new CollidableComponent(true))
                    .buildAndAttach(getGameWorld());
        }, Duration.seconds(0.8));
    }

    private void dropItem(Entity item, Point2D position) {
        DescriptionComponent desc = item.getComponentUnsafe(DescriptionComponent.class);

        EntityView view = new EntityView();
        view.addNode(getAssetLoader().loadTexture(desc.getTextureName()));
        view.setTranslateX(position.getX());
        view.setTranslateY(position.getY());
        view.setCursor(Cursor.CLOSED_HAND);

        view.setOnMouseClicked(event -> {
            getGameScene().removeGameView(view);
            player.getInventory().addItem(item);
        });

        getGameScene().addGameView(view);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.3), view);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setByX(new Random().nextInt(20) - 10);
        tt.setByY(10 + new Random().nextInt(10));
        tt.play();
    }

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

    private enum CharacterAnimation implements AnimationChannel {
        WALK_RIGHT(11, 9),
        WALK_LEFT(9, 9),
        WALK_UP(8, 9),
        WALK_DOWN(10, 9),

        SLASH_UP(12, 6),
        SLASH_LEFT(13, 6),
        SLASH_DOWN(14, 6),
        SLASH_RIGHT(15, 6),

        SHOOT_UP(16, 13),
        SHOOT_LEFT(17, 13),
        SHOOT_DOWN(18, 13),
        SHOOT_RIGHT(19, 13),

        DEATH(20, 6);

        int row;
        int cycle;

        CharacterAnimation(int row, int cycle) {
            this.row = row;
            this.cycle = cycle;
        }

        @Override
        public Rectangle2D area() {
            return new Rectangle2D(0, TILE_SIZE*row, TILE_SIZE*cycle, TILE_SIZE);
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

    private void initPlayer() {
        player = new PlayerEntity("Developer", "chars/players/player_full.png");
        playerControl = player.getControl();

        player.getTypeComponent().setValue(EntityType.PLAYER);
        player.addComponent(new CollidableComponent(true));

        player.getPositionComponent().setValue(TILE_SIZE * 4, TILE_SIZE * 4);

        playerAnimation = getAssetLoader().loadTexture(player.getDescription().getTextureName())
                .toDynamicAnimatedTexture(CharacterAnimation.WALK_RIGHT, CharacterAnimation.values());

        player.getMainViewComponent().setView(playerAnimation, true);
        player.getData().setAnimation(playerAnimation);

        // TODO: TEST DATA BEGIN
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.MIGHTY_SWING()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.WARRIOR_HEART()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ARMOR_MASTERY()));

        player.getInventory().addItem(new WeaponEntity(Data.Weapon.INSTANCE.GUT_RIPPER()));
        player.getInventory().addItem(new WeaponEntity(Data.Weapon.INSTANCE.DRAGON_CLAW()));
        player.getInventory().addItem(new ArmorEntity(Data.Armor.INSTANCE.CHAINMAIL()));

        // TEST DATA END

        getGameWorld().addEntity(player);

        addCharacterSubView(player);
    }

    private void initEnemies() {
        CharacterEntity enemy = new CharacterEntity(Data.Character.INSTANCE.SKELETON_ARCHER());

        enemy.getTypeComponent().setValue(EntityType.CHARACTER);
        enemy.addComponent(new CollidableComponent(true));

        Random random = new Random();

        spawnEntity(random.nextInt(15), random.nextInt(10), enemy);

        enemy.getMainViewComponent().getView().setOnMouseClicked(e -> {
            selected.set(enemy);
        });
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

    private void addCharacterSubView(CharacterEntity entity) {
        ProgressBar barHP = makeHPBar();
        ProgressBar barSP = makeSkillBar();

        barHP.setTranslateX(0);
        barHP.setTranslateY(80);
        barHP.setWidth(TILE_SIZE);
        barHP.setHeight(10);
        barHP.setLabelVisible(false);

        barSP.setTranslateX(0);
        barSP.setTranslateY(90);
        barSP.setWidth(TILE_SIZE);
        barSP.setHeight(10);
        barSP.setLabelVisible(false);

        barHP.maxValueProperty().bind(entity.getHp().maxValueProperty());
        barHP.currentValueProperty().bind(entity.getHp().valueProperty());

        barSP.maxValueProperty().bind(entity.getSp().maxValueProperty());
        barSP.currentValueProperty().bind(entity.getSp().valueProperty());

        Text text = new Text();
        text.setFont(Font.font(14));
        text.setFill(Color.WHITE);
        text.textProperty().bind(entity.getDescription().nameProperty().concat(" Lv. ").concat(entity.getBaseLevel()));
        text.setTranslateX(TILE_SIZE / 2 - text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(75);

        Group vbox = new Group(barHP, barSP, text);

        EntityView subView = new EntityView();
        subView.addNode(vbox);

        subView.translateXProperty().bind(entity.getPositionComponent().xProperty());
        subView.translateYProperty().bind(entity.getPositionComponent().yProperty());

        getGameScene().addGameView(subView);

        entity.activeProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                getGameScene().removeGameView(subView);
            }
        });
    }

    private void spawnEntity(int x, int y, GameEntity entity) {
        entity.getPositionComponent().setValue(x * TILE_SIZE, y * TILE_SIZE);

        DynamicAnimatedTexture texture = getAssetLoader().loadTexture(entity.getComponentUnsafe(DescriptionComponent.class).getTextureName())
                .toDynamicAnimatedTexture(CharacterAnimation.WALK_RIGHT, CharacterAnimation.values());

        entity.getMainViewComponent().setView(texture, true);
        entity.getComponentUnsafe(CharacterDataComponent.class).setAnimation(texture);

        getGameWorld().addEntity(entity);

        addCharacterSubView((CharacterEntity) entity);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
