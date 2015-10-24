package com.almasb.zeph.entity.item;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.GameEntity;
import com.almasb.zeph.entity.character.GameCharacter;

public abstract class UsableItem extends GameEntity {

    public UsableItem(int id, String name, String description,
            String textureName) {
        super(id, name, description, textureName);
    }

    private static final long serialVersionUID = 9082052313919069522L;

    public abstract void onUse(GameCharacter target);

    @Override
    public Entity toEntity() {
        Entity e = Entity.noType();
        //e.setProperty("usable_item_data", this);
        return e;
    }
}
