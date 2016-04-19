package com.almasb.zeph.entity

import com.almasb.ents.Component
import com.almasb.ents.Entity
import com.almasb.zeph.entity.item.ArmorEntity
import com.almasb.zeph.entity.item.WeaponEntity
import java.lang.reflect.Method
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object EntityManager {

    private val weapons = HashMap<Int, Method>()
    private val armor = HashMap<Int, Method>()

    fun getWeapon(id: Int) = WeaponEntity(weapons[id]!!.invoke(Data.Weapon) as List<Component>)

    fun getArmor(id: Int) = ArmorEntity(armor[id]!!.invoke(Data.Armor) as List<Component>)

    fun getItem(id: Int): Entity {
        if (weapons.containsKey(id))
            return getWeapon(id)

        if (armor.containsKey(id))
            return getArmor(id)

        throw IllegalArgumentException("ID $id not found in the database")
    }

    init {
        Data.Weapon.javaClass.declaredMethods.forEach {
            val list = it.invoke(Data.Weapon) as List<Component>

            val id = (list[0] as DescriptionComponent).id
            weapons[id] = it
        }

        Data.Armor.javaClass.declaredMethods.forEach {
            val list = it.invoke(Data.Armor) as List<Component>

            val id = (list[0] as DescriptionComponent).id
            armor[id] = it
        }
    }
}