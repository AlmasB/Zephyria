package com.almasb.zeph.data

import com.almasb.fxgl.core.reflect.ReflectionUtils
import com.almasb.fxgl.logging.Logger
import com.almasb.zeph.Description
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.item.*
import com.almasb.zeph.quest.QuestData
import com.almasb.zeph.skill.SkillData

/**
 * ID ranges:
 * Character [2000-2499].
 * NPCs [2500-2999].
 * Weapon [4000-4999].
 * Armor [5000-5999].
 * Usable [6000-6499].
 * Misc [6500-6999].
 * Skill [7000-7499].
 * Quests [7500-7999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Data {

    private val log = Logger.get(javaClass)

    private val dbSkills = hashMapOf<Int, SkillData>()

    private val dbWeapons = hashMapOf<Int, WeaponData>()
    private val dbArmors = hashMapOf<Int, ArmorData>()
    private val dbUsableItems = hashMapOf<Int, UsableItemData>()
    private val dbMiscItems = hashMapOf<Int, MiscItemData>()

    private val dbMonsters = hashMapOf<Int, CharacterData>()
    private val dbNPCs = hashMapOf<Int, NPCData>()

    private val dbQuests = hashMapOf<Int, QuestData>()

    @JvmField
    val Skills = Skills()

    @JvmField
    val UsableItems = UsableItems()

    @JvmField
    val MiscItems = MiscItems()

    @JvmField
    val Weapons = Weapons()

    @JvmField
    val Armors = Armors()

    @JvmField
    val Characters = Characters()

    @JvmField
    val NPCs = NPCs()

    @JvmField
    val Quests = Quests()

    val allSkillData by lazy { dbSkills.values.toList() }
    val allWeaponData by lazy { dbWeapons.values.toList() }
    val allArmorData by lazy { dbArmors.values.toList() }
    val allCharacterData by lazy { dbMonsters.values.toList() }
    val allQuestData by lazy { dbQuests.values.toList() }

    // There is ever only one of these
    val hands by lazy { Weapon(Weapons.Maces.HANDS) }
    val hat by lazy { Armor(Armors.Helm.HAT) }
    val clothes by lazy { Armor(Armors.Body.CLOTHES) }
    val shoes by lazy { Armor(Armors.Shoes.SIMPLE_SHOES) }

    init {
        try {
            populate(dbWeapons, Weapons.Maces)
            populate(dbWeapons, Weapons.OneHandedSwords)
            populate(dbWeapons, Weapons.OneHandedAxes)
            populate(dbWeapons, Weapons.Daggers)
            populate(dbWeapons, Weapons.Spears)
            populate(dbWeapons, Weapons.Rods)
            populate(dbWeapons, Weapons.Shields)
            populate(dbWeapons, Weapons.TwoHandedSwords)
            populate(dbWeapons, Weapons.TwoHandedAxes)
            populate(dbWeapons, Weapons.Katars)
            populate(dbWeapons, Weapons.Bows)

            populate(dbArmors, Armors.Helm)
            populate(dbArmors, Armors.Body)
            populate(dbArmors, Armors.Shoes)

            populate(dbUsableItems, UsableItems)
            populate(dbMiscItems, MiscItems)

            populate(dbSkills, Skills.Warrior)
            populate(dbSkills, Skills.Crusader)
            populate(dbSkills, Skills.Gladiator)
            populate(dbSkills, Skills.Mage)
            populate(dbSkills, Skills.Wizard)
            populate(dbSkills, Skills.Enchanter)
            populate(dbSkills, Skills.Scout)
            populate(dbSkills, Skills.Rogue)
            populate(dbSkills, Skills.Ranger)

            populate(dbMonsters, Characters)

            populate(dbNPCs, NPCs)

            populate(dbQuests, Quests)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // exclude (default items) hands, hat, clothes and shoes

        dbWeapons.remove(Weapons.Maces.HANDS.description.id)
        dbArmors.remove(Armors.Helm.HAT.description.id)
        dbArmors.remove(Armors.Shoes.SIMPLE_SHOES.description.id)
        dbArmors.remove(Armors.Body.CLOTHES.description.id)
    }

    private fun <T> populate(db: MutableMap<Int, T>, dataObject: Any) {
        dataObject.javaClass.declaredFields.forEach {
            it.isAccessible = true

            val data = it.get(dataObject)

            val fieldDesc = ReflectionUtils.findFieldsByTypeRecursive(data, Description::class.java)[0].also { it.isAccessible = true }
            val desc = fieldDesc.get(data) as Description

            if (db.containsKey(desc.id)) {
                throw RuntimeException("$desc has the same id as ${db[desc.id]}")
            } else {
                db[desc.id] = data as T
            }
        }
    }

    fun isMonster(id: Int) = id in 2000..2499
    fun isNPC(id: Int) = id in 2500..2999
    fun isWeapon(id: Int) = id.toString().startsWith("4")
    fun isArmor(id: Int) = id.toString().startsWith("5")

    fun getSkillData(id: Int) = dbSkills[id] ?: throw IllegalArgumentException("No skill found: $id")
    fun getCharacterData(id: Int) = dbMonsters[id] ?: throw IllegalArgumentException("No character found: $id")
    fun getNPCData(id: Int) = dbNPCs[id] ?: throw IllegalArgumentException("No NPC found: $id")
    fun getWeaponData(id: Int) = dbWeapons[id] ?: throw IllegalArgumentException("No weapon found: $id")
    fun getArmorData(id: Int) = dbArmors[id] ?: throw IllegalArgumentException("No armor found: $id")
    fun getUsableItemData(id: Int) = dbUsableItems[id] ?: throw IllegalArgumentException("No usable item found: $id")
    fun getMiscItemData(id: Int) = dbMiscItems[id] ?: throw IllegalArgumentException("No misc item found: $id")

    fun getItemData(id: Int): ItemData =
            dbWeapons[id]
            ?: dbArmors[id]
            ?: dbUsableItems[id]
            ?: dbMiscItems[id]
            ?: throw IllegalArgumentException("No weapon/armor/usable/misc item found: $id")

    fun getDefaultWeapon(id: Int): Weapon {
        return hands
    }

    fun getDefaultArmor(id: Int): Armor {
        return when (id) {
            5000 -> hat
            5001 -> clothes
            5002 -> shoes
            else -> throw RuntimeException("$id is not default armor id")
        }
    }
}