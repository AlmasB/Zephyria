package com.almasb.zeph.entity.item.component

import com.almasb.ents.Entity
import com.almasb.zeph.entity.item.component.EquippableComponent
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.entity.character.component.StatsComponent
import com.almasb.zeph.entity.item.ItemLevel
import com.almasb.zeph.entity.item.WeaponType

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class WeaponDataComponent(itemLevel: ItemLevel, val type: WeaponType, val pureDamage: Int) : EquippableComponent(itemLevel) {

    fun fullDamage(): Int {
        return pureDamage + refineLevel * (if (refineLevel > 2) itemLevel.bonus + 5 else itemLevel.bonus)
    }

    override fun onEquip(entity: Entity) {
        super.onEquip(entity)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ATK, fullDamage())
    }

    override fun onUnEquip(entity: Entity) {
        super.onUnEquip(entity)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ATK, -fullDamage())
    }
}