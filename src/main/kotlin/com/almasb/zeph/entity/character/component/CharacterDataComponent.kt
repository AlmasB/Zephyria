package com.almasb.zeph.entity.character.component

import com.almasb.ents.AbstractComponent
import com.almasb.fxgl.texture.DynamicAnimatedTexture
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.entity.character.CharacterType
import javafx.beans.property.SimpleIntegerProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterDataComponent(val type: CharacterType) : AbstractComponent() {

    val baseLevel = SimpleIntegerProperty(1)
    val attributes = AttributesComponent()

    lateinit var animation: DynamicAnimatedTexture

    fun withLevel(value: Int): CharacterDataComponent {
        baseLevel.value = value
        return this
    }

    fun withAttribute(attribute: Attribute, value: Int): CharacterDataComponent {
        attributes.setAttribute(attribute, value)
        return this
    }
}