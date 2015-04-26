package com.almasb.zeph.entity.item;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;

public class Armor {

    public enum ArmorProperty implements PropertyKey {
        TYPE
    }

    public Entity toEntity() {
        return new Entity(ArmorProperty.TYPE);
    }
}
