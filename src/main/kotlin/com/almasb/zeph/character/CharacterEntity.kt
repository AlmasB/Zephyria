package com.almasb.zeph.character

import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.zeph.Inventory
import com.almasb.zeph.character.components.AnimationComponent
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.character.components.CharacterComponent

/**
 * This is a convenience class and DOES NOT have any logic.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(val data: CharacterData, val characterComponent: CharacterComponent) : Entity() {

    constructor(data: CharacterData) : this(data, CharacterComponent(data))

    val animationComponent = AnimationComponent(data.description.textureName)
    val actionComponent = CharacterActionComponent()

    val attributes = characterComponent.attributes

    val stats = characterComponent.stats

    val hp = characterComponent.hp
    val sp = characterComponent.sp

    val baseLevel = characterComponent.baseLevel

    val inventory: Inventory
        get() = characterComponent.inventory

    init {
        addComponent(StateComponent())
        addComponent(EffectComponent())
        addComponent(characterComponent)
        addComponent(animationComponent)
        addComponent(actionComponent)
    }

    fun kill() {
        characterComponent.kill()
    }
}