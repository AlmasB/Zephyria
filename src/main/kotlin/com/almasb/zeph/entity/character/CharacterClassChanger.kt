package com.almasb.zeph.entity.character

import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
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

    fun canChangeClass(player: PlayerEntity): Boolean {
        val r = reqList[player.charClass.value]
        return r != null && player.baseLevel.value >= r.baseLevel && player.jobLevel.value >= r.jobLevel
    }

    fun getAscensionClasses(player: PlayerEntity) = reqList[player.charClass.value]!!.classesTo

    private class Ascension(val baseLevel: Int, val jobLevel: Int, vararg classes: CharacterClass) {
        val classesTo = classes as Array<CharacterClass>
    }
}