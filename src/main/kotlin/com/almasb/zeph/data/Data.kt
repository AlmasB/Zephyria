package com.almasb.zeph.data

import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.item.*
import com.almasb.zeph.item.Armor
import com.almasb.zeph.item.Weapon
import com.almasb.zeph.skill.SkillData

/**
 * ID ranges:
 * Character [2000-2999].
 * Weapon [4000-4999].
 * Armor [5000-5999].
 * Usable [6000-6999].
 * Skill [7000-7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    private val dbSkills = hashMapOf<Int, SkillData>()
    // TODO: exclude (default items) hands, hat, clothes and shoes
    private val dbWeapons = hashMapOf<Int, WeaponData>()
    private val dbArmors = hashMapOf<Int, ArmorData>()
    private val dbUsableItems = hashMapOf<Int, UsableItemData>()

    private val dbCharacters = hashMapOf<Int, CharacterData>()

    @JvmField
    val Skills = Skills()

    @JvmField
    val UsableItems = UsableItems()

    @JvmField
    val Weapons = Weapons()

    @JvmField
    val Armors = Armors()

    @JvmField
    val Characters = Characters()

    val allSkillData by lazy { dbSkills.values.toList() }
    val allWeaponData by lazy { dbWeapons.values.toList() }
    val allArmorData by lazy { dbArmors.values.toList() }
    val allCharacterData by lazy { dbCharacters.values.toList() }






    // There is ever only one of these
    val hands by lazy { Weapon(getWeaponData(4000)) }
    val hat by lazy { Armor(getArmorData(5000)) }
    val clothes by lazy { Armor(getArmorData(5001)) }
    val shoes by lazy { Armor(getArmorData(5002)) }

    init {
        // TODO: check no duplicate IDs
        Weapons.javaClass.declaredMethods.forEach {
            val data = it.invoke(Weapons) as WeaponData

            dbWeapons[data.description.id] = data
        }

        Armors.javaClass.declaredMethods.forEach {
            val data = it.invoke(Armors) as ArmorData

            dbArmors[data.description.id] = data
        }

        UsableItems.javaClass.declaredMethods.forEach {
            val data = it.invoke(UsableItems) as UsableItemData

            dbUsableItems[data.description.id] = data
        }

        Characters.javaClass.declaredMethods.forEach {
            val data = it.invoke(Characters) as CharacterData

            dbCharacters[data.description.id] = data
        }
    }

    fun isCharacter(id: Int) = id.toString().startsWith("2")
    fun isWeapon(id: Int) = id.toString().startsWith("4")
    fun isArmor(id: Int) = id.toString().startsWith("5")

    fun getSkillData(id: Int) = dbSkills[id] ?: throw IllegalArgumentException("No skill found: $id")
    fun getCharacterData(id: Int) = dbCharacters[id] ?: throw IllegalArgumentException("No character found: $id")
    fun getWeaponData(id: Int) = dbWeapons[id] ?: throw IllegalArgumentException("No weapon found: $id")
    fun getArmorData(id: Int) = dbArmors[id] ?: throw IllegalArgumentException("No armor found: $id")
    fun getUsableItemData(id: Int) = dbUsableItems[id] ?: throw IllegalArgumentException("No usable item found: $id")
    fun getItemData(id: Int): ItemData = dbWeapons[id] ?: dbArmors[id] ?: dbUsableItems[id] ?: throw IllegalArgumentException("No weapon/armor/usable item found: $id")

    fun getDefaultArmor(id: Int): Armor {
        return when (id) {
            5000 -> hat
            5001 -> clothes
            5002 -> shoes
            else -> throw RuntimeException("")
        }
    }
}