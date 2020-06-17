package com.almasb.zeph.data

import com.almasb.zeph.character.char
import com.almasb.zeph.combat.Element.*
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.item.*
import com.almasb.zeph.item.ArmorType.*
import com.almasb.zeph.item.WeaponType.*


/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    private val weapons = hashMapOf<Int, WeaponData>()
    private val armors = hashMapOf<Int, ArmorData>()

    // There is ever only one of these
    val hands by lazy { Weapon(getWeapon(4000)) }
    val hat by lazy { Armor(getArmor(5000)) }
    val clothes by lazy { Armor(getArmor(5001)) }
    val shoes by lazy { Armor(getArmor(5002)) }

    @JvmField
    val UsableItem = com.almasb.zeph.data.UsableItem()

    @JvmField
    val Weapon = com.almasb.zeph.data.Weapon()

    @JvmField
    val Armor = com.almasb.zeph.data.Armor()

    @JvmField
    val Character = com.almasb.zeph.data.Character()

    init {
        Weapon.javaClass.declaredMethods.forEach {
            val data = it.invoke(Weapon) as WeaponData

            weapons[data.description.id] = data
        }

        Armor.javaClass.declaredMethods.forEach {
            val data = it.invoke(Armor) as ArmorData

            armors[data.description.id] = data
        }
    }

    fun isWeapon(id: Int) = id.toString().startsWith("4")
    fun isArmor(id: Int) = id.toString().startsWith("5")

    fun getWeapon(id: Int) = weapons[id] ?: throw IllegalArgumentException("No weapon found: $id")
    fun getArmor(id: Int) = armors[id] ?: throw IllegalArgumentException("No armor found: $id")

    fun getDefaultArmor(id: Int): com.almasb.zeph.item.Armor {
        return when (id) {
            5000 -> hat
            5001 -> clothes
            5002 -> shoes
            else -> throw RuntimeException("")
        }
    }
}