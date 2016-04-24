package com.almasb.zeph.combat

/**
 * Increases a stat.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Essence(val stat: Stat, val bonus: Int) {

    override fun toString() = "$stat +$bonus"
}