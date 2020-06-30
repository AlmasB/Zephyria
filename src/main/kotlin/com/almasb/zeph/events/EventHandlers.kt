package com.almasb.zeph.events

import com.almasb.fxgl.dsl.onEvent
import com.almasb.zeph.EntityType
import com.almasb.zeph.EntityType.*
import com.almasb.zeph.Gameplay
import com.almasb.zeph.character.ai.RandomWanderComponent
import javafx.scene.paint.Color

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object EventHandlers {

    fun initialize() {
        onEvent(Events.ON_ATTACK) {
            if (!it.target.isType(PLAYER)) {
                it.target.actionComponent.orderAttack(it.attacker)
                it.target.getComponent(RandomWanderComponent::class.java).pause()
            }

            if (it.attacker.isType(PLAYER)) {
                it.attacker.playerComponent!!.weapon.value.onAttack(it.attacker, it.target)
            }
        }

        onEvent(Events.ON_PHYSICAL_DAMAGE_DEALT) {
            Gameplay.showDamage(
                    it.damage, it.isCritical, it.target.center,
                    if (it.target.isPlayer) Color.LIGHTGRAY.darker() else Color.WHITE
            )
        }

        onEvent(Events.ON_MONEY_RECEIVED) {
            Gameplay.showMoneyEarned(it.amount, it.receiver.position)
        }
    }
}