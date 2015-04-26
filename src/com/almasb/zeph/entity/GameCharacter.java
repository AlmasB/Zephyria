package com.almasb.zeph.entity;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.combat.Effect;
import com.almasb.zeph.combat.Status;
import com.almasb.zeph.entity.control.GameCharacterControl;

public abstract class GameCharacter {

    public enum GameCharacterProperty implements PropertyKey {
        ID, NAME, DESCRIPTION,
        HP, SP, HP_MAX, SP_MAX, HP_REGEN, SP_REGEN,
        ATK, MATK, DEF, MDEF, ARM, MARM, ASPD, MSPD, CRIT_CHANCE, MCRIT_CHANCE, CRIT_DMG, MCRIT_DMG,
        LEVEL,
        STATUSES, EFFECTS
    }

    private int id;
    private String name;
    private String description;
    private String textureName;

    // BEGIN TEST DATA
    private int hp = 100, hpMax = 100, hpRegen = 2;
    private int sp = 25, spMax = 25, spRegen = 1;

    private int atk = 10, matk = 10;
    private int def = 3, mdef = 5;
    private int arm = 4, marm = 3;
    private int aspd = 1, mspd = 1;
    private int critChance = 0, mcritChance = 0;
    private int critDmg = 10, mcritDmg = 15;

    private int level = 1;

    private List<Status> statuses = new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();

    // END TEST DATA

    public Entity toEntity() {
        Entity entity = new Entity("");
        entity.setProperty(GameCharacterProperty.ID, id)
            .setProperty(GameCharacterProperty.NAME, name)
            .setProperty(GameCharacterProperty.DESCRIPTION, description)
            .setProperty(GameCharacterProperty.HP, new SimpleIntegerProperty(hp))
            .setProperty(GameCharacterProperty.SP, new SimpleIntegerProperty(sp))
            .setProperty(GameCharacterProperty.HP_MAX, new SimpleIntegerProperty(hpMax))
            .setProperty(GameCharacterProperty.SP_MAX, new SimpleIntegerProperty(spMax))
            .setProperty(GameCharacterProperty.HP_REGEN, new SimpleIntegerProperty(hpRegen))
            .setProperty(GameCharacterProperty.SP_REGEN, new SimpleIntegerProperty(spRegen))
            .setProperty(GameCharacterProperty.LEVEL, new SimpleIntegerProperty(level))
            .setProperty(GameCharacterProperty.STATUSES, new ArrayList<>(statuses))
            .setProperty(GameCharacterProperty.EFFECTS, new ArrayList<>(effects));

        return entity;
    }
}
