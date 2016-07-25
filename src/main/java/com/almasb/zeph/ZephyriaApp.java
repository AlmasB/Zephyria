package com.almasb.zeph;

import com.almasb.astar.AStarGrid;
import com.almasb.astar.NodeState;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.MainViewComponent;
import com.almasb.fxgl.entity.control.OffscreenCleanControl;
import com.almasb.fxgl.entity.control.ProjectileControl;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.DynamicAnimatedTexture;
import com.almasb.zeph.combat.DamageResult;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.entity.Data;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.EntityType;
import com.almasb.zeph.entity.ai.AIControl;
import com.almasb.zeph.entity.character.CharacterEntity;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.component.CharacterDataComponent;
import com.almasb.zeph.entity.character.component.SubViewComponent;
import com.almasb.zeph.entity.character.control.PlayerActionControl;
import com.almasb.zeph.entity.character.control.PlayerControl;
import com.almasb.zeph.entity.item.ArmorEntity;
import com.almasb.zeph.entity.item.WeaponEntity;
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

import java.util.List;
import java.util.Random;

public class ZephyriaApp extends GameApplication {

    private static final int TILE_SIZE = Config.INSTANCE.getTileSize();
    private static final int MAP_WIDTH = Config.INSTANCE.getMapWidth();
    private static final int MAP_HEIGHT = Config.INSTANCE.getMapHeight();

    private AStarGrid grid;

    private PlayerEntity player;
    private PlayerControl playerControl;
    private PlayerActionControl playerActionControl;

    private ObjectProperty<Entity> selected = new SimpleObjectProperty<>();

    private boolean selectingSkillTargetArea = false;
    private boolean selectingSkillTargetChar = false;
    private int selectedSkillIndex = -1;

    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);

    public AStarGrid getGrid() {
        return grid;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

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

    @Override
    protected void initInput() {
        Input input = getInput();

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

                // let player select the target character
                selectingSkillTargetChar = true;
                selectedSkillIndex = index;
            }
        }
    }

    private void useAreaSkill() {
        // TODO: we should fire projectile based on skill data component
        SkillUseResult result = playerControl.useAreaSkill(selectedSkillIndex, getInput().getMousePositionWorld());
    }

    // TODO: generalize to use skill or attack
    private void useTargetSkill(CharacterEntity target) {
        SkillEntity skill = player.getSkills().get(selectedSkillIndex);

        if (skill.isOnCooldown() || skill.getManaCost().intValue() > player.getSp().getValue())
            return;

        Point2D vector = target.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld());

        DynamicAnimatedTexture animation = player.getData().getAnimation();

        if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
            if (vector.getX() >= 0) {
                animation.setAnimationChannel(CharacterAnimation.CAST_RIGHT);
            } else {
                animation.setAnimationChannel(CharacterAnimation.CAST_LEFT);
            }
        } else {
            if (vector.getY() >= 0) {
                animation.setAnimationChannel(CharacterAnimation.CAST_DOWN);
            } else {
                animation.setAnimationChannel(CharacterAnimation.CAST_UP);
            }
        }

        getMasterTimer().runOnceAfter(() -> {
            if (!player.isActive() || !target.isActive())
                return;

            // we are using a skill

            if (skill.getData().getHasProjectile()) {
                Entities.builder()
                        .type(EntityType.SKILL_PROJECTILE)
                        .at(player.getBoundingBoxComponent().getCenterWorld())
                        .viewFromTextureWithBBox(skill.getData().getTextureName())
                        .with(new ProjectileControl(target.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld()), 6))
                        .with(new OffscreenCleanControl())
                        .with(new OwnerComponent(skill))
                        .with(new CollidableComponent(true))
                        .buildAndAttach(getGameWorld());
            } else {
                if (player.isInWeaponRange(target)) {

                    SkillUseResult result = playerControl.useTargetSkill(skill, target);
                    showDamage(result.getDamage(), target.getPositionComponent().getValue());

                    if (target.getHp().getValue() <= 0) {
                        onKill(target);
                    }

                } else {
                    playerActionControl.moveTo(target.getTileX(), target.getTileY());
                }
            }

        }, Duration.seconds(0.8));
    }

    @Override
    protected void initAssets() {}

    @Override
    protected void initGame() {
        grid = new AStarGrid(MAP_WIDTH, MAP_HEIGHT);

        initBackground();

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
                    selected.set(null);
                }
            }

            playerActionControl.getSelected().set(newEntity);
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

            playerActionControl.moveTo(targetX, targetY);
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

        grid.setNodeState(x, y, NodeState.NOT_WALKABLE);
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

                DamageResult damage = player.getPlayerControl().attack(character);
                showDamage(damage, character.getPositionComponent().getValue());

                if (character.getHp().getValue() <= 0) {
                    onKill(character);
                }
            }
        });

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity proj, Entity target) {
                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
                    return;

                proj.removeFromWorld();

                CharacterEntity attacker = (CharacterEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();
                CharacterEntity character = (CharacterEntity) target;

                DamageResult damage = attacker.getCharConrol().attack(character);
                showDamage(damage, character.getPositionComponent().getValue());

//                if (character.getHp().getValue() <= 0) {
//                    onKill(character);
//                }
            }
        });

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SKILL_PROJECTILE, EntityType.CHARACTER) {
            @Override
            protected void onCollisionBegin(Entity proj, Entity target) {
                SkillEntity skill = (SkillEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();

                proj.removeFromWorld();

                CharacterEntity character = (CharacterEntity) target;

                SkillUseResult result = playerControl.useTargetSkill(skill, character);
                showDamage(result.getDamage(), character.getPositionComponent().getValue());

                if (character.getHp().getValue() <= 0) {
                    onKill(character);
                }
            }
        });
    }

    /**
     * Called when player kills given character.
     *
     * @param character killed char
     */
    private void onKill(CharacterEntity character) {
        character.setControlsEnabled(false);

        // TODO: reward based on level differences
        playerControl.rewardMoney(new Random().nextInt(character.getBaseLevel().get()));
        playerControl.rewardXP(character.getData().getRewardXP());

        List<Pair<Integer, Integer> > drops = character.getData().getDropItems();
        drops.forEach(p -> {
            if (GameMath.INSTANCE.checkChance(p.getSecond())) {
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
    protected void onUpdate(double tpf) {}

    private void dropItem(Entity item, Point2D position) {
        DescriptionComponent desc = item.getComponentUnsafe(DescriptionComponent.class);

        EntityView view = new EntityView();
        view.addNode(getAssetLoader().loadTexture(desc.getTextureName().get()));
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

    private void showDamage(DamageResult damage, Point2D position) {
        Text text = new Text(damage.getValue() + (damage.getCritical() ? "!" : ""));
        text.setFill(damage.getCritical() ? Color.RED : Color.WHITE);
        text.setFont(Font.font(damage.getCritical() ? 22 : 16));

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

    private void initPlayer() {
        player = new PlayerEntity("Developer", "chars/players/player_full.png");
        playerControl = player.getPlayerControl();

        player.getTypeComponent().setValue(EntityType.PLAYER);
        player.getPositionComponent().setValue(TILE_SIZE * 4, TILE_SIZE * 4);

        player.addComponent(EntityManager.INSTANCE.makeCharacterSubView(player));
        spawnCharacter(player);

        // TODO: do something with circular references
        player.addControl(new PlayerActionControl());
        playerActionControl = player.getControlUnsafe(PlayerActionControl.class);

        // TODO: TEST DATA BEGIN
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.MIGHTY_SWING()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.WARRIOR_HEART()));
        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ARMOR_MASTERY()));

//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Mage.INSTANCE.FIREBALL()));

        player.getInventory().addItem(new WeaponEntity(Data.Weapon.INSTANCE.GUT_RIPPER()));
        player.getInventory().addItem(new WeaponEntity(Data.Weapon.INSTANCE.DRAGON_CLAW()));
        player.getInventory().addItem(new ArmorEntity(Data.Armor.INSTANCE.CHAINMAIL()));

        // TEST DATA END
    }

    private void initEnemies() {
        Random random = new Random();

        spawnCharacter(EntityManager.INSTANCE.createCharacter(Data.Character.INSTANCE.SKELETON_ARCHER(), random.nextInt(15), random.nextInt(10)));
    }

    private void spawnCharacter(CharacterEntity character) {
        character.addComponent(new CollidableComponent(true));

        DynamicAnimatedTexture texture = getAssetLoader()
                .loadTexture(character.getComponentUnsafe(DescriptionComponent.class).getTextureName().get())
                .toDynamicAnimatedTexture(CharacterAnimation.WALK_RIGHT, CharacterAnimation.values());

        character.getComponentUnsafe(CharacterDataComponent.class).setAnimation(texture);
        character.getMainViewComponent().setView(texture, true);

        if (!character.getTypeComponent().isType(EntityType.PLAYER)) {
            //character.addControl(new AIControl());

            character.getMainViewComponent().getView().setOnMouseClicked(e -> {
                selected.set(character);
            });
        }

        EntityView subView = character.getComponentUnsafe(SubViewComponent.class).getValue();

        getGameWorld().addEntity(character);
        getGameScene().addGameView(subView);

        character.activeProperty().addListener((o, wasActive, isActive) -> {
            if (!isActive) {
                getGameScene().removeGameView(subView);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
