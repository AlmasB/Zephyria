package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.cutscene.dialogue.FunctionCallHandler
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.ExpireCleanComponent
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.action.ActionComponent
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent
import com.almasb.fxgl.ui.FontType
import com.almasb.zeph.Config.Z_INDEX_DAMAGE_TEXT
import com.almasb.zeph.Vars.GAME_MAP
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.components.PortalComponent
import com.almasb.zeph.data.Data
import com.almasb.zeph.quest.QuestData
import com.almasb.zeph.skill.Skill
import com.almasb.zeph.ui.StorageView

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.util.Duration

/**
 * Functions of this object are automatically parsed to be available from the command line
 * and in-game dialogues.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Gameplay {

    private val log = Logger.get(javaClass)

    val player: CharacterEntity by lazy { getGameWorld().getSingleton(EntityType.PLAYER) as CharacterEntity }

    val currentMap: GameMap
        get() = geto(GAME_MAP)

    fun openStorage() {
        val storageView = StorageView()

        addUINode(storageView, 200.0, 200.0)
    }

    fun where() {
        log.info("Player is at ${player.cellX},${player.cellY}")
    }

    fun teleport(id: Int, toCellX: Int, toCellY: Int) {
        // TODO: generalise?

        log.debug("teleport($id, $toCellX, $toCellY)")

        FXGL.byID("NPC", id).ifPresent {
            log.debug("Teleporting NPC $id to $toCellX, $toCellY")

            it.getComponent(AStarMoveComponent::class.java).stopMovementAt(toCellX, toCellY)
        }
    }

    fun goto(toCellX: Int, toCellY: Int) {
        player.actionComponent.orderIdle()
        player.setPositionToCell(toCellX, toCellY)
    }

    fun gotoMap(mapName: String, toCellX: Int, toCellY: Int) {
        // handle case where destination is the current map
        if (FXGL.getWorldProperties().exists(GAME_MAP)) {
            if (currentMap.name == mapName) {
                goto(toCellX, toCellY)
                return
            }
        }


        // TODO: should Level be an abstraction, then we can have TiledMapLevel and other types
        val level = getAssetLoader().loadLevel("tmx/$mapName", TMXLevelLoader())

        log.info("Loaded level $mapName: " + level.width + "x" + level.height)

        if (FXGL.getWorldProperties().exists(GAME_MAP)) {
            currentMap.exit()
        }

        val newMap = GameMap(mapName, level)
        newMap.enter()

        set(GAME_MAP, newMap)

        player.removeComponent(AStarMoveComponent::class.java)
        player.addComponent(AStarMoveComponent(newMap.grid))

        player.actionComponent.orderIdle()
        player.setPositionToCell(toCellX, toCellY)

        getGameScene().viewport.setBounds(0, 0, level.width, level.height)
    }

    fun showDamage(damage: Int, isCritical: Boolean, position: Point2D, color: Color = Color.WHITE) {
        val text = getUIFactoryService().newText(
                damage.toString() + if (isCritical) "!" else "",
                color,
                FontType.GAME,
                if (isCritical) 28.0 else 26.0
        )

        text.stroke = Color.WHITE
        val view = GameView(text, Z_INDEX_DAMAGE_TEXT)

        getGameScene().addGameView(view)

        animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .translate(text)
                .from(Point2D(position.x + FXGL.random(-25, 0), position.y))
                .to(position.add(FXGL.random(-25, 0).toDouble(), FXGL.random(-40, -25).toDouble()))
                .buildAndPlay()

        animationBuilder()
                .onFinished(Runnable { getGameScene().removeGameView(view) })
                .duration(Duration.seconds(3.0))
                .fadeOut(text)
                .buildAndPlay()
    }

    fun showMoneyEarned(money: Int, position: Point2D) {
        val text = getUIFactoryService().newText(money.toString() + "G", 18.0)
        text.fill = Color.GOLD
        text.stroke = Color.GOLD

        val e = entityBuilder()
                .at(position)
                .view(text)
                .with(ExpireCleanComponent(Duration.seconds(1.2)))
                .buildAndAttach()

        animationBuilder()
                .duration(Duration.seconds(1.0))
                .translate(e)
                .from(position)
                .to(position.add(0.0, -30.0))
                .buildAndPlay()
    }

    fun showSkillCast(skill: Skill, position: Point2D) {
        val text = getUIFactoryService().newText(skill.data.description.name, Color.WHITE, FontType.GAME, 16.0)

        val e = entityBuilder()
                .at(position.x - text.layoutBounds.width / 2.0, position.y)
                .viewWithBBox(text)
                .with(ExpireCleanComponent(Duration.seconds(1.2)))
                .buildAndAttach()

        animationBuilder()
                .duration(Duration.seconds(0.33))
                .scale(e)
                .from(Point2D(0.5, 1.2))
                .to(Point2D(1.0, 1.0))
                .buildAndPlay()
    }

    fun spawn(entityType: String, cellX: Int, cellY: Int) {
        spawn(entityType,
                SpawnData(cellX * Config.TILE_SIZE.toDouble(), cellY * Config.TILE_SIZE.toDouble())
                        .put("cellX", cellX)
                        .put("cellY", cellY)
        )
    }

    fun spawnTextBox(text: String, cellX: Int, cellY: Int) {
        spawnTextBox(text, cellX * Config.TILE_SIZE.toDouble(), cellY * Config.TILE_SIZE.toDouble())
    }

    fun spawnTextBox(text: String, x: Double, y: Double) {
        spawn("textBox",
                SpawnData(x, y)
                        .put("cellX", x / Config.TILE_SIZE)
                        .put("cellY", y / Config.TILE_SIZE)
                        .put("text", text)
        )
    }

    fun startDialogue(dialogueFileName: String) {
        log.debug("Starting dialogue: $dialogueFileName")

        val graph = getAssetLoader().loadDialogueGraph(dialogueFileName)

        getCutsceneService().startDialogueScene(graph, CommandHandler)
    }

    fun enablePortal(id: Int) {
        FXGL.byID("portal", id).ifPresent {
            it.getComponent(PortalComponent::class.java).turnOn()
        }
    }

    /**
     * Make the entity with given [id] invisible.
     */
    fun setNotVisible(id: Int) {
        // TODO: generalize?
        FXGL.byID("NPC", id).ifPresent {
            it.isVisible = false
        }
    }

    fun spawnNPC(id: Int, cellX: Int, cellY: Int) {
        currentMap.spawnNPC(cellX, cellY, Data.getNPCData(id))
    }

    fun spawnMob(id: Int, cellX: Int, cellY: Int) {
        currentMap.spawnMonster(cellX, cellY, Data.getCharacterData(id))
    }

    fun spawnItem(id: Int, cellX: Int, cellY: Int) {
        currentMap.spawnItem(cellX, cellY, Data.getItemData(id))
    }

    fun addMoney(amount: Int) {
        player.playerComponent!!.rewardMoney(amount)
    }

    @JvmOverloads fun hasItem(itemID: Int, amount: Int = 1): Boolean {
        val itemEntry = player.inventory
                .allData
                .entries
                .find { it.key.description.id == itemID }
                ?: return false

        return player.inventory.getItemQuantity(itemEntry.key) >= amount
    }

    @JvmOverloads fun addItem(itemID: Int, amount: Int = 1): Boolean {
        log.info("addItem $itemID $amount")

        val item = Data.newItem(itemID)

        return player.inventory.add(item, quantity = amount)
    }

    @JvmOverloads fun removeItem(itemID: Int, amount: Int = 1) {
        log.info("removeItem $itemID $amount")

        player.inventory
                .itemsProperty()
                .find { it.description.id == itemID }
                ?.let {
                    player.inventory.incrementQuantity(it, -amount)
                }
    }


//            }

    // TODO: implement
    private val quests = arrayListOf<Int>()

    fun hasQuest(questID: Int) = questID in quests

    fun startQuest(questID: Int) {
        log.info("Started quest $questID")

        quests += questID
    }

    fun completeQuest(questID: Int) {
        log.info("Quest $questID is completed")

//        val quest = TODO()
//        val questData: QuestData = TODO()
//
//        addMoney(questData.rewardMoney)
    }

    fun failQuest(questID: Int) {

    }
}