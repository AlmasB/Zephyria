package com.almasb.zeph.combat

/**
 * Increases an attribute.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Rune(val attribute: Attribute, val bonus: Int) {

    override fun toString() = "$attribute +$bonus"
}