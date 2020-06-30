package com.almasb.zeph

import com.almasb.fxgl.animation.Interpolators
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.cutscene.dialogue.FunctionCallHandler
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.components.ExpireCleanComponent
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader
import com.almasb.fxgl.ui.FontType
import com.almasb.zeph.Config.Z_INDEX_DAMAGE_TEXT
import com.almasb.zeph.Vars.GAME_MAP
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.data.Data
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.util.Duration

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Gameplay : FunctionCallHandler {

    fun getPlayer(): CharacterEntity {
        return getGameWorld().getSingleton(EntityType.PLAYER) as CharacterEntity
    }

    fun getCurrentMap(): GameMap {
        return geto(GAME_MAP)
    }

    fun goto(toCellX: Int, toCellY: Int) {
        val player = getPlayer()

        player.actionComponent.orderIdle()
        player.setPositionToCell(toCellX, toCellY)
    }

    fun gotoMap(mapName: String, toCellX: Int, toCellY: Int) {
        val level = getAssetLoader().loadLevel("tmx/$mapName", TMXLevelLoader())

        val map = GameMap(level)
        map.enter()

        set(GAME_MAP, map)

        val player = getPlayer()

        player.removeComponent(NewAStarMoveComponent::class.java)
        player.addComponent(NewAStarMoveComponent(map.grid))

        player.actionComponent.orderIdle()
        player.setPositionToCell(toCellX, toCellY)
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

    fun spawnMob(id: Int, cellX: Int, cellY: Int) {
        getCurrentMap().spawnMonster(cellX, cellY, Data.getCharacterData(id))
    }

    fun spawnItem(id: Int, cellX: Int, cellY: Int) {
        getCurrentMap().spawnItem(cellX, cellY, Data.getItemData(id))
    }

    override fun handle(functionName: String, args: Array<String>): Any {
        val cmd = functionName.toLowerCase()

        when (cmd) {
            "tp" -> {
                val cellX = args[0].toInt()
                val cellY = args[1].toInt()

                goto(cellX, cellY)
            }

            "spawn_mob" -> {
                val mobID = args[0].toInt()
                val cellX = args[1].toInt()
                val cellY = args[2].toInt()

                spawnMob(mobID, cellX, cellY)
            }

            "spawn_item" -> {
                val itemID = args[0].toInt()
                val cellX = args[1].toInt()
                val cellY = args[2].toInt()

                spawnItem(itemID, cellX, cellY)
            }

            else -> {
                println("Unrecognized command: $cmd")
            }
        }

        return 0
    }
}