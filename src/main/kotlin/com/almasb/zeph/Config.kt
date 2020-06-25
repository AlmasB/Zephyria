package com.almasb.zeph

/**
 * Holds various constants.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Config {

    const val SPRITE_SIZE = 64
    const val TILE_SIZE = 32

    /**
     * Pixels per seconds.
     */
    const val CHAR_MOVE_SPEED = TILE_SIZE * 4.0

    /**
     * How much time a char does not move (in seconds) before randomly moving.
     */
    const val CHAR_IDLE_TIME = 3.0

    // These must be outside of cellY values since each char's z-index = cellY
    // We have these statically since, when parsed, a tile layer is a single entity
    const val Z_INDEX_DECOR_BELOW_PLAYER = -50
    const val Z_INDEX_DECOR_ABOVE_PLAYER = 5000

    const val Z_INDEX_CELL_SELECTION = 5100

    const val MAP_WIDTH = 150
    const val MAP_HEIGHT = 150

    // CHARACTER VALUES

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



    // GAMEPLAY

    const val STARTING_MONEY = 100

    const val MAX_INVENTORY_SIZE = 30

    const val SKILL_PROJECTILE_SPEED = 1000.0
}

object Vars {
    const val IS_SELECTING_SKILL_TARGET_CHAR = "IS_SELECTING_SKILL_TARGET_CHAR"

    const val SELECTED_SKILL_INDEX = "SELECTED_SKILL_INDEX"

}