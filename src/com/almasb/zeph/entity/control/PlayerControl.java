package com.almasb.zeph.entity.control;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.Player.PlayerProperty;

public class PlayerControl extends AbstractControl {

    private WeaponControl rightWeapon;

    @Override
    protected void initEntity(Entity entity) {
        rightWeapon = entity.<Entity>getProperty(PlayerProperty.ITEM_RIGHT_HAND).getControl(WeaponControl.class);
    }

    @Override
    public void onUpdate(Entity entity, long now) {

    }

    public WeaponControl rightHand() {
        return rightWeapon;
    }
}
