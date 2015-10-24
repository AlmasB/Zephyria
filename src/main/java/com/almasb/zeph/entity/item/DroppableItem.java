package com.almasb.zeph.entity.item;

public class DroppableItem implements java.io.Serializable {

    private static final long serialVersionUID = -7613094483468439486L;

    public int itemID;
    public int dropChance;

    public DroppableItem(int id, int chance) {
        itemID = id;
        dropChance = chance;
    }
}
