package com.almasb.zeph.character

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.extra.entity.effect.EffectComponent
import com.almasb.zeph.character.components.AnimationComponent
import com.almasb.zeph.character.components.CharacterComponent
import com.almasb.zeph.entity.character.component.AttributesComponent
import com.almasb.zeph.entity.character.component.StatsComponent

/**
 * This is a convenience class and DOES NOT have any logic.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(val data: CharacterData) : Entity() {

    val characterComponent = CharacterComponent(data)
    val animationComponent = AnimationComponent(data.description.textureName)
    val effectComponent = EffectComponent()

    val attributes: AttributesComponent
        get() = characterComponent.attributes

    val stats: StatsComponent
        get() = characterComponent.stats

    init {
        addComponent(characterComponent)
        addComponent(animationComponent)
        addComponent(effectComponent)
    }

    fun kill() {
        characterComponent.kill()
    }
}