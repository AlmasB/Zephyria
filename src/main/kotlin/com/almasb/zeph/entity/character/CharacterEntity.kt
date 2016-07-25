package com.almasb.zeph.entity.character

import com.almasb.ents.Component
import com.almasb.fxgl.entity.GameEntity
import com.almasb.zeph.Config
import com.almasb.zeph.combat.Element
import com.almasb.zeph.entity.DescriptionComponent
import com.almasb.zeph.entity.Inventory
import com.almasb.zeph.entity.character.component.*
import com.almasb.zeph.entity.character.control.CharacterControl
import com.almasb.zeph.entity.item.WeaponEntity
import com.almasb.zeph.entity.skill.SkillEntity
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(dataComponents: List<Component>) : GameEntity() {

    val charClass = SimpleObjectProperty<CharacterClass>(CharacterClass.MONSTER)

    val hp = HPComponent()
    val sp = SPComponent()

    val baseLevel = SimpleIntegerProperty(1)
    val attributes = AttributesComponent()
    val stats = StatsComponent()

    val weapon = SimpleObjectProperty<WeaponEntity>()

    val weaponElement = SimpleObjectProperty<Element>()
    val armorElement = SimpleObjectProperty<Element>()

    val inventory = Inventory()
    val skills = FXCollections.observableArrayList<SkillEntity>()

    val baseXP = SimpleIntegerProperty()
    val statXP = SimpleIntegerProperty()
    val jobXP = SimpleIntegerProperty()

    val description: DescriptionComponent
    val data: CharacterDataComponent

    val charConrol = CharacterControl()

    init {
        addComponent(attributes)
        addComponent(stats)

        addComponent(hp)
        addComponent(sp)

        dataComponents.forEach { addComponent(it) }

        description = getComponentUnsafe(DescriptionComponent::class.java)
        data = getComponentUnsafe(CharacterDataComponent::class.java)

        charClass.value = data.charClass

        data.attributes.forEach { attribute, value ->  attributes.setAttribute(attribute, value)}
        baseLevel.value = data.baseLevel

        weapon.value = data.defaultWeapon

        weaponElement.value = data.element
        armorElement.value = data.element

        addControl(charConrol)
    }

    fun getTileX() = positionComponent.x.toInt() / Config.tileSize

    fun getTileY() = positionComponent.y.toInt() / Config.tileSize

    /**
     * @return true if [target] is in weapon range of this character
     */
    fun isInWeaponRange(target: CharacterEntity) =
            this.positionComponent.distance(target.positionComponent) <= weapon.value.range * Config.tileSize
}