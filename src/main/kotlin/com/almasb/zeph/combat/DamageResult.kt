package com.almasb.zeph.combat

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class DamageResult(val type: DamageType, val element: Element, val value: Int, val critical: Boolean) {

    companion object {
        val NONE = DamageResult(DamageType.PURE, Element.NEUTRAL, 0, false)
    }

    override fun toString() = "DamageResult($value $type $element Critical:$critical)"
}