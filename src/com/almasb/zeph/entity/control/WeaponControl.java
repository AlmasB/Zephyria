package com.almasb.zeph.entity.control;

import javafx.beans.property.IntegerProperty;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.item.Weapon.WeaponProperty;

public class WeaponControl extends AbstractControl {

    private IntegerProperty damage;
    private IntegerProperty refineLevel;

    @Override
    protected void initEntity(Entity entity) {
        damage = entity.getProperty(WeaponProperty.DAMAGE);
        refineLevel = entity.getProperty(WeaponProperty.REFINE_LEVEL);
    }

    @Override
    public void onUpdate(Entity entity, long now) {
        // TODO Auto-generated method stub

    }

    public IntegerProperty damage() {
        return damage;
    }

    public IntegerProperty refineLevel() {
        return refineLevel;
    }
}
