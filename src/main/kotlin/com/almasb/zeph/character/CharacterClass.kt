package com.almasb.zeph.character

import java.util.HashMap

/**
 * Character classes with base hp/sp.
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

/**
 * Enemies can be one of 3 types.
 */
enum class CharacterType {
    NORMAL, MINIBOSS, BOSS
}

object CharacterClassChanger {

    private val reqList = HashMap<CharacterClass, Ascension>()

    init {
        reqList[CharacterClass.NOVICE] = Ascension(2, 2, CharacterClass.WARRIOR, CharacterClass.SCOUT, CharacterClass.MAGE)

        reqList[CharacterClass.WARRIOR] = Ascension(3, 3, CharacterClass.CRUSADER, CharacterClass.GLADIATOR)
        reqList[CharacterClass.SCOUT] = Ascension(3, 3, CharacterClass.ROGUE, CharacterClass.RANGER)
        reqList[CharacterClass.MAGE] = Ascension(3, 3, CharacterClass.WIZARD, CharacterClass.ENCHANTER)

        reqList[CharacterClass.CRUSADER] = Ascension(5, 5, CharacterClass.PALADIN)
        reqList[CharacterClass.GLADIATOR] = Ascension(5, 5, CharacterClass.KNIGHT)

        reqList[CharacterClass.ROGUE] = Ascension(5, 5, CharacterClass.ASSASSIN)
        reqList[CharacterClass.RANGER] = Ascension(5, 5, CharacterClass.HUNTER)

        reqList[CharacterClass.WIZARD] = Ascension(5, 5, CharacterClass.ARCHMAGE)
        reqList[CharacterClass.ENCHANTER] = Ascension(5, 5, CharacterClass.SAGE)
    }

//    fun canChangeClass(player: Entity): Boolean {
//        val r = reqList[player.charClass.value]
//        return r != null && player.baseLevel.value >= r.baseLevel && player.jobLevel.value >= r.jobLevel
//    }
//
//    fun getAscensionClasses(player: Entity) = reqList[player.charClass.value]!!.classesTo

    private class Ascension(val baseLevel: Int, val jobLevel: Int, vararg classes: CharacterClass) {
        val classesTo = classes as Array<CharacterClass>
    }
}