package com.almasb.zeph.character

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.extra.entity.effect.EffectComponent
import com.almasb.zeph.character.components.AnimationComponent
import com.almasb.zeph.character.components.CharacterComponent

/**
 * This is a convenience class and DOES NOT have any logic.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterEntity(val data: CharacterData) : Entity() {

    val characterComponent = CharacterComponent(data)
    val animationComponent = AnimationComponent(data.description.textureName)
    val effectComponent = EffectComponent()

    init {
        addComponent(characterComponent)
        addComponent(animationComponent)
        addComponent(effectComponent)
    }

    fun kill() {
        characterComponent.kill()
    }
}