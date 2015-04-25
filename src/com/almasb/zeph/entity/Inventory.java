package com.almasb.zeph.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.entity.item.Item;

public class Inventory {

    public enum InventoryProperty implements PropertyKey {
        LIST
    }

    private List<Item> items = new ArrayList<>();

    public Entity toEntity() {
        Entity inventory = new Entity("inventory");

        ObservableList<Entity> list = FXCollections.observableArrayList(
                items.stream().map(Item::toEntity).collect(Collectors.toList()));

        inventory.setProperty(InventoryProperty.LIST, list);
        inventory.setGraphics(new ListView<Entity>(list));

        return null;
    }
}
