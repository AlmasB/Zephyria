package com.almasb.zeph.events

import com.almasb.fxgl.dsl.onEvent
import com.almasb.zeph.EntityType
import com.almasb.zeph.character.ai.RandomWanderComponent

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object EventHandlers {

    fun initialize() {
        onEvent(Events.ON_ATTACK) {
            if (!it.target.isType(EntityType.PLAYER)) {
                it.target.actionComponent.orderAttack(it.attacker)
                it.target.getComponent(RandomWanderComponent::class.java).pause()
            }
        }
    }
}