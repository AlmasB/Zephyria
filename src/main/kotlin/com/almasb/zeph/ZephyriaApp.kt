package com.almasb.zeph

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.scene.LoadingScene
import com.almasb.fxgl.app.scene.SceneFactory
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.FXGL.Companion.getSceneService
import com.almasb.fxgl.dsl.FXGL.Companion.onCollisionCollectible
import com.almasb.fxgl.input.InputSequence
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.logging.LoggerLevel
import com.almasb.fxgl.logging.LoggerOutput
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.Gameplay.currentMap
import com.almasb.zeph.Gameplay.gotoMap
import com.almasb.zeph.Gameplay.player
import com.almasb.zeph.Gameplay.spawnTextBox
import com.almasb.zeph.Gameplay.startDialogue
import com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_AREA
import com.almasb.zeph.Vars.IS_SELECTING_SKILL_TARGET_CHAR
import com.almasb.zeph.Vars.SELECTED_SKILL_INDEX
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.data.Data
import com.almasb.zeph.events.EventHandlers
import com.almasb.zeph.skill.SkillTargetType
import com.almasb.zeph.skill.SkillType
import com.almasb.zeph.ui.*
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode.*
import javafx.scene.paint.Color
import javafx.util.Duration
import java.util.function.Consumer
import kotlin.collections.set

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephyriaApp : GameApplication() {

    private var devScene: DevScene? = null

    override fun initSettings(settings: GameSettings) {
        settings.width = 1800
        settings.setHeightFromRatio(16 / 9.0)
        settings.title = "Zephyria RPG"
        settings.version = "0.1 Pre-alpha"
        settings.cssList += "zephyria.css"
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
            val key = getKeyCode(i.toString() + "")
            val index = i - 1

            onKeyDown(key, "Hotbar Skill $i") {
                onHotbarSkill(index)
            }
        }

        onKeyDown(C) {
            getGameScene().uiNodes
                    .filterIsInstance(BasicInfoView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(I) {
            getGameScene().uiNodes
                    .filterIsInstance(InventoryView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(S) {
            getGameScene().uiNodes
                    .filterIsInstance(HotbarView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(V) {
            getGameScene().uiNodes
                    .filterIsInstance(MessagesView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        getInput().addAction(
                object : UserAction("Codefest2021") {
                    override fun onActionBegin() {
                        if (player.cellX in 90..94 && player.cellY in 125..129) {
                            showMessage("Congratulations on getting this far! The keyword is:\n10,5,11,12,1,8,3,7")
                        }
                    }
                },
                InputSequence(C,O,D,E,F,E,S,T,DIGIT2,DIGIT0,DIGIT2,DIGIT1)
        )

        if (!isReleaseMode()) {
            onKeyDown(ENTER) {
                getSceneService().pushSubScene(devScene!!)
            }

            onKeyDown(F) {
                val quest = com.almasb.zeph.quest.Quest(Data.Quests.TUTORIAL_KILLS)

                println(quest.data.description)
                //spawn("animated_flame", getInput().mouseXWorld, getInput().mouseYWorld)
            }
//
//            onKeyDown(T) {
//                Gameplay.spawn("treasureChest", player.cellX, player.cellY)
//            }
//
//            onKeyDown(L) {
//                fire(OnLevelUpEvent(player))
//            }
//
//            onKeyDown(Y) {
//                val scene = CharSelectSubScene(WARRIOR, SCOUT, MAGE)
//                scene.onSelected = {
//                    println(it)
//                }
//
//                getSceneService().pushSubScene(scene)
//            }
        }
    }

    override fun initGameVars(vars: MutableMap<String?, Any?>) {
        vars[IS_SELECTING_SKILL_TARGET_AREA] = false
        vars[IS_SELECTING_SKILL_TARGET_CHAR] = false
        vars[SELECTED_SKILL_INDEX] = -1

        vars["isCodefestEnabled"] = false
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



    // TODO: dialogue system values (set / get, compare?)

    override fun initGame() {
        devScene = DevScene()

        getGameScene().setBackgroundColor(Color.BLACK)

        getGameWorld().addEntityFactory(ZephFactory())

        val player = spawn("player") as CharacterEntity

        spawn("cellSelection")

        getGameScene().viewport.bindToEntity(player, getAppWidth() / 2.toDouble(), getAppHeight() / 2.toDouble())
        getGameScene().viewport.setZoom(1.5)

        //gotoMap("dev_world.tmx", 8, 6)
        //gotoMap("tutorial.tmx", 8, 6)
        gotoMap("test_map.tmx", 2, 6)

        // Codefest stuff
        if (isReleaseMode()) {
            val c1 = Gameplay.spawnItem(6997, 2, 8)
            val c2 = Gameplay.spawnItem(6998, 2, 10)
            val c3 = Gameplay.spawnItem(6999, 2, 12)

            c1.viewComponent.visibleProperty.bind(getbp("isCodefestEnabled"))
            c2.viewComponent.visibleProperty.bind(getbp("isCodefestEnabled"))
            c3.viewComponent.visibleProperty.bind(getbp("isCodefestEnabled"))

            runOnce({
                getDialogService().showInputBox("Please enter your name") { name ->
                    set("playerName", name)
                }
            }, Duration.millis(10.0))
        }
    }

    override fun initPhysics() {
        onCollisionBegin(SKILL_PROJECTILE, MONSTER) { proj, target ->

            if (proj.getObject<Any>("target") !== target) {
                return@onCollisionBegin
            }

            proj.removeFromWorld()
            player.characterComponent.useTargetSkill(geti(SELECTED_SKILL_INDEX), (target as CharacterEntity))
        }

        onCollisionCollectible(PLAYER, TEXT_TRIGGER_BOX, Consumer {
            spawnTextBox(it.getString("text"), it.x, it.y)
        })

        onCollisionCollectible(PLAYER, DIALOGUE, Consumer {
            startDialogue(it.getString("text"))
        })
    }

    override fun initUI() {
        getGameScene().setUIMouseTransparent(false)
        getGameScene().setCursor(image("ui/cursors/main.png"), Point2D(52.0, 10.0))

        getGameScene().addUINodes(
                BasicInfoView(player),
                InventoryView(player),
                HotbarView(player),
                MessagesView()
        )

        // add visual output to logger
        Logger.addOutput(object : LoggerOutput {
            override fun append(message: String) {
                getExecutor().startAsyncFX {
                    getGameScene().uiNodes
                            .filterIsInstance(MessagesView::class.java)
                            .forEach { it.appendMessage(message.drop(70)) }
                }
            }

            override fun close() { }
        }, LoggerLevel.INFO)
    }

    override fun onUpdate(tpf: Double) {
        currentMap.onUpdate(tpf)
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(ZephyriaApp::class.java, args)
}
