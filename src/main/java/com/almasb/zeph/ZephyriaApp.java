package com.almasb.zeph;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.Layer;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarGridView;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.ui.FontType;
import com.almasb.zeph.character.CharacterData;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.combat.DamageResult;
import com.almasb.zeph.combat.DamageType;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.data.Data;
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent;
import com.almasb.zeph.events.EventHandlers;
import com.almasb.zeph.item.ItemData;
import com.almasb.zeph.skill.Skill;
import com.almasb.zeph.skill.SkillTargetType;
import com.almasb.zeph.skill.SkillType;
import com.almasb.zeph.skill.SkillUseResult;
import com.almasb.zeph.ui.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import kotlin.Pair;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.zeph.Config.*;

public class ZephyriaApp extends GameApplication {

    private AStarGrid grid;

    private CharacterEntity player;

    private ObjectProperty<Entity> selected = new SimpleObjectProperty<>();

    private boolean selectingSkillTargetArea = false;
    private boolean selectingSkillTargetChar = false;
    private int selectedSkillIndex = -1;

    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);

    public AStarGrid getGrid() {
        return grid;
    }

    public CharacterEntity getPlayer() {
        return player;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1700);
        settings.setHeightFromRatio(16/9.0);
        settings.setTitle("Zephyria RPG");
        settings.setVersion("0.1");
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void onPreInit() {
        EventHandlers.INSTANCE.initialize();

        if (isReleaseMode()) {
            loopBGM("BGM_Foggy_Woods.mp3");
        }
    }

    @Override
    protected void initInput() {
        for (int i = 1; i <= 9; i++) {
            KeyCode key = KeyCode.getKeyCode(i + "");

            final int index = i - 1;

            onKeyDown(key, "Hotbar Skill " + i, () -> onHotbarSkill(index));
        }

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
        var pc = player.getCharacterComponent();

        if (index < pc.getSkills().size()) {
            Skill skill = pc.getSkills().get(index);

            if (skill.getData().getType() == SkillType.PASSIVE) {
                // skill is passive and is always on
                return;
            }

            if (skill.getData().getTargetTypes().contains(SkillTargetType.SELF)) {

                // use skill immediately since player is the target
                SkillUseResult result = pc.useSelfSkill(index);
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
    }

    private void useTargetSkill(Entity target) {
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ZephFactory());

        player = ZephFactoryKt.newPlayer();

        gotoMap("test_map.tmx", 2, 6);

        spawn("cellSelection");

        getGameScene().getViewport().setBounds(0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        getGameScene().getViewport().setZoom(1.5);
        
        //showGrid();

        //selectedEffect.setInput(new Glow(0.8));
        //initEnemies();

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
//            }
//
//            playerActionControl.getSelected().set(newEntity);
//        });
    }

    private void showGrid() {
        var gridView = new AStarGridView(grid, TILE_SIZE, TILE_SIZE);
        gridView.setMouseTransparent(true);
        getGameScene().addUINode(gridView);
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

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SKILL_PROJECTILE, EntityType.MONSTER) {
            @Override
            protected void onCollisionBegin(Entity proj, Entity target) {
                if (proj.getObject("target") != target) {
                    return;
                }

                proj.removeFromWorld();

                CharacterEntity character = (CharacterEntity) target;

                player.getCharacterComponent().useTargetSkill(selectedSkillIndex, character);

                // TODO: show damage

//                SkillUseResult result = playerComponent.useTargetSkill(skill, character);
//                showDamage(result.getDamage(), character.getPositionComponent().getValue());

                if (character.getHp().isZero()) {
                    playerKilledChar(character);
                }
            }
        });
    }

    /**
     * Called when player kills given character.
     *
     * @param character killed char
     */
    public void playerKilledChar(CharacterEntity character) {
        character.getCharacterComponent().kill();

        int levelDiff = character.getBaseLevel().get() - player.getBaseLevel().get();

        int money = (levelDiff > 0 ? levelDiff * 5 : 0) + FXGLMath.random(0, character.getBaseLevel().get());

        showMoneyEarned(money, player.getPosition());

        player.getPlayerComponent().rewardMoney(money);
        player.getPlayerComponent().rewardXP(character.getData().getRewardXP());

        List<Pair<Integer, Integer>> drops = character.getData().getDropItems();
        drops.forEach(p -> {
            int itemID = p.getFirst();
            int chance = p.getSecond();

            if (GameMath.INSTANCE.checkChance(chance)) {
                // TODO: get item data from itemID
                //spawnItem(character.getPosition());
            }
        });

        // TODO:
//        character.getViewComponent().getView().setOnMouseClicked(null);
//        selected.set(null);
    }

    private Text debug = new Text();

    @Override
    protected void initUI() {
        getGameScene().setBackgroundColor(Color.GRAY);
        getGameScene().setUIMouseTransparent(false);
        getGameScene().setCursor(image("ui/cursors/main.png"), new Point2D(52, 10));

        debug.setTranslateX(100);
        debug.setTranslateY(300);
        debug.setFill(Color.WHITE);

        getGameScene().addUINodes(
                new BasicInfoView(player),
                //new CharInfoView(player),
                new InventoryView(player, getAppWidth(), getAppHeight()),
                new EquipmentView(player, getAppWidth(), getAppHeight())
                //new HotbarView(player)
        );
    }

    private void spawnItem(int x, int y, ItemData itemData) {
        SpawnData data = new SpawnData(x, y);
        data.put("itemData", itemData);

        Entity itemEntity = spawn("item", data);

        itemEntity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            player.getActionComponent().orderPickUp(itemEntity);
        });

        animationBuilder()
                .duration(Duration.seconds(0.3))
                .interpolator(Interpolators.SMOOTH.EASE_IN())
                .translate(itemEntity)
                .from(itemEntity.getPosition())
                .to(itemEntity.getPosition().add(FXGLMath.randomPoint2D().multiply(20)))
                .buildAndPlay();
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
        var text = getUIFactoryService().newText(
                damage.getValue() + (damage.isCritical() ? "!" : ""),
                damage.isCritical() ? Color.RED : Color.WHITE,
                FontType.GAME,
                damage.isCritical() ? 28 : 26
        );
        text.setStroke(damage.isCritical() ? Color.RED : Color.WHITE);

        var view = new GameView(text, 100);

        getGameScene().addGameView(view);

        animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .translate(text)
                .from(new Point2D(position.getX() + random(-25, 0), position.getY()))
                .to(position.add(random(-25, 0), random(-40, -25)))
                .buildAndPlay();

        animationBuilder()
                .onFinished(() -> getGameScene().removeGameView(view))
                .duration(Duration.seconds(2.15))
                .fadeOut(text)
                .buildAndPlay();
    }

    private void spawnCharacter(int x, int y, CharacterData charData) {
        System.out.println("Spawning " + charData + " at " + x + "," + y);

        SpawnData data = new SpawnData(x, y);
        data.put("charData", charData);

        CharacterEntity e = (CharacterEntity) spawn("char", data);

        e.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            // TODO: check for skill range

            if (selectingSkillTargetChar) {
                selectingSkillTargetChar = false;

                player.getActionComponent().orderSkillCast(selectedSkillIndex, e);
            } else {
                player.getActionComponent().orderAttack(e);
            }
        });
    }

    public void gotoMap(String mapName, int toCellX, int toCellY) {
        var level = getAssetLoader().loadLevel("tmx/" + mapName, new TMXLevelLoader());

        getGameWorld().setLevel(level);

        grid = AStarGrid.fromWorld(getGameWorld(), 150, 150, TILE_SIZE, TILE_SIZE, (type) -> {
            if (type.equals(EntityType.NAV) || type.equals(EntityType.PORTAL))
                return CellState.WALKABLE;

            return CellState.NOT_WALKABLE;
        });

        getGameWorld().getEntitiesFiltered(e -> e.isType("TiledMapLayer"))
                .stream()
                .filter(e -> e.<Layer>getObject("layer").getName().equals("Decor_above_player"))
                .forEach(e -> {
                    e.getViewComponent().getParent().setMouseTransparent(true);
                    e.setZ(Z_INDEX_DECOR_ABOVE_PLAYER);
                });

        spawnMobs(level);

        player.removeComponent(NewAStarMoveComponent.class);
        player.addComponent(new NewAStarMoveComponent(grid));

        player.getActionComponent().orderIdle();
        player.setPositionToCell(toCellX, toCellY);
    }

    private void spawnMobs(Level level) {
        level.getProperties()
                .keys()
                .stream()
                // a char id
                .filter(key -> key.startsWith("2"))
                .map(key -> Integer.parseInt(key))
                .forEach(id -> {
                    var numMobs = level.getProperties().getInt(id + "");

                    for (int i = 0; i < numMobs; i++) {
                        grid.getRandomCell(AStarCell::isWalkable).ifPresent(cell -> {
                            spawnCharacter(cell.getX() * TILE_SIZE, cell.getY() * TILE_SIZE, Data.INSTANCE.getCharacterData(id));
                        });
                    }
                });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
