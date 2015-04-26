package com.almasb.zeph.entity.orion;

import com.almasb.fxgl.entity.Entity;

public abstract class UsableItem extends GameEntity {

    public UsableItem(int id, String name, String description,
            String textureName) {
        super(id, name, description, textureName);
    }

    private static final long serialVersionUID = 9082052313919069522L;

    public abstract void onUse(GameCharacter target);

    @Override
    public Entity toEntity() {
        Entity e = new Entity("usable_item");
        e.setProperty("usable_item_data", this);
        return e;
    }
}
