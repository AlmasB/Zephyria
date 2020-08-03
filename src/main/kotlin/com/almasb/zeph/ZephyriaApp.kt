package com.almasb.zeph

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.scene.LoadingScene
import com.almasb.fxgl.app.scene.SceneFactory
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.FXGL.Companion.getSceneService
import com.almasb.zeph.Config.MAP_HEIGHT
import com.almasb.zeph.Config.MAP_WIDTH
import com.almasb.zeph.Config.TILE_SIZE
import com.almasb.zeph.EntityType.MONSTER
import com.almasb.zeph.EntityType.SKILL_PROJECTILE
import com.almasb.zeph.Gameplay.currentMap
import com.almasb.zeph.Gameplay.gotoMap
import com.almasb.zeph.Gameplay.player
import com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_AREA
import com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_CHAR
import com.almasb.zeph.Vars.SELECTED_SKILL_INDEX
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.events.EventHandlers
import com.almasb.zeph.skill.SkillTargetType
import com.almasb.zeph.skill.SkillType
import com.almasb.zeph.ui.BasicInfoView
import com.almasb.zeph.ui.HotbarView
import com.almasb.zeph.ui.InventoryView
import com.almasb.zeph.ui.ZephLoadingScene
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import kotlin.collections.set

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephyriaApp : GameApplication() {

    private var devScene: DevScene? = null

    override fun initSettings(settings: GameSettings) {
        settings.width = 1700
        settings.setHeightFromRatio(16 / 9.0)
        settings.title = "Zephyria RPG"
        settings.version = "0.1 Pre-alpha"
        //settings.setIntroEnabled(true);
        //settings.setManualResizeEnabled(true);
        //settings.setPreserveResizeRatio(true);
        //settings.setProfilingEnabled(true);
        settings.sceneFactory = object : SceneFactory() {
            override fun newLoadingScene(): LoadingScene {
                return ZephLoadingScene()
            }
        }
        settings.applicationMode = ApplicationMode.DEVELOPER
    }

    override fun onPreInit() {
        EventHandlers.initialize()
        
        if (isReleaseMode()) {
            loopBGM("BGM_Foggy_Woods.mp3")
        }
    }

    override fun initInput() {
        for (i in 1..9) {
            val key = KeyCode.getKeyCode(i.toString() + "")
            val index = i - 1

            onKeyDown(key, "Hotbar Skill $i") {
                onHotbarSkill(index)
            }
        }

        onKeyDown(KeyCode.C) {
            getGameScene().uiNodes
                    .filterIsInstance(BasicInfoView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(KeyCode.I) {
            getGameScene().uiNodes
                    .filterIsInstance(InventoryView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(KeyCode.S) {
            getGameScene().uiNodes
                    .filterIsInstance(HotbarView::class.java)
                    .forEach { it.minBtn.onClick() }
        }
        
        if (!isReleaseMode()) {
            onKeyDown(KeyCode.ENTER) {
                getSceneService().pushSubScene(devScene!!)
            }

            onKeyDown(KeyCode.F) {
                spawn("animated_flame", getInput().mouseXWorld, getInput().mouseYWorld)
            }
        }
    }

    override fun initGameVars(vars: MutableMap<String?, Any?>) {
        vars[IS_SELECTING_SKILL_TARGET_AREA] = false
        vars[IS_SELECTING_SKILL_TARGET_CHAR] = false
        vars[SELECTED_SKILL_INDEX] = -1
    }

    private fun onHotbarSkill(index: Int) {
        val pc = player.characterComponent
        if (index < pc.skills.size) {
            val skill = pc.skills[index]
            if (skill.level == 0) {
                // skill not learned yet
                return
            }

            if (skill.isOnCooldown) {
                return
            }

            if (skill.manaCost.value > pc.sp.value) {
                // no mana
                return
            }

            if (skill.data.type === SkillType.PASSIVE) {
                // skill is passive and is always on
                return
            }

            if (skill.data.targetTypes.contains(SkillTargetType.SELF)) {

                // use skill immediately since player is the target
                val result = pc.useSelfSkill(index)
            } else if (skill.data.targetTypes.contains(SkillTargetType.AREA)) {

                // let player select the area
                set(IS_SELECTING_SKILL_TARGET_AREA, true)
                set(SELECTED_SKILL_INDEX, index)
            } else {

                // let player select the target character
                set(IS_SELECTING_SKILL_TARGET_CHAR, true)
                set(SELECTED_SKILL_INDEX, index)
            }
        }
    }

    override fun initGame() {
        devScene = DevScene()

        // TODO: allow test map loading for quick prototype testing
        getGameWorld().addEntityFactory(ZephFactory())

        val player = spawn("player") as CharacterEntity

        spawn("cellSelection")

        getGameScene().viewport.setBounds(0, 0, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE)
        getGameScene().viewport.bindToEntity(player, getAppWidth() / 2.toDouble(), getAppHeight() / 2.toDouble())
        getGameScene().viewport.setZoom(1.5)

        gotoMap("test_map.tmx", 2, 6)
    }

    override fun initPhysics() {
        onCollisionBegin(SKILL_PROJECTILE, MONSTER) { proj, target ->

            if (proj.getObject<Any>("target") !== target) {
                return@onCollisionBegin
            }

            proj.removeFromWorld()
            player.characterComponent.useTargetSkill(geti(SELECTED_SKILL_INDEX), (target as CharacterEntity))
        }
    }

    override fun initUI() {
        getGameScene().setUIMouseTransparent(false)
        getGameScene().setCursor(image("ui/cursors/main.png"), Point2D(52.0, 10.0))

        getGameScene().addUINodes(
                BasicInfoView(player),
                InventoryView(player),
                HotbarView(player)
        )
    }

    override fun onUpdate(tpf: Double) {
        currentMap.onUpdate(tpf)
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(ZephyriaApp::class.java, args)
}
