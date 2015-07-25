package com.almasb.zeph.entity.orion;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;

public class MiscItem {

    public enum MiscItemProperty implements PropertyKey {
        TYPE, ID, NAME, DESCRIPTION, SELL_PRICE
    }

    private int id;
    private String name;
    private String description;
    private String textureName;
    private int sellPrice;

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

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    /*package-private*/ Entity toEntity() {
        Entity item = Entity.noType();
        item.setProperty(MiscItemProperty.NAME, name)
                .setProperty(MiscItemProperty.DESCRIPTION, description)
                .setProperty(MiscItemProperty.ID, id)
                .setProperty(MiscItemProperty.SELL_PRICE, sellPrice);

        //weapon.setGraphics(texture);
        return item;
    }

    /*package-private*/ static MiscItem fromEntity(Entity entity) {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
