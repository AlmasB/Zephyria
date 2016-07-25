package com.almasb.zeph.entity.character.component

import com.almasb.ents.AbstractComponent
import com.almasb.fxgl.texture.DynamicAnimatedTexture
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Element
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.entity.Data
import com.almasb.zeph.entity.character.CharacterClass
import com.almasb.zeph.entity.character.CharacterType
import com.almasb.zeph.entity.item.WeaponEntity
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import java.util.*

/**
 * TODO: move some stuff to CharacterEntity and leave only "value" fields
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CharacterDataComponent(val type: CharacterType) : AbstractComponent() {

    val charClass = SimpleObjectProperty<CharacterClass>(CharacterClass.MONSTER)

    val baseLevel = SimpleIntegerProperty(1)
    val attributes = AttributesComponent()

    val defaultWeapon = WeaponEntity(Data.Weapon.DRAGON_CLAW())

    var element = Element.NEUTRAL

    val rewardXP = Experience(0, 0, 0)

    val dropItems = ArrayList<Pair<Int, Int> >()

    lateinit var animation: DynamicAnimatedTexture

    fun withLevel(value: Int): CharacterDataComponent {
        baseLevel.value = value
        return this
    }

    fun withAttribute(attribute: Attribute, value: Int): CharacterDataComponent {
        attributes.setAttribute(attribute, value)
        return this
    }

    fun withElement(element: Element): CharacterDataComponent {
        this.element = element
        return this
    }

    fun withXP(base: Int, stat: Int, job: Int): CharacterDataComponent {
        rewardXP.base += base
        rewardXP.stat += stat
        rewardXP.job += job
        return this
    }

    fun withDrop(itemID: Int, chance: Int): CharacterDataComponent {
        dropItems.add(itemID.to(chance))
        return this
    }
}