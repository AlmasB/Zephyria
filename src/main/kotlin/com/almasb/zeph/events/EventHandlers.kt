package com.almasb.zeph.events

import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.dsl.onEvent
import com.almasb.fxgl.dsl.random
import com.almasb.zeph.EntityType.PLAYER
import com.almasb.zeph.Gameplay
import com.almasb.zeph.Gameplay.spawnItem
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.ai.RandomWanderComponent
import com.almasb.zeph.combat.GameMath.checkChance
import com.almasb.zeph.combat.runIfChance
import javafx.scene.paint.Color
import java.util.function.Consumer

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object EventHandlers {

    fun initialize() {
        onEvent(Events.ON_ATTACK) {
            if (it.attacker.isType(PLAYER)) {
                it.attacker.playerComponent!!.weapon.value.onAttack(it.attacker, it.target)
            }
        }

        onEvent(Events.ON_PHYSICAL_DAMAGE_DEALT) {
            Gameplay.showDamage(
                    it.damage, it.isCritical, it.target.center,
                    if (it.target.isPlayer) Color.LIGHTGRAY.darker() else Color.WHITE
            )

            if (!it.target.isType(PLAYER)) {
                it.target.actionComponent.orderAttack(it.attacker)
                it.target.getComponent(RandomWanderComponent::class.java).pause()
            }
        }

        onEvent(Events.ON_MAGICAL_DAMAGE_DEALT) {
            Gameplay.showDamage(
                    it.damage, it.isCritical, it.target.center,
                    if (it.target.isPlayer) Color.LIGHTGRAY.darker() else Color.WHITE
            )

            if (!it.target.isType(PLAYER)) {
                it.target.actionComponent.orderAttack(it.attacker)
                it.target.getComponent(RandomWanderComponent::class.java).pause()
            }
        }

        onEvent(Events.ON_MONEY_RECEIVED) {
            Gameplay.showMoneyEarned(it.amount, it.receiver.position)
        }

        onEvent(Events.ON_BEING_KILLED) {

            if (it.killer.isPlayer) {
                val player = it.killer
                val mob = it.killedEntity

                val levelDiff = mob.baseLevel.value - player.baseLevel.value
                val money = (if (levelDiff > 0) levelDiff * 5 else 0) + random(0, mob.baseLevel.value)

                player.playerComponent!!.rewardMoney(money)
                player.playerComponent!!.rewardXP(mob.data.rewardXP)

                mob.data.dropItems.forEach(Consumer { (itemID, chance) ->
                    runIfChance(chance) {
                        spawnItem(itemID, mob.cellX, mob.cellY)
                    }
                })
            }
        }
    }
}