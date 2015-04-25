package com.almasb.zeph.entity.control;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.combat.Effect;
import com.almasb.zeph.combat.Status;
import com.almasb.zeph.entity.GameCharacter.GameCharacterProperty;

public class GameCharacterControl extends AbstractControl {

    private IntegerProperty hp, hpMax;
    private IntegerProperty sp, spMax;

    private List<Status> statuses;
    private List<Effect> effects;

    @Override
    protected void initEntity(Entity entity) {
        hp = entity.getProperty(GameCharacterProperty.HP);
        sp = entity.getProperty(GameCharacterProperty.SP);
        hpMax = entity.getProperty(GameCharacterProperty.HP);
        spMax = entity.getProperty(GameCharacterProperty.SP);
        statuses = entity.getProperty(GameCharacterProperty.STATUSES);
        effects = entity.getProperty(GameCharacterProperty.EFFECTS);
    }

    @Override
    public void onUpdate(Entity entity, long now) {


    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public IntegerProperty hp() {
        return hp;
    }

    public IntegerProperty hpMax() {
        return hpMax;
    }

    public IntegerProperty sp() {
        return sp;
    }

    public IntegerProperty spMax() {
        return spMax;
    }
}
