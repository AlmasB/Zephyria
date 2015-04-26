package com.almasb.zeph.entity.control;

import java.util.List;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;

import com.almasb.fxgl.GameApplication;
import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.combat.Effect;
import com.almasb.zeph.combat.Status;
import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.GameCharacter.GameCharacterProperty;
import com.almasb.zeph.entity.orion.Damage;

public abstract class GameCharacterControl extends AbstractControl {

    /* RAW STATS */
    private IntegerProperty hp, hpMax, hpRegen;
    private IntegerProperty sp, spMax, spRegen;

    private IntegerProperty atk, matk;
    private IntegerProperty def, mdef;
    private IntegerProperty arm, marm;
    private IntegerProperty aspd, mspd;
    private IntegerProperty critChance, mcritChance;
    private IntegerProperty critDmg, mcritDmg;

    /* BONUSES */
    protected IntegerProperty b_hpMax, b_hpRegen;
    protected IntegerProperty b_spMax, b_spRegen;

    protected IntegerProperty b_atk, b_matk;
    protected IntegerProperty b_def, b_mdef;
    protected IntegerProperty b_arm, b_marm;
    protected IntegerProperty b_aspd, b_mspd;
    protected IntegerProperty b_critChance, b_mcritChance;
    protected IntegerProperty b_critDmg, b_mcritDmg;


    private IntegerProperty level;

    private List<Status> statuses;
    private List<Effect> effects;

    private long timeRegen = 0;

    private boolean canAttack = true;

    @Override
    protected void initEntity(Entity entity) {
        hp = entity.getProperty(GameCharacterProperty.HP);
        sp = entity.getProperty(GameCharacterProperty.SP);
        hpMax = entity.getProperty(GameCharacterProperty.HP_MAX);
        spMax = entity.getProperty(GameCharacterProperty.SP_MAX);
        hpRegen = entity.getProperty(GameCharacterProperty.HP_REGEN);
        spRegen = entity.getProperty(GameCharacterProperty.SP_REGEN);

        // TODO: init other fields

        level = entity.getProperty(GameCharacterProperty.LEVEL);

        statuses = entity.getProperty(GameCharacterProperty.STATUSES);
        effects = entity.getProperty(GameCharacterProperty.EFFECTS);
    }

    @Override
    public void onUpdate(Entity entity, long now) {
        if (now - timeRegen >= 2 * GameApplication.SECOND) {
            regen();
            timeRegen = now;
        }

        // TODO: attack tick
        // TODO: update effects
        // TODO: update statuses
    }

    private void regen() {
        if (!statuses.contains(Status.POISONED)) {
            int nextHP = Math.min(hpMax.get(), hp.get() + hpRegen.get());
            int nextSP = Math.min(spMax.get(), sp.get() + spRegen.get());
            hp.set(nextHP);
            sp.set(nextSP);
        }
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

    public NumberBinding hpMaxTotal() {
        return hpMax.add(b_hpMax);
    }

    public IntegerProperty sp() {
        return sp;
    }

    public IntegerProperty spMax() {
        return spMax;
    }

    public NumberBinding spMaxTotal() {
        return spMax.add(b_spMax);
    }

    public void levelUp() {
        level.set(level.get() + 1);
        hpMax.set(hpMax.get() + 20 * level.get());
        spMax.set(spMax.get() + 11 * level.get());


//        private IntegerProperty hp, hpMax, hpRegen;
//        private IntegerProperty sp, spMax, spRegen;
//
//        private IntegerProperty atk, matk;
//        private IntegerProperty def, mdef;
//        private IntegerProperty arm, marm;
//        private IntegerProperty aspd, mspd;
//        private IntegerProperty critChance, mcritChance;
//        private IntegerProperty critDmg, mcritDmg;
//
//        private IntegerProperty level;
    }

    public abstract Damage getDamage();
    public abstract Element getElement();

    public Damage attack(GameCharacterControl other) {
        Damage damage = getDamage();
        return dealDamage(damage, other);
    }

    /**
     *
     * @param damage
     * @param other
     * @return damage actually dealt, or Damage.NULL if attacker cannot perform the attack
     */
    public Damage dealDamage(Damage damage, GameCharacterControl other) {
        if (statuses.contains(Status.STUNNED))
            return Damage.NULL;

        Element element = other.getElement();
        // TODO: actual damage
        return new Damage();
    }
}
