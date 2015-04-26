package com.almasb.zeph.entity.control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.Player.PlayerProperty;
import com.almasb.zeph.entity.item.Armor.ArmorProperty;
import com.almasb.zeph.entity.item.ItemModifier;
import com.almasb.zeph.entity.item.Weapon.WeaponControl;
import com.almasb.zeph.entity.item.Weapon.WeaponProperty;
import com.almasb.zeph.entity.orion.Damage;

public class PlayerControl extends GameCharacterControl {

    private WeaponControl rightWeapon;
    private WeaponControl leftWeapon;

    @Override
    protected void initEntity(Entity entity) {
        super.initEntity(entity);
        rightWeapon = entity.<Entity>getProperty(PlayerProperty.ITEM_RIGHT_HAND).getControl(WeaponControl.class);
        leftWeapon = entity.<Entity>getProperty(PlayerProperty.ITEM_LEFT_HAND).getControl(WeaponControl.class);
    }

    @Override
    public void onUpdate(Entity entity, long now) {

    }

    public WeaponControl rightHand() {
        return rightWeapon;
    }

    public WeaponControl leftHand() {
        return leftWeapon;
    }

    @Override
    public Damage getDamage() {
        Damage left = leftWeapon.getDamage();
        Damage right = rightWeapon.getDamage();

        // we use all other properties from the main (right) weapon
        right.setValue(right.getValue() + left.getValue());

        return right;
    }

    @Override
    public Element getElement() {
        // TODO return element of body armor
        return Element.NEUTRAL;
    }

    public void equip(Entity item) {
        if (item.getType().equals(ArmorProperty.TYPE)) {
            // armor

            entity.setProperty(PlayerProperty.ITEM_BODY, item);
            // unequip effects
            // equip effects
        }
        else if (item.getType().equals(WeaponProperty.TYPE)) {

        }
    }

    public void applyItemModifier(ItemModifier mod) {

    }

    public void removeItemModifier(ItemModifier mod) {

    }
}
