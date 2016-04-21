package com.almasb.zeph.entity.item.component

import com.almasb.ents.Entity
import com.almasb.zeph.combat.Rune
import com.almasb.zeph.combat.Stat
import com.almasb.zeph.entity.character.component.StatsComponent
import com.almasb.zeph.entity.item.component.EquippableComponent
import com.almasb.zeph.entity.item.ArmorType
import com.almasb.zeph.entity.item.ItemLevel

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ArmorDataComponent(itemLevel: ItemLevel, val armorType: ArmorType, val armor: Int, val marmor: Int) : EquippableComponent(itemLevel) {

    fun armorRating(): Int {
        return armor + refineLevel * if (refineLevel > 2) itemLevel.bonus + 1 else itemLevel.bonus
    }

    fun marmorRating(): Int {
        return marmor + refineLevel * if (refineLevel > 2) itemLevel.bonus + 1 else itemLevel.bonus
    }

    override fun onEquip(entity: Entity) {
        super.onEquip(entity)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, armorRating())
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, marmorRating())
    }

    override fun onUnEquip(entity: Entity) {
        super.onUnEquip(entity)
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.ARM, -armorRating())
        entity.getComponentUnsafe(StatsComponent::class.java).addBonusStat(Stat.MARM, -marmorRating())
    }

    fun withRune(rune: Rune): ArmorDataComponent {
        addRune(rune)
        return this
    }

    // TODO: data binding since armor ratings can change realtime
    override fun toString(): String {
        return "Armor: ${armorRating()}% \n MArmor: ${marmorRating()}% \n $runes"
    }
}