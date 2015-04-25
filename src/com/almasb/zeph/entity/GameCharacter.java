package com.almasb.zeph.entity;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.combat.Effect;
import com.almasb.zeph.combat.Status;
import com.almasb.zeph.entity.control.GameCharacterControl;

public class GameCharacter {

    public enum GameCharacterProperty implements PropertyKey {
        ID, NAME, DESCRIPTION,
        HP, SP, HP_MAX, SP_MAX, STATUSES, EFFECTS
    }

    private int id;
    private String name;
    private String description;
    private String textureName;

    // test data
    private int hp = 100, hpMax = 100;
    private int sp = 25, spMax = 25;

    private List<Status> statuses = new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();

    public Entity toEntity() {
        Entity entity = new Entity("");
        entity.setProperty(GameCharacterProperty.ID, id)
            .setProperty(GameCharacterProperty.NAME, name)
            .setProperty(GameCharacterProperty.DESCRIPTION, description)
            .setProperty(GameCharacterProperty.HP, new SimpleIntegerProperty(hp))
            .setProperty(GameCharacterProperty.SP, new SimpleIntegerProperty(sp))
            .setProperty(GameCharacterProperty.HP_MAX, new SimpleIntegerProperty(hpMax))
            .setProperty(GameCharacterProperty.SP_MAX, new SimpleIntegerProperty(spMax))
            .setProperty(GameCharacterProperty.STATUSES, new ArrayList<>(statuses))
            .setProperty(GameCharacterProperty.EFFECTS, new ArrayList<>(effects));

        entity.addControl(new GameCharacterControl());

        return entity;
    }
}
