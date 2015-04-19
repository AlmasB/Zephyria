package com.almasb.zeph.entity;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;

public class IngredientItem {

    public enum IngredientItemProperty implements PropertyKey {
        TYPE, ID, NAME, DESCRIPTION, SELL_PRICE
    }

    private int id;
    private String name;
    private String description;
    private String textureName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    /*package-private*/ Entity toEntity() {
        Entity item = new Entity(IngredientItemProperty.TYPE.getUniqueKey());
        item.setProperty(IngredientItemProperty.NAME, name)
                .setProperty(IngredientItemProperty.DESCRIPTION, description)
                .setProperty(IngredientItemProperty.ID, id);

        //weapon.setGraphics(texture);
        return item;
    }

    /*package-private*/ static IngredientItem fromEntity(Entity entity) {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
