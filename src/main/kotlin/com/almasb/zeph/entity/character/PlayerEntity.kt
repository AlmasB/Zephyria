package com.almasb.zeph.entity.character

import com.almasb.ents.Entity
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.entity.character.component.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerEntity : GameEntity() {

    val money = MoneyComponent(1000)

    val hp = HPComponent()
    val sp = SPComponent()

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

        addComponent(hp)
        addComponent(sp)
    }
}