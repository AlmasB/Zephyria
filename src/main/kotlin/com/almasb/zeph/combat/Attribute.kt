package com.almasb.zeph.combat

import com.almasb.zeph.combat.Attribute.AM.*
import com.almasb.zeph.combat.Stat.*

/**
 * Primary attributes (9 in total) of a game character.
 *
 * @author Almas Baimagambetov
 */
enum class Attribute(description: String, vararg cStats: Pair<Stat, AM>) {

    STRENGTH("Increases damage dealt by physical attacks. Increases HP and regeneration.",
            ATK increase VERY_HIGH,
            MAX_HP increase MEDIUM,
            HP_REGEN increase LOW),

    VITALITY("Reduces damage you take. Increases HP and regeneration.",
            MAX_HP increase (VERY_HIGH),
            HP_REGEN increase (HIGH),
            DEF increase (HIGH)),

    DEXTERITY("Increases attack speed and improves offense based stats.",
            ATK increase (MEDIUM),
            MATK increase (MEDIUM),
            ASPD increase (LOW),
            MSPD increase (LOW)),

    AGILITY("Allows to attack faster with all weapons.",
            ASPD increase (VERY_HIGH)),

    INTELLECT("Increases magic damage and defense. Provides greater SP pool.",
            MATK increase (VERY_HIGH),
            MDEF increase (MEDIUM),
            MAX_SP increase (LOW)),

    WISDOM("Increases SP pool and regeneration. Increases magic damage.",
            MATK increase (VERY_LOW),
            MDEF increase (VERY_LOW),
            MAX_SP increase (VERY_HIGH),
            SP_REGEN increase (VERY_HIGH)),

    WILLPOWER("Increases magic defense and SP pool. Increases critical hit chance with skills",
            MCRIT_CHANCE increase (HIGH),
            MDEF increase (VERY_HIGH),
            MAX_SP increase (LOW),
            MSPD increase (MEDIUM)),

    PERCEPTION("Improves all combat stats.",
            ATK increase (LOW),
            MATK increase (LOW),
            DEF increase (VERY_LOW),
            MDEF increase (VERY_LOW),
            CRIT_CHANCE increase (VERY_LOW),
            MCRIT_CHANCE increase (VERY_LOW)),

    LUCK("Affects the ability to score critical hits and deal more critical damage.",
            CRIT_CHANCE increase (VERY_HIGH),
            MCRIT_CHANCE increase (VERY_HIGH),
            CRIT_DMG increase (VERY_LOW),
            MCRIT_DMG increase (VERY_LOW));

    val description: String

    @Suppress("UNCHECKED_CAST")
    val stats: Array<Pair<Stat, AM> > = cStats as Array<Pair<Stat, AM> >

    init {
        this.description = "$description\n" + stats.joinToString("\n") { pair -> "%12s %-5s".format(pair.first, pair.second.desc()) }
    }

    override fun toString() = this.name.substring(0, 3)

    /**
     * Attribute modifier.
     */
    enum class AM {
        VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH;

        fun value() = (ordinal + 1) * 0.1

        fun desc() = "+".repeat(ordinal + 1)
    }

    /**
     * Data structure for an info object that contains
     * values of all the attributes.
     */
    class AttributeInfo {
        val attrs = hashMapOf<Attribute, Int>()

        init {
            // add 0 defaults
            Attribute.values().forEach {
                it +0
            }
        }

        operator fun Attribute.plus(value: Int) {
            attrs[this] = value
        }
    }
}

private infix fun Stat.increase(am: Attribute.AM) = this to am

/**
 * Increases an attribute.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Rune(val attribute: Attribute, val bonus: Int) {

    override fun toString() = "$attribute +$bonus"
}