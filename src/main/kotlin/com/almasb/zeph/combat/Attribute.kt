package com.almasb.zeph.combat

/**
 * Primary attributes (9 in total) of a game character.
 *
 * @author Almas Baimagambetov
 */
enum class Attribute private constructor(description: String, vararg cStats: Pair<Stat, AM>) {

    STRENGTH("Increases damage dealt by physical attacks. Increases HP and regeneration.",
            Stat.ATK.to(AM.VERY_HIGH),
            Stat.MAX_HP.to(AM.MEDIUM),
            Stat.HP_REGEN.to(AM.LOW)),

    VITALITY("Reduces damage you take. Increases HP and regeneration.",
            Stat.MAX_HP.to(AM.VERY_HIGH),
            Stat.HP_REGEN.to(AM.HIGH),
            Stat.DEF.to(AM.HIGH)),

    DEXTERITY("Increases attack speed and improves offense based stats.",
            Stat.ATK.to(AM.MEDIUM),
            Stat.MATK.to(AM.MEDIUM),
            Stat.ASPD.to(AM.LOW),
            Stat.MSPD.to(AM.LOW)),

    AGILITY("Allows to attack faster with all weapons.",
            Stat.ASPD.to(AM.VERY_HIGH)),

    INTELLECT("Increases magic damage and defense. Provides greater SP pool.",
            Stat.MATK.to(AM.VERY_HIGH),
            Stat.MDEF.to(AM.MEDIUM),
            Stat.MAX_SP.to(AM.LOW)),

    WISDOM("Increases SP pool and regeneration. Increases magic damage.",
            Stat.MATK.to(AM.VERY_LOW),
            Stat.MDEF.to(AM.VERY_LOW),
            Stat.MAX_SP.to(AM.VERY_HIGH),
            Stat.SP_REGEN.to(AM.VERY_HIGH)),

    WILLPOWER("Increases magic defense and SP pool. Increases critical hit chance with skills",
            Stat.MCRIT_CHANCE.to(AM.HIGH),
            Stat.MDEF.to(AM.VERY_HIGH),
            Stat.MAX_SP.to(AM.LOW),
            Stat.MSPD.to(AM.MEDIUM)),

    PERCEPTION("Improves all combat stats.",
            Stat.ATK.to(AM.LOW),
            Stat.MATK.to(AM.LOW),
            Stat.DEF.to(AM.VERY_LOW),
            Stat.MDEF.to(AM.VERY_LOW),
            Stat.CRIT_CHANCE.to(AM.VERY_LOW),
            Stat.MCRIT_CHANCE.to(AM.VERY_LOW)),

    LUCK("Affects the ability to score critical hits and deal more critical damage.",
            Stat.CRIT_CHANCE.to(AM.VERY_HIGH),
            Stat.MCRIT_CHANCE.to(AM.VERY_HIGH),
            Stat.CRIT_DMG.to(AM.VERY_LOW),
            Stat.MCRIT_DMG.to(AM.VERY_LOW));

    /**
     * @return attribute description
     */
    val description: String
    val stats: Array<Pair<Stat, AM> >

    init {
        @Suppress("UNCHECKED_CAST")
        this.stats = cStats as Array<Pair<Stat, AM> >

        this.description = /*"$description\n" +*/ stats.mapIndexed { i, pair -> "%12s %-5s".format(pair.first, pair.second.desc()) }
                .joinToString("\n", "", "", -1, "", null)
    }

    override fun toString() = if (this.name.length > 3) this.name.substring(0, 3) else this.name

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

        var str = 1
        var vit = 1
        var dex = 1
        var agi = 1
        var int_ = 1
        var wis = 1
        var wil = 1
        var per = 1
        var luc = 1

        fun str(value: Int): AttributeInfo {
            str = value
            return this
        }

        fun vit(value: Int): AttributeInfo {
            vit = value
            return this
        }

        fun dex(value: Int): AttributeInfo {
            dex = value
            return this
        }

        fun agi(value: Int): AttributeInfo {
            agi = value
            return this
        }

        fun int_(value: Int): AttributeInfo {
            int_ = value
            return this
        }

        fun wis(value: Int): AttributeInfo {
            wis = value
            return this
        }

        fun wil(value: Int): AttributeInfo {
            wil = value
            return this
        }

        fun per(value: Int): AttributeInfo {
            per = value
            return this
        }

        fun luc(value: Int): AttributeInfo {
            luc = value
            return this
        }
    }
}
