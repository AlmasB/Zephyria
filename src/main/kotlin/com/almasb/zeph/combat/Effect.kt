package com.almasb.zeph.combat

import com.almasb.zeph.entity.character.CharacterEntity

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
interface Effect {

    fun id()

    fun onBegin(char: CharacterEntity)

    fun onUpdate(tpf: Double)

    fun onEnd(char: CharacterEntity)
}