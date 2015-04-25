package com.almasb.zeph.entity;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.entity.control.PlayerControl;
import com.almasb.zeph.entity.item.Armor;
import com.almasb.zeph.entity.item.Weapon;

public class Player extends GameCharacter {

    public enum PlayerProperty implements PropertyKey {
        TYPE,
        ITEM_HEAD, ITEM_BODY, ITEM_SHOES, ITEM_LEFT_HAND, ITEM_RIGHT_HAND,
        INVENTORY
    }

    private Weapon rightHand = new Weapon();
    private Weapon leftHand = new Weapon();
    private Armor head = new Armor();
    private Armor body = new Armor();
    private Armor shoes = new Armor();

    private Inventory inventory;

    @Override
    public Entity toEntity() {
        Entity entity = super.toEntity();
        entity.setProperty(PlayerProperty.ITEM_BODY, body.toEntity())
            .setProperty(PlayerProperty.ITEM_HEAD, head.toEntity())
            .setProperty(PlayerProperty.ITEM_SHOES, shoes.toEntity())
            .setProperty(PlayerProperty.ITEM_LEFT_HAND, leftHand.toEntity())
            .setProperty(PlayerProperty.ITEM_RIGHT_HAND, rightHand.toEntity())
            .setProperty(PlayerProperty.INVENTORY, inventory.toEntity());

        entity.addControl(new PlayerControl());
        return entity;
    }
}
