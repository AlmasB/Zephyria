package com.almasb.zeph.entity.ai

import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.zeph.entity.character.CharacterEntity

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AIControl : AbstractControl() {

    lateinit var char: CharacterEntity

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        // if (in range) add attack control
        // else add random wander
    }
}