package com.almasb.zeph.entity.item;

import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.CharacterControl;

public abstract class UsableItem extends DescriptionComponent {

    public UsableItem(int id, String name, String description,
            String textureName) {
        super(id, name, description, textureName);
    }

    private static final long serialVersionUID = 9082052313919069522L;

    public abstract void onUse(CharacterControl target);

//    @Override
//    public Entity toEntity() {
//        Entity e = Entity.noType();
//        //e.setProperty("usable_item_data", this);
//        return e;
//    }
}
