package com.almasb.zeph

/**
 * All entity types in the game.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class EntityType {

    /**
     * Only the (singleton) player entity has this type.
     */
    PLAYER,

    /**
     * Any in-game character that does not fight,
     * but can be interacted with.
     */
    NPC,

    /**
     * Any in-game character (apart from player) that can fight.
     */
    MONSTER,

    /**
     * Any free-standing item inside the game world
     * that can be picked up by the player.
     */
    ITEM,

    /**
     * A treasure chest inside the game world that, when opened,
     * spawns lootable items.
     */
    TREASURE_CHEST,

    /**
     * A navigable (walkable) cell inside the game world.
     */
    NAV,

    /**
     * An entity that transports the player from one map to another.
     */
    PORTAL,

    /**
     * The singleton entity that shows the cell selection inside the game world.
     */
    CELL_SELECTION,

    /**
     * An invisible trigger for displaying text.
     */
    TEXT_TRIGGER_BOX,

    /**
     * An invisible trigger for starting a dialogue.
     */
    DIALOGUE_TRIGGER_BOX
}