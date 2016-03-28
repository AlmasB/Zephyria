package com.almasb.zeph.entity.character

import com.almasb.ents.Entity
import com.almasb.zeph.entity.character.component.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerEntity : Entity() {

    val money = MoneyComponent(1000)

    val baseLevel = LevelComponent()
    val attributes = AttributesComponent()
    val stats = StatsComponent()
    val data = PlayerDataComponent()

    init {
        addComponent(money)
        addComponent(baseLevel)
        addComponent(attributes)
        addComponent(stats)
        addComponent(data)
    }
}