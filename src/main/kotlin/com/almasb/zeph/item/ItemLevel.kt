package com.almasb.zeph.item

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class ItemLevel private constructor(// 100/80/60/40/20

        val bonus: Int, val refineChanceReduction: Int, val maxRunes: Int) {
    NORMAL(3, 10, 2), // refinement chance 100/90/80/70/60
    UNIQUE(5, 15, 3), // 100/85/70/55/40
    EPIC(10, 20, 4)
}