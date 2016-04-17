package com.almasb.zeph.entity.character.control

import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.entity.Data
import com.almasb.zeph.entity.character.EquipPlace
import com.almasb.zeph.entity.character.PlayerEntity
import com.almasb.zeph.entity.character.component.AttributesComponent
import com.almasb.zeph.entity.character.component.PlayerDataComponent
import com.almasb.zeph.entity.item.ArmorEntity
import com.almasb.zeph.entity.item.WeaponEntity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.SimpleObjectProperty
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerControl : CharacterControl() {

    private lateinit var data: PlayerDataComponent
    private lateinit var player: PlayerEntity

    override fun onAdded(entity: Entity) {
        super.onAdded(entity)

        player = entity as PlayerEntity
        data = entity.getComponentUnsafe(PlayerDataComponent::class.java)
    }

    fun rewardMoney(amount: Int) {
        data.money.value += amount
    }

    /**
     * Gameplay constants
     */
    private val MAX_LEVEL_BASE = 100
    private val MAX_LEVEL_STAT = 100
    private val MAX_LEVEL_JOB = 60
    private val MAX_ATTRIBUTE = 100
    private val ATTRIBUTE_POINTS_PER_LEVEL = 3

    /**
     * Holds experience needed for each level
     */
    private val EXP_NEEDED_BASE = IntArray(MAX_LEVEL_BASE)
    private val EXP_NEEDED_STAT = IntArray(MAX_LEVEL_STAT)
    private val EXP_NEEDED_JOB = IntArray(MAX_LEVEL_JOB)

//    static
//    {
//        /**
//         * By what value should experience needed for next level
//         * increase per level
//         */
//        float EXP_NEEDED_INC_BASE = 1.75f;
//        float EXP_NEEDED_INC_STAT = 1.5f;
//        float EXP_NEEDED_INC_JOB  = 2.25f;
//
//        int EXP_NEEDED_FOR_LEVEL2 = 10;
//
//        EXP_NEEDED_BASE[0] = EXP_NEEDED_FOR_LEVEL2;
//        EXP_NEEDED_STAT[0] = EXP_NEEDED_FOR_LEVEL2;
//        EXP_NEEDED_JOB[0] = EXP_NEEDED_FOR_LEVEL2;
//        for (int i = 1; i < EXP_NEEDED_BASE.length; i++) {
//        EXP_NEEDED_BASE[i] = int (EXP_NEEDED_BASE[i - 1] * EXP_NEEDED_INC_BASE) + 2 * i;
//
//        if (i < EXP_NEEDED_STAT.length)
//            EXP_NEEDED_STAT[i] = int (EXP_NEEDED_STAT[i - 1] * EXP_NEEDED_INC_STAT) + i;
//
//        if (i < EXP_NEEDED_JOB.length)
//            EXP_NEEDED_JOB[i] = int (EXP_NEEDED_JOB[i - 1] * EXP_NEEDED_INC_JOB) + 3 * i;
//    }
//    }




    /**
     * Increases base attribute.

     * @param attr
     */
    fun increaseAttr(attr: Attribute) {


        getEntity().getComponentUnsafe(AttributesComponent::class.java)
            .setAttribute(attr, getEntity().getComponentUnsafe(AttributesComponent::class.java).getBaseAttribute(attr) + 1)


        //        if (getAttributePoints() == 0)
        //            return;
        //
        //        int value = attributes.getBaseAttribute(attr);
        //        if (value < MAX_ATTRIBUTE) {
        //            //attributes.setAttribute(attr, value + 1);
        //            setAttributePoints(getAttributePoints() - 1);
        //        }
    }


    //  public void increaseSkillLevel(int skillCode) {
    //  if (skillCode >= skills.length)
    //      return;
    //
    //  if (skills[skillCode].levelUp())
    //      skillPoints--;
    //}

    //    public boolean hasAttributePoints() {
    //        return attributePoints > 0;
    //    }
    //
    //    public boolean hasSkillPoints() {
    //        return skillPoints > 0;
    //    }


//    /**
//     * Player experience.
//     */
//    private val xp = Experience(0, 0, 0)
//    @Transient private val baseXPProperty = ReadOnlyIntegerWrapper(0)
//    @Transient private val statXPProperty = ReadOnlyIntegerWrapper(0)
//    @Transient private val jobXPProperty = ReadOnlyIntegerWrapper(0)
//
//    fun baseXPProperty(): ReadOnlyIntegerProperty {
//        return baseXPProperty.readOnlyProperty
//    }
//
//    fun statXPProperty(): ReadOnlyIntegerProperty {
//        return statXPProperty.readOnlyProperty
//    }
//
//    fun jobXPProperty(): ReadOnlyIntegerProperty {
//        return jobXPProperty.readOnlyProperty
//    }
//
//    fun expNeededForNextBaseLevel(): Int {
//        return EXP_NEEDED_BASE[baseLevel.level - 1]
//    }
//
//    fun expNeededForNextStatLevel(): Int {
//        return EXP_NEEDED_STAT[statLevel.getLevel() - 1]
//    }
//
//    fun expNeededForNextJobLevel(): Int {
//        return EXP_NEEDED_JOB[jobLevel.getLevel() - 1]
//    }
//
//    /**
//     * Increases player's experience.
//     * TODO: check against MAX LEVELS
//
//     * @param gainedXP
//     * *
//     * @return
//     * *          true if player gained new base level
//     */
//    fun rewardXP(gainedXP: Experience): Boolean {
//        var baseLevelUp = false
//
//        xp.add(gainedXP)
//        if (xp.stat >= EXP_NEEDED_STAT[statLevel.getLevel() - 1]) {
//            //statLevelUp();
//            xp.stat = 0
//        }
//        if (xp.job >= EXP_NEEDED_JOB[jobLevel.getLevel() - 1]) {
//            //jobLevelUp();
//            xp.job = 0
//        }
//        if (xp.base >= expNeededForNextBaseLevel()) {
//            //baseLevelUp();
//            xp.base = 0
//            baseLevelUp = true
//        }
//
//        baseXPProperty.set(xp.base)
//        statXPProperty.set(xp.stat)
//        jobXPProperty.set(xp.job)
//
//        return baseLevelUp
//    }












//        /**
//         * Equipped gear
//         */
//        private Map<EquipPlace, EquippableItem> equip = new HashMap<>();
//        // TODO: make read only
//        private transient Map<EquipPlace, ObjectProperty<EquippableItem> > equipProperties = new HashMap<>();
//
//        public final ObjectProperty<EquippableItem> equipProperty(EquipPlace place) {
//            return equipProperties.get(place);
//        }
    //
    //    private void setEquip(EquipPlace place, EquippableItem item) {
    //        equip.put(place, item);
    //        equipProperties.get(place).set(item);
    //    }
    //
    //    /**
    //     * Constructs player with given in-game name and character class.
    //     *
    //     * @param name
    //     * @param charClass
    //     */
    //    public PlayerControl(String name, GameCharacterClass charClass) {
    //        //super(1000, name, "Player", "player.png", charClass);
    //
    //        for (EquipPlace p : EquipPlace.values()) {
    //            EquippableItem item = (EquippableItem) EntityManager.getItemByID(p.emptyID);
    //            item.onEquip(this);
    //            equip.put(p, item);
    //            equipProperties.put(p, new SimpleObjectProperty<>(item));
    //        }
    //    }
    //
    //    private void baseLevelUp() {
    //        baseLevel.incLevel();
    //
    //        hp.restorePercentageMax(100);
    //        sp.restorePercentageMax(100);
    //    }
    //
    //    private void statLevelUp() {
    //        statLevel.incLevel();
    //        setAttributePoints(getAttributePoints() + ATTRIBUTE_POINTS_PER_LEVEL);
    //    }
    //
    //    private void jobLevelUp() {
    //        jobLevel.incLevel();
    //        setSkillPoints(getSkillPoints() + 1);
    //    }
    //
    //    @Override
    //    public final boolean canAttack() {
    //        Weapon w1 = (Weapon) getEquip(EquipPlace.RIGHT_HAND);
    //        Weapon w2 = (Weapon) getEquip(EquipPlace.LEFT_HAND);
    //
    //        return getAtkTick() >= 50 / (1 + stats.getTotalStat(Stat.ASPD) *w1.type.aspdFactor*w2.type.aspdFactor/100.0f);
    //    }
    //

    val equip = HashMap<EquipPlace, Entity>()
    val equipProperties = HashMap<EquipPlace, ObjectProperty<Entity> >()

    fun getEquip(place: EquipPlace) = equip[place]
    fun equipProperty(place: EquipPlace) = equipProperties[place]

    init {
        EquipPlace.values().forEach {
            val item = WeaponEntity(Data.Weapon.HANDS())
            equip.put(it, item)
            equipProperties.put(it, SimpleObjectProperty(item))
        }
    }


    fun equipWeapon(weapon: WeaponEntity) {
        // remove item from inventory to clear space
        player.inventory.removeItem(weapon)



        weapon.data.onEquip(player)
    }

    fun equipArmor(armor: ArmorEntity) {

    }

    fun unEquipItem(place: EquipPlace) {

    }













    //    public final void equipWeapon(Weapon w) {
    //        inventory.removeItem(w);    // remove item from inventory to clear space
    //
    //        if (w.type.ordinal() >= WeaponType.TWO_H_SWORD.ordinal()) {
    //            if (Inventory.MAX_SIZE - inventory.size() == 1
    //                    && !isFree(EquipPlace.RIGHT_HAND)
    //                    && !isFree(EquipPlace.LEFT_HAND)) {
    //                // ex case, when inventory is full and player tries to equip 2H weapon
    //                // but holds two 1H weapons
    //                inventory.addItem(w);
    //                return;
    //            }
    //            unEquipItem(EquipPlace.RIGHT_HAND);
    //            unEquipItem(EquipPlace.LEFT_HAND);
    //            setEquip(EquipPlace.RIGHT_HAND, w);
    //            setEquip(EquipPlace.LEFT_HAND, w);
    //        }
    //        else if (w.type == WeaponType.SHIELD || !isFree(EquipPlace.RIGHT_HAND)) {
    //            unEquipItem(EquipPlace.LEFT_HAND);
    //            setEquip(EquipPlace.LEFT_HAND, w);
    //        }
    //        else {  // normal 1H weapon
    //            unEquipItem(EquipPlace.RIGHT_HAND);
    //            setEquip(EquipPlace.RIGHT_HAND, w);
    //        }
    //
    //        w.onEquip(this);            // put it on
    //    }
    //
    //    public final void equipArmor(Armor a) {
    //        inventory.removeItem(a);    // remove it first, so we can unequip our armor
    //
    //        EquipPlace place;
    //        switch (a.type) {
    //            case BODY:
    //                place = EquipPlace.BODY;
    //                break;
    //            case HELM:
    //                place = EquipPlace.HELM;
    //                break;
    //            case SHOES:
    //            default:
    //                place = EquipPlace.SHOES;
    //                break;
    //        }
    //
    //        unEquipItem(place);
    //        setEquip(place, a);
    //        a.onEquip(this);
    //    }
    //
    //    public final void unEquipItem(EquipPlace itemPlace) {
    //        if (isFree(itemPlace) || inventory.isFull())
    //            return; // no item at this place or inventory is full
    //
    //        EquippableItem item = getEquip(itemPlace);
    //
    //        if (item instanceof Weapon) {
    //            Weapon w = (Weapon) item;
    //            if (w.type.ordinal() >= WeaponType.TWO_H_SWORD.ordinal()) { // if 2 handed
    //                if (itemPlace == EquipPlace.RIGHT_HAND)
    //                    setEquip(EquipPlace.LEFT_HAND, EntityManager.getWeaponByID(ID.Weapon.HANDS));
    //                else
    //                    setEquip(EquipPlace.RIGHT_HAND, EntityManager.getWeaponByID(ID.Weapon.HANDS));
    //            }
    //        }
    //
    //        item.onUnEquip(this);   // take item off
    //        inventory.addItem(item);    // put it in inventory
    //
    //        setEquip(itemPlace, (EquippableItem) EntityManager.getItemByID(itemPlace.emptyID));    // replace with default
    //    }
    //
    //    /**
    //     *
    //     * @param place
    //     * @return true if the slot at given place is free to put gear on
    //     */
    //    public final boolean isFree(EquipPlace place) {
    //        return getEquip(place).getID() == place.emptyID;
    //    }
    //
    //    public final EquippableItem getEquip(EquipPlace place) {
    //        return equip.get(place);
    //    }
    //
    //    @Override
    //    public final Element getWeaponElement() {
    //        return getEquip(EquipPlace.RIGHT_HAND).getElement();
    //    }
    //
    //    @Override
    //    public final Element getArmorElement() {
    //        return getEquip(EquipPlace.BODY).getElement();
    //    }
}