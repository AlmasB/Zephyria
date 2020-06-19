package com.almasb.zeph.character.components

import com.almasb.zeph.Config
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.EquipPlace
import com.almasb.zeph.character.PlayerEntity
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.data.Data
import com.almasb.zeph.item.*
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerComponent(charData: CharacterData) : CharacterComponent(charData) {

    init {
        charClass.value = CharacterClass.NOVICE
    }

    private lateinit var player: PlayerEntity

    // TODO: set to 0 when tests are done
    val attributePoints = SimpleIntegerProperty(90)
    val skillPoints = SimpleIntegerProperty(90)

    val money = SimpleIntegerProperty(Config.STARTING_MONEY)
    val weapon = SimpleObjectProperty<Weapon>()

    val statLevel = SimpleIntegerProperty(1)
    val jobLevel = SimpleIntegerProperty(1)

    override fun onAdded() {
        super.onAdded()

        player = entity as PlayerEntity

        EquipPlace.values().forEach {

            val item: Item

            if (it.isWeapon) {
                val weapon = Weapon(Data.getWeapon(it.emptyID))
                weapon.onEquip(player)

                item = weapon
            } else {
                val armor = Armor(Data.getArmor(it.emptyID))
                armor.onEquip(player)

                item = armor
            }

            equip[it] = item
            equipProperties[it] = SimpleObjectProperty(item)
        }

        equipProperty(EquipPlace.RIGHT_HAND).addListener { _, _, newWeapon ->
            weapon.value = newWeapon as Weapon
        }

        weapon.value = getRightWeapon()
    }

    fun rewardMoney(amount: Int) {
        money.value += amount
    }

    /**
     * Holds experience needed for each level
     */
    private val EXP_NEEDED_BASE = IntArray(Config.MAX_LEVEL_BASE)
    private val EXP_NEEDED_STAT = IntArray(Config.MAX_LEVEL_STAT)
    private val EXP_NEEDED_JOB = IntArray(Config.MAX_LEVEL_JOB)

    init {
        EXP_NEEDED_BASE[0] = Config.EXP_NEEDED_FOR_LEVEL2;
        EXP_NEEDED_STAT[0] = Config.EXP_NEEDED_FOR_LEVEL2;
        EXP_NEEDED_JOB[0] = Config.EXP_NEEDED_FOR_LEVEL2;

        for (i in 1..EXP_NEEDED_BASE.size - 1) {
            EXP_NEEDED_BASE[i] = (EXP_NEEDED_BASE[i - 1] * Config.EXP_NEEDED_INC_BASE + 2 * i).toInt();

            if (i < EXP_NEEDED_STAT.size)
                EXP_NEEDED_STAT[i] = (EXP_NEEDED_STAT[i - 1] * Config.EXP_NEEDED_INC_STAT + i).toInt();

            if (i < EXP_NEEDED_JOB.size)
                EXP_NEEDED_JOB[i] = (EXP_NEEDED_JOB[i - 1] * Config.EXP_NEEDED_INC_JOB + 3 * i).toInt();
        }
    }

    /**
     * Increases base [attribute].
     */
    fun increaseAttribute(attribute: Attribute) {
        if (attributePoints.value == 0)
            return

        val value = attributes.getBaseAttribute(attribute)
        if (value < Config.MAX_ATTRIBUTE) {
            attributes.setAttribute(attribute, value + 1)
            attributePoints.value--
        }
    }

    /**
     * Level up a skill with given skill [index].
     */
    fun increaseSkillLevel(index: Int) {
//        if (skillPoints.value == 0)
//            return
//
//        val skill = skills[index]
//
//        if (skill.level.value < Config.MAX_LEVEL_SKILL) {
//            skill.level.value++
//            skillPoints.value--
//
//            // apply passive skills immediately
//            if (skill.data.type == SkillType.PASSIVE && skill.level.value == 1) {
//                skill.data.onLearn(player, skill)
//            }
//        }
    }

    fun expNeededForNextBaseLevel() = EXP_NEEDED_BASE[baseLevel.value - 1]

    fun expNeededForNextStatLevel() = EXP_NEEDED_STAT[statLevel.value - 1]

    fun expNeededForNextJobLevel()  = EXP_NEEDED_JOB[jobLevel.value - 1]

    /**
     * Increases player's experience by [gainedXP].
     *
     * @return true if player gained new base level
     */
    fun rewardXP(gainedXP: Experience): Boolean {
        var baseLevelUp = false

        if (statLevel.value < Config.MAX_LEVEL_STAT) {
            statXP.value += gainedXP.stat

            if (statXP.value >= expNeededForNextStatLevel()) {
                statXP.value = 0
                statLevelUp();
            }
        }

        if (jobLevel.value < Config.MAX_LEVEL_JOB) {
            jobXP.value += gainedXP.job

            if (jobXP.value >= expNeededForNextJobLevel()) {
                jobXP.value = 0
                jobLevelUp();
            }
        }

        if (baseLevel.value < Config.MAX_LEVEL_BASE) {
            baseXP.value += gainedXP.base

            if (baseXP.value >= expNeededForNextBaseLevel()) {
                baseXP.value = 0
                baseLevelUp();
                baseLevelUp = true
            }
        }

        return baseLevelUp
    }

    private fun baseLevelUp() {
        baseLevel.value++

        hp.restorePercentageMax(100.0)
        sp.restorePercentageMax(100.0)
    }

    private fun statLevelUp() {
        statLevel.value++
        attributePoints.value += Config.ATTRIBUTE_POINTS_PER_LEVEL
    }

    private fun jobLevelUp() {
        jobLevel.value++
        skillPoints.value++
    }

    val equip = HashMap<EquipPlace, Item>()
    val equipProperties = HashMap<EquipPlace, ObjectProperty<Item> >()

    fun getEquip(place: EquipPlace) = equip[place]!!
    fun equipProperty(place: EquipPlace) = equipProperties[place]!!

    fun setEquip(place: EquipPlace, item: Item) {
        equip.put(place, item)
        equipProperties[place]!!.set(item)
    }

    fun getRightWeapon() = getEquip(EquipPlace.RIGHT_HAND) as Weapon
    fun getLeftWeapon() = getEquip(EquipPlace.LEFT_HAND) as Weapon

    fun equipWeapon(weapon: Weapon) {
        inventory.removeItem(weapon)

        if (weapon.type.isTwoHanded()) {

            if (30 - inventory.items.size == 1
                && !isFree(EquipPlace.RIGHT_HAND)
                && !isFree(EquipPlace.LEFT_HAND)) {
                // ex case, when inventory is full and player tries to equip 2H weapon
                // but holds two 1H weapons
                inventory.addItem(weapon)
                return
            }

            unEquipItem(EquipPlace.RIGHT_HAND)
            unEquipItem(EquipPlace.LEFT_HAND)
            setEquip(EquipPlace.RIGHT_HAND, weapon)
            setEquip(EquipPlace.LEFT_HAND, weapon)

        } else if (weapon.type == WeaponType.SHIELD || !isFree(EquipPlace.RIGHT_HAND)) {
            unEquipItem(EquipPlace.LEFT_HAND)
            setEquip(EquipPlace.LEFT_HAND, weapon)
        } else {    // normal 1H weapon
            unEquipItem(EquipPlace.RIGHT_HAND)
            setEquip(EquipPlace.RIGHT_HAND, weapon)
        }

        weapon.onEquip(player)
        weaponElement.value = weapon.element.value
    }

    fun equipArmor(armor: Armor) {
        inventory.removeItem(armor)

        val place = when (armor.type) {
            ArmorType.BODY -> EquipPlace.BODY
            ArmorType.HELM -> EquipPlace.HELM
            ArmorType.SHOES -> EquipPlace.SHOES
        }

        unEquipItem(place)
        setEquip(place, armor)
        armor.onEquip(player)
        armorElement.value = armor.element.value
    }

    fun unEquipItem(place: EquipPlace) {
        if (isFree(place) || inventory.isFull())
            return

        val item = getEquip(place)

        if (item is Weapon) {
            if (item.type.isTwoHanded()) {
                if (place == EquipPlace.RIGHT_HAND)
                    setEquip(EquipPlace.LEFT_HAND, Data.hands)
                else
                    setEquip(EquipPlace.RIGHT_HAND, Data.hands)
            }

            item.onUnEquip(player)
        } else if (item is Armor) {
            item.onUnEquip(player)
        }

        inventory.addItem(item)

        // replace with default
        if (place.isWeapon) {
            setEquip(place, Data.hands)
        } else {
            setEquip(place, Data.getDefaultArmor(place.emptyID))
        }
    }

    fun isFree(place: EquipPlace) = getEquip(place).description.id == place.emptyID

    // TODO: player version of canAttack that uses aspd of both weapons
}