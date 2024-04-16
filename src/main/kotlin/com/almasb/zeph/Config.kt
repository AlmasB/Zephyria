package com.almasb.zeph

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.addUINode
import com.almasb.fxgl.logging.Logger
import com.almasb.zeph.ui.TooltipView

/**
 * Holds all constants and configurations.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Config {

    const val SPRITE_SIZE = 64
    const val TILE_SIZE = 32

    /**
     * Pixels per seconds.
     */
    const val CHAR_MOVE_SPEED = TILE_SIZE * 8.0

    /**
     * How much time a char does not move (in seconds) before randomly moving.
     */
    const val CHAR_IDLE_TIME = 3.0

    // These must be outside of cellY values since each char's z-index = cellY
    // We have these statically since, when parsed, a tile layer is a single entity

    // Background has z-index of 0

    // for chars on a map z-index ranges [5000..5150]
    const val Z_INDEX_DECOR_BELOW_PLAYER = 4000
    const val Z_INDEX_DECOR_ABOVE_PLAYER = 6000

    const val Z_INDEX_CHARACTER = 5000

    // TODO: add another decor above / below player to allow multiple layers in Tiled

    const val Z_INDEX_DAMAGE_TEXT = 6500

    const val Z_INDEX_CELL_SELECTION = 3500

    const val MAP_WIDTH = 150
    const val MAP_HEIGHT = 150

    // GAMEPLAY / CHARACTER VALUES

    /**
     * When regeneration happens, in seconds.
     */
    const val REGEN_INTERVAL = 2.0

    /**
     * How slow can a character attack, i.e. 1 attack in **n** seconds.
     */
    const val SLOWEST_ATTACK_INTERVAL = 3.0

    const val MAX_LEVEL_BASE = 100
    const val MAX_LEVEL_ATTR = 100
    const val MAX_LEVEL_JOB = 60
    const val MAX_LEVEL_SKILL = 10
    const val MAX_ATTRIBUTE = 100

    const val ATTRIBUTE_POINTS_PER_LEVEL = 3
    const val ATTRIBUTE_POINTS_AT_LEVEL1 = 3
    const val SKILL_POINTS_AT_LEVEL1 = 3

    /**
     * By what value should experience needed for next level
     * increase per level.
     */
    const val EXP_NEEDED_INC_BASE = 1.75f
    const val EXP_NEEDED_INC_STAT = 1.5f
    const val EXP_NEEDED_INC_JOB  = 2.25f

    const val EXP_NEEDED_FOR_LEVEL2 = 10

    const val STARTING_MONEY = 100

    const val MAX_STORAGE_SIZE = 100
    const val MAX_INVENTORY_SIZE = 30
    const val MAX_ESSENCES = 3
    const val MAX_ITEM_REFINE_LEVEL = 5
}

object Vars {
    const val GAME_MAP = "GAME_MAP"
}

private val tooltipView by lazy {
    TooltipView(300.0).also {
        it.hide()
        addUINode(it, FXGL.getAppWidth() - 300.0, 0.0)
    }
}

private val log = Logger.get("Zephyria")

fun getUITooltip() = tooltipView

/**
 * Push given [message] to in-game message view.
 */
fun pushMessage(message: String) {
    // for now we just make use of the logger to unify all message calls to this function
    log.info(message)
}