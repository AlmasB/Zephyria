package com.almasb.zeph;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.events.EventHandlers;
import com.almasb.zeph.skill.Skill;
import com.almasb.zeph.skill.SkillTargetType;
import com.almasb.zeph.skill.SkillType;
import com.almasb.zeph.skill.SkillUseResult;
import com.almasb.zeph.ui.BasicInfoView;
import com.almasb.zeph.ui.HotbarView;
import com.almasb.zeph.ui.InventoryView;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.zeph.Config.*;
import static com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_CHAR;
import static com.almasb.zeph.Vars.SELECTED_SKILL_INDEX;

public class ZephyriaApp extends GameApplication {

    private boolean selectingSkillTargetArea = false;
    private DevScene devScene;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1700);
        settings.setHeightFromRatio(16/9.0);
        settings.setTitle("Zephyria RPG");
        settings.setVersion("0.1");
        //settings.setManualResizeEnabled(true);
        //settings.setPreserveResizeRatio(true);
        settings.setProfilingEnabled(true);
        settings.setApplicationMode(ApplicationMode.DEBUG);
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

        if (!isReleaseMode()) {
            onKeyDown(KeyCode.ENTER, () -> getSceneService().pushSubScene(devScene));
        }
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        // TODO: DSL String operator = ...

        vars.put(IS_SELECTING_SKILL_TARGET_CHAR, false);
        vars.put(SELECTED_SKILL_INDEX, -1);
    }

    private void onHotbarSkill(int index) {
        var pc = Gameplay.INSTANCE.getPlayer().getCharacterComponent();

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

                set(SELECTED_SKILL_INDEX, index);
            } else {

                // let player select the target character
                set(IS_SELECTING_SKILL_TARGET_CHAR, true);
                set(SELECTED_SKILL_INDEX, index);
            }
        }
    }

    @Override
    protected void initGame() {
        devScene = new DevScene();

        getGameWorld().addEntityFactory(new ZephFactory());

        // TODO: check collision with teleport ...
        var player = (CharacterEntity) spawn("player");

        spawn("cellSelection");

        // TODO: bounds should consider zoom
        // maxX and width should be recomputed because of zoom
        getGameScene().getViewport().setBounds(0, 0, (int)(MAP_WIDTH * TILE_SIZE * 1.13 - 64), (int)(MAP_HEIGHT * TILE_SIZE * 1.07 - 32));
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        getGameScene().getViewport().setZoom(1.5);

        Gameplay.INSTANCE.gotoMap("test_map.tmx", 2, 6);
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
//            }
//        });

        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SKILL_PROJECTILE, EntityType.MONSTER) {
            @Override
            protected void onCollisionBegin(Entity proj, Entity target) {
                if (proj.getObject("target") != target) {
                    return;
                }

                proj.removeFromWorld();

                Gameplay.INSTANCE.getPlayer().getCharacterComponent().useTargetSkill(geti(SELECTED_SKILL_INDEX), (CharacterEntity) target);
            }
        });
    }

    @Override
    protected void initUI() {
        getGameScene().setUIMouseTransparent(false);
        getGameScene().setCursor(image("ui/cursors/main.png"), new Point2D(52, 10));

        var player = Gameplay.INSTANCE.getPlayer();

        getGameScene().addUINodes(
                new BasicInfoView(player),
                new InventoryView(player),
                new HotbarView(player)
        );
    }

    @Override
    protected void onUpdate(double tpf) {
        Gameplay.INSTANCE.getCurrentMap().onUpdate(tpf);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
