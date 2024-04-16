package com.almasb.zeph

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.scene.LoadingScene
import com.almasb.fxgl.app.scene.SceneFactory
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.FXGL.Companion.getSceneService
import com.almasb.fxgl.dsl.FXGL.Companion.onCollisionCollectible
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.logging.LoggerLevel
import com.almasb.fxgl.logging.LoggerOutput
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.Gameplay.currentMap
import com.almasb.zeph.Gameplay.gotoMap
import com.almasb.zeph.Gameplay.player
import com.almasb.zeph.Gameplay.spawnChar
import com.almasb.zeph.Gameplay.spawnTextBox
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.data.Data
import com.almasb.zeph.events.EventHandlers
import com.almasb.zeph.gameplay.ClockService
import com.almasb.zeph.ui.BasicInfoView
import com.almasb.zeph.ui.HotbarView
import com.almasb.zeph.ui.MessagesView
import com.almasb.zeph.ui.PlayerInventoryView
import com.almasb.zeph.ui.scenes.DevScene
import com.almasb.zeph.ui.scenes.ZephLoadingScene
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode.*
import javafx.scene.paint.Color
import java.util.function.Consumer

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
        settings.addEngineService(ClockService::class.java)
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
        onKeyDown(C) {
            getGameScene().uiNodes
                    .filterIsInstance(BasicInfoView::class.java)
                    .forEach { it.minBtn.onClick() }
        }

        onKeyDown(I) {
            getGameScene().uiNodes
                    .filterIsInstance(PlayerInventoryView::class.java)
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

        if (!isReleaseMode()) {
            onKeyDown(ENTER) {
                getSceneService().pushSubScene(devScene!!)
            }

            onKeyDown(F) {
                //Gameplay.startDialogueNPC("context_test.json", 2505)

                Gameplay.startCombat(player, getGameWorld().getClosestEntity(player) { it.isType(MONSTER) }.get() as CharacterEntity)

                //val quest = com.almasb.zeph.quest.Quest(Data.Quests.TUTORIAL_KILLS)

                //println(quest.data.description)

                //Gameplay.openStorage()

//                val clockService = getService(ClockService::class.java)
//                clockService.clock.runAt(12, 0) {
//                    pushMessage("It's 12:00!")
//                }
//
//                addUINode(TextClockView(clockService.clock), 300.0, 300.0)
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

    }

    override fun initGame() {
        devScene = DevScene()

        getGameScene().setBackgroundColor(Color.BLACK)

        getGameWorld().addEntityFactory(ZephFactory())

        val player = spawn("player") as CharacterEntity

        spawn("cellSelection")

        getGameScene().viewport.isLazy = true
        getGameScene().viewport.bindToEntity(player, getAppWidth() / 2.toDouble(), getAppHeight() / 2.toDouble())
        getGameScene().viewport.setZoom(1.5)

        gotoMap("dev_world.tmx", 8, 6)
        //gotoMap("tutorial.tmx", 8, 6)
        //gotoMap("test_map.tmx", 2, 6)

        spawnChar(Data.Monsters.SKELETON_WARRIOR, 10, 7)

        getExecutor().startAsyncFX {
            FXGL.getNotificationService().pushNotification("Certain actions may crash the game.")
        }
    }

    override fun initPhysics() {
        onCollisionCollectible(PLAYER, TEXT_TRIGGER_BOX, Consumer {
            spawnTextBox(it.getString("text"), it.x, it.y)
        })

        onCollisionCollectible(PLAYER, DIALOGUE_TRIGGER_BOX, Consumer {
            //startDialogue(it.getString("text"))
        })
    }

    override fun initUI() {
        getGameScene().setUIMouseTransparent(false)
        getGameScene().setCursor(image("ui/cursors/main.png"), Point2D(52.0, 10.0))

        getGameScene().addUINodes(
                BasicInfoView(player),
                PlayerInventoryView(player),
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
