package com.almasb.zeph.entity.orion;

import java.util.ArrayList;
import java.util.Optional;

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
    private ArrayList<GameEntity> items = new ArrayList<GameEntity>(MAX_SIZE);

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
        return items.add(item);
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
        return true;
    }

    /**
     *
     * @return
     *          a new copy of items list, retaining
     *          references to original items
     */
    public ArrayList<GameEntity> getItems() {
        return new ArrayList<GameEntity>(items);
    }

    /**
     *
     * @return
     *          number of items in inventory
     */
    public int getSize() {
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
