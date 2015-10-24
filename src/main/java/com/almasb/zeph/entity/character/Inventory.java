package com.almasb.zeph.entity.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.almasb.zeph.entity.GameEntity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a "bag" of items of a player
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 * @version 1.0
 *
 */
public class Inventory implements java.io.Serializable {

    private static final long serialVersionUID = 7187464078429433554L;

    /**
     * Matches number of elements that can be shown in inventory GUI
     */
    public static final int MAX_SIZE = 30;

    /**
     * Actual data structure
     */
    private List<GameEntity> items = new ArrayList<>(MAX_SIZE);
    // TODO: make read only
    private transient ObservableList<GameEntity> itemsProperty = FXCollections.observableArrayList();

//    /**
//     *
//     * @return a new copy of items list, retaining references to original items
//     */
//    public List<GameEntity> getItems() {
//        return new ArrayList<>(items);
//    }

    public final ObservableList<GameEntity> itemsProperty() {
        return itemsProperty;
    }

    /**
     * Adds item to inventory if inventory isnt full
     *
     * @param item
     *              item to add
     * @return
     *          true if added, false otherwise
     */
    public boolean addItem(GameEntity item) {
        if (isFull()) {
            return false;
        }

        items.add(item);
        itemsProperty.add(item);

        return true;
    }

    /**
     * Retrieve item at given index
     *
     * @param index
     * @return
     *          Optional item if index less than inventory size
     *          otherwise empty Optional
     */
    public Optional<GameEntity> getItem(int index) {
        return index < items.size() ? Optional.of(items.get(index)) : Optional.empty();
    }

    /**
     * Removes item from the inventory if it is in it
     *
     * @param item
     * @return
     *          true if removed, false otherwise
     */
    public boolean removeItem(GameEntity item) {
        if (!items.remove(item)) {
            return false;
        }
        itemsProperty.remove(item);
        return true;
    }

    /**
     *
     * @return
     *          number of items in inventory
     */
    public int size() {
        return items.size();
    }

    /**
     *
     * @return
     *          true if number of items in inventory reached maximum
     *          false otherwise
     */
    public boolean isFull() {
        return items.size() == MAX_SIZE;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
