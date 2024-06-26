package com.almasb.zeph.events

import com.almasb.fxgl.dsl.*
import com.almasb.zeph.EntityType.PLAYER
import com.almasb.zeph.Gameplay
import com.almasb.zeph.Gameplay.spawnItem
import com.almasb.zeph.character.components.RandomWanderComponent
import com.almasb.zeph.combat.runIfChance
import com.almasb.zeph.item.ArmorType.*
import com.almasb.zeph.pushMessage
import javafx.scene.paint.Color
import java.util.function.Consumer

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object EventHandlers {

    fun initialize() {
        getEventBus().isLoggingEnabled = false

        onEvent(Events.ON_ATTACK) {
            if (it.attacker.isType(PLAYER)) {
                it.attacker.playerComponent!!.weapon.value.onAttack(it.attacker, it.target)
            }

            if (isReleaseMode()) {
                val value = random(0, 4)

                play("slash$value.wav")
            }
        }

        onEvent(Events.ON_PHYSICAL_DAMAGE_DEALT) {
            Gameplay.showDamage(
                    it.damage, it.isCritical, it.target.center,
                    if (it.target.isPlayer) Color.LIGHTGRAY.darker() else Color.WHITE
            )
        }

        onEvent(Events.ON_MAGICAL_DAMAGE_DEALT) {
            Gameplay.showDamage(
                    it.damage, it.isCritical, it.target.center,
                    if (it.target.isPlayer) Color.LIGHTGRAY.darker() else Color.WHITE
            )
        }

        onEvent(Events.ON_BEFORE_SKILL_CAST) {
            Gameplay.showSkillCast(it.skill, it.caster.center.subtract(0.0, 35.0))
        }

        onEvent(Events.ON_XP_RECEIVED) {
            pushMessage("Received: ${it.xp.base} XP")
        }

        onEvent(Events.ON_MONEY_RECEIVED) {
            pushMessage("Received: ${it.amount} gold")

            Gameplay.showMoneyEarned(it.amount, it.receiver.position)
        }

        onEvent(Events.ON_ITEM_PICKED_UP) {
            pushMessage("Got: ${it.item.description.name}")
        }

        onEvent(Events.ON_LEVEL_UP) {
            pushMessage("Level Up!")

            spawn("level_up", it.char.center)
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

            if (!it.killedEntity.isPlayer) {
                val deathSoundName = it.killedEntity.characterComponent.data.deathSoundName

                if (deathSoundName.isNotEmpty()) {
                    if (isReleaseMode()) {
                        play(deathSoundName)
                    }
                }
            }
        }

        // SOUNDS

        if (isReleaseMode()) {
            onEvent(Events.ON_ITEM_PICKED_UP) {
                play("pick_up_item.wav")
            }

            onEvent(Events.ON_SKILL_LEARNED) {
                play("skill_level_up.wav")
            }

            onEvent(Events.ON_ARMOR_EQUIPPED) {
                when (it.armor.type) {
                    HELM -> play("equip_armor_helm.wav")
                    BODY -> play("equip_armor_body.wav")
                    SHOES -> play("equip_armor_shoes.wav")
                }
            }

            onEvent(Events.ON_WEAPON_EQUIPPED) {
                play("equip_weapon.wav")
            }

            onEvent(Events.ON_ORDERED_MOVE) {
                if (it.char.isPlayer) {
                    val num = random(0, 1)
                    play("voice/male/move${num}_alex.wav")
                }
            }

            onEvent(Events.ON_ITEM_USED) {
                val soundName = it.item.data.useSoundName

                if (soundName.isNotEmpty()) {
                    play(soundName)
                }
            }
        }
    }
}