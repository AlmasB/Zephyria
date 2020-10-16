package com.almasb.zeph.character

import java.util.*

/**
 * Character classes with base hp/sp.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class CharacterClass(val hp: Int, val sp: Int, val description: String) {
    MONSTER  (50, 50, ""),
    NOVICE   (10, 10, "You've only started your adventure! You have many great opportunities before you!"),

    // Warrior tree
    WARRIOR  (40, 20, "Warriors are strong, tough and fearless. They will fight until their last breath and against all odds, will often emerge victorious."),

    CRUSADER (60, 35, "Crusaders are guardians of light. By using divine armor and weapons, they become virtually indestructible."),
    PALADIN  (90, 50, "TODO"),

    GLADIATOR(65, 30, "Gladiators train their whole life to be the unstoppable force they are. Their raw strength can turn the tides of any battle."),
    KNIGHT   (100, 35, "TODO"),

    // Scout tree
    SCOUT    (30, 35, "Scouts are nimble experts with a bow. They often use unconventional tactics to surprise and, ultimately, defeat their opponents."),

    ROGUE    (50, 40, "Rogues are shameless combatants who prefer daggers to execute their swift and deadly attacks. No matter who their enemies are, a rogue will always find a chink in their armor."),
    ASSASSIN (75, 45, "Assassins have mastered the art of killing. Always walking in the shadows, their enemies will never know what hit them."),

    RANGER   (40, 40, "TODO"),
    HUNTER   (50, 55, "TODO"),

    // Mage tree
    MAGE     (25, 45, "Mages are adepts at manipulating the elements themselves. By tapping into the nature's powers, they can defeat their enemies with a single thought."),

    WIZARD   (35, 60, "Wizards have gained the knowledge to blend elements together, multiplying their skill power. They are able to penetrate even most powerful magic barriers."),
    ARCHMAGE (45, 75, "Archmages' arcane powers are second to none. Only few exist who can withstand their full destructive power."),

    ENCHANTER(30, 65, "TODO"),
    SAGE     (40, 90, "TODO");
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