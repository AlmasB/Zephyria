package com.almasb.zeph.entity.character

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class CharacterClass(val hp: Int, val sp: Int) {
    MONSTER  (50, 50),
    NOVICE   (10, 10),

    WARRIOR  (40, 20),
    CRUSADER (60, 35),
    PALADIN  (90, 50),
    GLADIATOR(65, 30),
    KNIGHT   (100, 35),

    SCOUT    (30, 35),
    ROGUE    (50, 40),
    ASSASSIN (75, 45),
    RANGER   (40, 40),
    HUNTER   (50, 55),

    MAGE     (25, 45),
    WIZARD   (35, 60),
    ARCHMAGE (45, 75),
    ENCHANTER(30, 65),
    SAGE     (40, 90);
}