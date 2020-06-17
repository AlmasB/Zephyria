package com.almasb.zeph.data

import com.almasb.zeph.item.*

/**
 * ID ranges:
 * Character [2000-2999].
 * Weapon [4000-4999].
 * Armor [5000-5999].
 * Usable [6000-6999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    // TODO: exclude (default items) hands, hat, clothes and shoes
    private val dbWeapons = hashMapOf<Int, WeaponData>()
    private val dbArmors = hashMapOf<Int, ArmorData>()
    private val dbUsableItems = hashMapOf<Int, UsableItemData>()

    val weapons by lazy { dbWeapons.values.toList() }
    val armors by lazy { dbArmors.values.toList() }

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
        // TODO: check no duplicate IDs
        Weapon.javaClass.declaredMethods.forEach {
            val data = it.invoke(Weapon) as WeaponData

            dbWeapons[data.description.id] = data
        }

        Armor.javaClass.declaredMethods.forEach {
            val data = it.invoke(Armor) as ArmorData

            dbArmors[data.description.id] = data
        }

        UsableItem.javaClass.declaredMethods.forEach {
            val data = it.invoke(UsableItem) as UsableItemData

            dbUsableItems[data.description.id] = data
        }
    }

    fun isWeapon(id: Int) = id.toString().startsWith("4")
    fun isArmor(id: Int) = id.toString().startsWith("5")

    fun getWeapon(id: Int) = dbWeapons[id] ?: throw IllegalArgumentException("No weapon found: $id")
    fun getArmor(id: Int) = dbArmors[id] ?: throw IllegalArgumentException("No armor found: $id")

    fun getDefaultArmor(id: Int): com.almasb.zeph.item.Armor {
        return when (id) {
            5000 -> hat
            5001 -> clothes
            5002 -> shoes
            else -> throw RuntimeException("")
        }
    }
}