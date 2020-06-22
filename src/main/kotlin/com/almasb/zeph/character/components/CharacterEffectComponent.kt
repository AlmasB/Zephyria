package com.almasb.zeph.character.components

import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Effect
import com.almasb.zeph.combat.Status
import javafx.collections.FXCollections

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterEffectComponent : Component() {

    private lateinit var char: CharacterEntity

    /**
     * Statuses currently affecting this character.
     */
    private val statuses = FXCollections.observableArrayList<Status>()

    /**
     * Effects currently placed on this character.
     */
    private val effects = FXCollections.observableArrayList<Effect>()

    override fun onAdded() {
        char = entity as CharacterEntity
    }

    override fun onUpdate(tpf: Double) {
        updateEffects(tpf)
    }

    private fun updateEffects(tpf: Double) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val e = it.next()
            e.duration -= tpf
            if (e.duration <= 0) {
                end(e)
                it.remove()
            }
        }
    }

    /**
     * Applies an effect to this character. If the effect comes from the same
     * source, e.g. skill, the effect will be re-applied (will reset its timer).
     *
     * @param e effect
     */
    fun addEffect(effect: Effect) {
        val it = effects.iterator()
        while (it.hasNext()) {
            val eff = it.next()
            if (eff.sourceID == effect.sourceID) {
                end(eff)
                it.remove()
                break
            }
        }

        begin(effect)
        effects += effect
    }

    fun removeEffect(effect: Effect) {
        TODO()
    }

    private fun begin(effect: Effect) {
        effect.status?.let { statuses += it }
        effect.onBegin(char)
    }

    private fun end(effect: Effect) {
        effect.status?.let { statuses -= it }
        effect.onEnd(char)
    }

    /**
     * @return true if character is under status, false otherwise
     */
    fun hasStatus(status: Status): Boolean = status in statuses
}