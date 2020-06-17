package com.almasb.zeph;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.zeph.character.CharacterData;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.character.PlayerEntity;
import com.almasb.zeph.character.components.PlayerComponent;
import com.almasb.zeph.combat.DamageResult;
import com.almasb.zeph.combat.DamageType;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.data.Data;
import com.almasb.zeph.item.UsableItemData;
import com.almasb.zeph.item.Weapon;
import com.almasb.zeph.item.WeaponData;
import com.almasb.zeph.skill.SkillComponent;
import com.almasb.zeph.skill.SkillUseResult;
import com.almasb.zeph.ui.BasicInfoView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import kotlin.Pair;

import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ZephyriaApp extends GameApplication {

    private static final int TILE_SIZE = Config.tileSize;
    private static final int MAP_WIDTH = Config.INSTANCE.getMapWidth();
    private static final int MAP_HEIGHT = Config.INSTANCE.getMapHeight();

    private AStarGrid grid;

    private PlayerEntity player;
    private PlayerComponent playerComponent;

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
        settings.setWidth(1280);
        settings.setHeight(768);
        settings.setTitle("Zephyria RPG");
        settings.setVersion("0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        for (int i = 1; i <= 9; i++) {
            KeyCode key = KeyCode.getKeyCode(i + "");

            final int index = i - 1;

            onKeyDown(key, "Hotbar Skill " + i, () -> onHotbarSkill(index));
        }

        onKeyDown(KeyCode.K, "Spawn Dev", () -> {
            spawnCharacter(800, 50, Data.Character.INSTANCE.SKELETON_WARRIOR());
            spawnCharacter(800, 100, Data.Character.INSTANCE.SKELETON_MAGE());
            spawnSkeletonArcher(800, 150, Element.AIR);
            spawnSkeletonArcher(800, 200, Element.WATER);
            spawnSkeletonArcher(800, 250, Element.EARTH);

            spawnItem(4001, new Point2D(800, 400));
            spawnItem(4001, new Point2D(800, 500));

            spawnItem(new Point2D(800, 600), Data.UsableItem.HEALING_POTION());
        });

        onKeyDown(KeyCode.J, "Kill Dev", () -> {
            getGameWorld().getEntitiesInRange(new Rectangle2D(getInput().getMouseXWorld() - 20, getInput().getMouseYWorld() - 20, 40, 40))
                    .stream()
                    .filter(e -> e instanceof CharacterEntity)
                    .map(e -> (CharacterEntity) e)
                    .forEach(this::playerKilledChar);
        });

        onKeyDown(KeyCode.O, "Show Damage", () -> {
            DamageResult dmg = new DamageResult(DamageType.PHYSICAL, Element.NEUTRAL, random(1, 15), false);

            showDamage(dmg, getInput().getMousePositionWorld());
        });
    }

    private void onHotbarSkill(int index) {
//        if (index < playerComponent.getSkills().size()) {
//            SkillComponent skill = playerComponent.getSkills().get(index);
//
//            if (skill.getData().getType() == SkillType.PASSIVE) {
//                // skill is passive and is always on
//                return;
//            }
//
//            if (skill.getData().getTargetTypes().contains(SkillTargetType.SELF)) {
//
//                // use skill immediately since player is the target
//                SkillUseResult result = playerComponent.useSelfSkill(index);
//            } else if (skill.getData().getTargetTypes().contains(SkillTargetType.AREA)) {
//
//                // let player select the area
//                selectingSkillTargetArea = true;
//                selectedSkillIndex = index;
//            } else {
//
//                // let player select the target character
//                selectingSkillTargetChar = true;
//                selectedSkillIndex = index;
//            }
//        }
    }

    private void useAreaSkill() {
        // TODO: we should fire projectile based on skill data component
        SkillUseResult result = playerComponent.useAreaSkill(selectedSkillIndex, getInput().getMousePositionWorld());
    }

    // TODO: generalize to use skill or attack
    private void useTargetSkill(Entity target) {
        SkillComponent skill = playerComponent.getSkills().get(selectedSkillIndex);

//        if (skill.isOnCooldown() || skill.getManaCost().intValue() > playerComponent.getSp().getValue())
//            return;

        Point2D vector = target.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld());

//        AnimatedTexture animation = player.getData().getAnimation();
//
//        if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
//            if (vector.getX() >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.CAST_RIGHT);
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.CAST_LEFT);
//            }
//        } else {
//            if (vector.getY() >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.CAST_DOWN);
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.CAST_UP);
//            }
//        }
//
//        getMasterTimer().runOnceAfter(() -> {
//            if (!player.isActive() || !target.isActive())
//                return;
//
//            // we are using a skill
//
//            if (skill.getData().getHasProjectile()) {
//                Entities.builder()
//                        .type(EntityType.SKILL_PROJECTILE)
//                        .at(player.getBoundingBoxComponent().getCenterWorld())
//                        .viewFromTextureWithBBox(skill.getData().getTextureName())
//                        .with(new ProjectileControl(target.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld()), 6))
//                        .with(new OffscreenCleanComponent())
//                        .with(new OwnerComponent(skill))
//                        .with(new CollidableComponent(true))
//                        .buildAndAttach(getGameWorld());
//            } else {
//                if (player.isInWeaponRange(target)) {
//
//                    SkillUseResult result = playerComponent.useTargetSkill(skill, target);
//                    showDamage(result.getDamage(), target.getPositionComponent().getValue());
//
//                    if (target.getHp().getValue() <= 0) {
//                        playerKilledChar(target);
//                    }
//
//                } else {
//                    playerActionControl.moveTo(target.getTileX(), target.getTileY());
//                }
//            }
//
//        }, Duration.seconds(0.8));
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ZephFactory());

        grid = new AStarGrid(MAP_WIDTH, MAP_HEIGHT);

        initBackground();

        selectedEffect.setInput(new Glow(0.8));

        initPlayer();
        initEnemies();

        //showGrid();
        getGameScene().getViewport().setBounds(0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);

//        selected.addListener((observable, oldValue, newEntity) -> {
//            if (oldValue != null) {
//                oldValue.getComponent(ViewComponent.class).ifPresent(c -> {
//                    c.getView().setEffect(null);
//                });
//            }
//
//            if (newEntity != null) {
//                newEntity.getComponent(ViewComponent.class).ifPresent(c -> {
//                    c.getView().setEffect(selectedEffect);
//                });
//
//                // TODO: at some point we need to check if it's ally or enemy based on skill target type
//                if (selectingSkillTargetChar) {
//                    if (newEntity instanceof CharacterEntity) {
//                        useTargetSkill((CharacterEntity) newEntity);
//                    }
//
//                    selectingSkillTargetChar = false;
//                    selectedSkillIndex = -1;
//                    selected.set(null);
//                }
//            }
//
//            playerActionControl.getSelected().set(newEntity);
//        });
    }

    private void initBackground() {
        Region region = new Region();
        region.setPrefSize(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);

        BackgroundImage bgImg = new BackgroundImage(image("tile.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        region.setBackground(new Background(bgImg));

        Entity bg = entityBuilder()
                .zIndex(-1000)
                .view(region)
                .onClick(bgEntity -> {
                    if (selectingSkillTargetArea) {
                        useAreaSkill();

                        selectingSkillTargetArea = false;
                        selectedSkillIndex = -1;
                        return;
                    }

                    selected.set(null);

                    int targetX = (int) (getInput().getMouseXWorld() / TILE_SIZE);
                    int targetY = (int) (getInput().getMouseYWorld() / TILE_SIZE);

                    player.getActionComponent().orderMove(targetX, targetY);
                })
                .buildAndAttach();

        spawnTree(0, 0);
        spawnTree(0, MAP_HEIGHT - 1);
        spawnTree(MAP_WIDTH - 1, 0);
        spawnTree(MAP_WIDTH - 1, MAP_HEIGHT - 1);
    }

    private void spawnTree(int x, int y) {
        Entity tree = entityBuilder()
                .at(x * TILE_SIZE, y * TILE_SIZE - 85 + 64)
                .view("tree2.png")
                .buildAndAttach();

        grid.get(x, y).setState(CellState.NOT_WALKABLE);
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

//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.CHARACTER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
//                    return;
//
//                proj.removeFromWorld();
//
//                CharacterEntity character = (CharacterEntity) target;
//
//                DamageResult damage = player.getPlayerControl().attack(character);
//                showDamage(damage, character.getPositionComponent().getValue());
//
//                if (character.getHp().getValue() <= 0) {
//                    playerKilledChar(character);
//                }
//            }
//        });
//
//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.PLAYER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
//                    return;
//
//                proj.removeFromWorld();
//
//                CharacterEntity attacker = (CharacterEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();
//                CharacterEntity character = (CharacterEntity) target;
//
//                DamageResult damage = attacker.getCharConrol().attack(character);
//                showDamage(damage, character.getPositionComponent().getValue());
//
////                if (character.getHp().getValue() <= 0) {
////                    playerKilledChar(character);
////                }
//            }
//        });
//
//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SKILL_PROJECTILE, EntityType.CHARACTER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                SkillEntity skill = (SkillEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();
//
//                proj.removeFromWorld();
//
//                CharacterEntity character = (CharacterEntity) target;
//
//                SkillUseResult result = playerComponent.useTargetSkill(skill, character);
//                showDamage(result.getDamage(), character.getPositionComponent().getValue());
//
//                if (character.getHp().getValue() <= 0) {
//                    playerKilledChar(character);
//                }
//            }
//        });
    }

    /**
     * Called when player kills given character.
     *
     * @param character killed char
     */
    public void playerKilledChar(CharacterEntity character) {
        character.kill();

        int levelDiff = character.getBaseLevel().get() - player.getBaseLevel().get();

        int money = (levelDiff > 0 ? levelDiff * 5 : 0) + FXGLMath.random(0, character.getBaseLevel().get());

        showMoneyEarned(money, player.getPosition());

        playerComponent.rewardMoney(money);
        playerComponent.rewardXP(character.getData().getRewardXP());

        List<Pair<Integer, Integer>> drops = character.getData().getDropItems();
        drops.forEach(p -> {
            int itemID = p.getFirst();
            int chance = p.getSecond();

            if (GameMath.INSTANCE.checkChance(chance)) {
                spawnItem(itemID, character.getPosition());
            }
        });

//        character.getViewComponent().getView().setOnMouseClicked(null);
//        selected.set(null);
    }

    private Text debug = new Text();

    @Override
    protected void initUI() {
        getGameScene().setUIMouseTransparent(false);
        getGameScene().setCursor(image("ui/cursors/main.png"), new Point2D(52, 10));

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        getGameScene().addUINodes(
//                new HotbarView(player),
                new BasicInfoView(player)
//                new CharInfoView(player),
//                new InventoryView(player, getAppWidth(), getAppHeight()),
//                new EquipmentView(player, getAppWidth(), getAppHeight())
        );
    }

    private void spawnItem(Point2D position, UsableItemData itemData) {
        SpawnData data = new SpawnData(position);
        data.put("itemData", itemData);

        Entity itemEntity = spawn("item", data);
    }

    private void spawnItem(int itemID, Point2D position) {

        SpawnData data = new SpawnData(position);

        if (Data.INSTANCE.isWeapon(itemID)) {
            WeaponData weaponData = Data.INSTANCE.getWeapon(itemID);
            data.put("weaponData", weaponData);
        } else if (Data.INSTANCE.isArmor(itemID)) {

            // TODO:
        }

        Entity itemEntity = spawn("item", data);
        itemEntity.setProperty("weapon", new Weapon(Data.INSTANCE.getWeapon(itemID)));
//        itemEntity.getView().setOnMouseClicked(e -> {
//            player.getActionComponent().orderPickUp(itemEntity);
//        });


//        view.setTranslateX(position.getX());
//        view.setTranslateY(position.getY());
//        view.setCursor(Cursor.CLOSED_HAND);
//
//        view.setOnMouseClicked(event -> {
//            getGameScene().removeGameView(view);
//            player.getInventory().addItem(item);
//        });
//
//        getGameScene().addGameView(view);
//
//        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.3), view);
//        tt.setInterpolator(Interpolator.EASE_IN);
//        tt.setByX(new Random().nextInt(20) - 10);
//        tt.setByY(10 + new Random().nextInt(10));
//        tt.play();
    }

    public void showMoneyEarned(int money, Point2D position) {
        Text text = getUIFactoryService().newText(money + "G", 18);
        text.setFill(Color.GOLD);
        text.setStroke(Color.GOLD);

        Entity e = entityBuilder()
                .at(position)
                .view(text)
                .with(new ExpireCleanComponent(Duration.seconds(1.2)))
                .buildAndAttach();

        animationBuilder()
                .duration(Duration.seconds(1))
                .translate(e)
                .from(position)
                .to(position.add(0, -30))
                .buildAndPlay();
    }

    public void showDamage(DamageResult damage, Point2D position) {
//        Text text = new Text(damage.getValue() + (damage.getCritical() ? "!" : ""));
//        text.setFill(damage.getCritical() ? Color.RED : Color.WHITE);
//        text.setFont(FXGLAssets.UI_GAME_FONT.newFont(damage.getCritical() ? 24 : 22));
//        text.setStroke(damage.getCritical() ? Color.RED : Color.WHITE);
//
//        EntityView view = new EntityView();
//        view.addNode(text);
//        view.setTranslateX(position.getX() + random(-25, 0));
//        view.setTranslateY(position.getY());
//
//        getGameScene().addGameView(view);
//
//        Animation<?> anim = translate(view, position.add(random(-25, 0), random(-40, -25)), Duration.seconds(1));
//        anim.getAnimatedValue().setInterpolator(Interpolators.EXPONENTIAL.EASE_OUT());
//        anim.startInPlayState();
//
//        Animation<?> anim2 = fadeOut(view, Duration.seconds(1.15));
//        anim2.getAnimatedValue().setInterpolator(Interpolators.EXPONENTIAL.EASE_OUT());
//        anim2.setOnFinished(() -> getGameScene().removeGameView(view, RenderLayer.DEFAULT));
//        anim2.startInPlayState();
    }

    private void initPlayer() {
        player = new PlayerEntity("Developer", "chars/players/player_full.png");
        playerComponent = player.getPlayerComponent();
        player.setType(EntityType.PLAYER);
        player.setPosition(TILE_SIZE * 8, TILE_SIZE * 4);


        //player.setView(new Rectangle(40, 40, Color.BLUE));


        getGameWorld().addEntity(player);




//        player.addComponent(DataManager.INSTANCE.makeCharacterSubView(player));
//        spawnCharacter(player);
//
//        // TODO: do something with circular references
//        player.addControl(new PlayerActionControl());
//        playerActionControl = player.getControlUnsafe(PlayerActionControl.class);
//
//        // TODO: TEST DATA BEGIN
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.MIGHTY_SWING()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.WARRIOR_HEART()));
//        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ARMOR_MASTERY()));
//
////        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
////        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
////        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
////        player.getSkills().add(new SkillEntity(Data.Skill.Warrior.INSTANCE.ROAR()));
////        player.getSkills().add(new SkillEntity(Data.Skill.Mage.INSTANCE.FIREBALL()));
//

        player.getInventory().getItems().add(newDagger(Element.NEUTRAL));
        player.getInventory().getItems().add(newDagger(Element.FIRE));
        player.getInventory().getItems().add(newDagger(Element.EARTH));
        player.getInventory().getItems().add(newDagger(Element.AIR));
        player.getInventory().getItems().add(newDagger(Element.WATER));

//        player.getInventory().addItem(new WeaponComponent(Data.Weapon.INSTANCE.GUT_RIPPER()));
//        player.getInventory().addItem(new WeaponComponent(Data.Weapon.INSTANCE.DRAGON_CLAW()));
//        player.getInventory().addItem(new ArmorComponent(Data.Armor.INSTANCE.CHAINMAIL()));

        // TEST DATA END
    }

    private Weapon newDagger(Element element) {
        Weapon weapon = new Weapon(Data.Weapon.INSTANCE.KNIFE());
        weapon.getElement().set(element);
        return weapon;
    }

    private void initEnemies() {
        Random random = new Random();

        //spawnCharacter(DataManager.INSTANCE.createCharacter(Data.Character.INSTANCE.SKELETON_ARCHER(), random.nextInt(15), random.nextInt(10)));
    }

    private void spawnCharacter(Entity character) {
//        character.addComponent(new CollidableComponent(true));
//
//        AnimatedTexture texture = getAssetLoader()
//                .loadTexture(character.getComponentUnsafe(Description.class).getTextureName().get())
//                .toAnimatedTexture(CharacterAnimation.WALK_RIGHT);
//
//        character.getComponentUnsafe(CharacterDataComponent.class).setAnimation(texture);
//        character.getViewComponent().setView(texture, true);
//
//        if (!character.getTypeComponent().isType(EntityType.PLAYER)) {
//            //character.addControl(new AIControl());
//
//            character.getViewComponent().getView().setOnMouseClicked(e -> {
//                selected.set(character);
//            });
//        }
//
//        EntityView subView = character.getComponentUnsafe(SubViewComponent.class).getValue();
//
//        getGameWorld().addEntity(character);
//        getGameScene().addGameView(subView);
//
//        character.activeProperty().addListener((o, wasActive, isActive) -> {
//            if (!isActive) {
//                getGameScene().removeGameView(subView);
//            }
//        });
    }

    private void spawnSkeletonArcher(int x, int y, Element element) {
        SpawnData data = new SpawnData(x, y);

        data.put("charData", Data.Character.INSTANCE.SKELETON_ARCHER());

        CharacterEntity e = (CharacterEntity) spawn("char", data);
        e.getCharacterComponent().getArmorElement().set(element);

//        e.getView().setOnMouseClicked(event -> {
//            player.getActionComponent().orderAttack(e);
//
////            DamageResult dmg = player.getPlayerComponent().attack(e);
////
////            showDamage(dmg, e.getCenter());
//        });
    }

    private void spawnCharacter(int x, int y, CharacterData charData) {
        SpawnData data = new SpawnData(x, y);

        data.put("charData", charData);

        CharacterEntity e = (CharacterEntity) spawn("char", data);

//        e.getView().setOnMouseClicked(event -> {
//            player.getActionComponent().orderAttack(e);
//
////            DamageResult dmg = player.getPlayerComponent().attack(e);
////
////            showDamage(dmg, e.getCenter());
//        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
