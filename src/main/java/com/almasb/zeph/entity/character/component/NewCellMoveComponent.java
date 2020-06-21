/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.zeph.entity.character.component;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.Cell;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import static java.lang.Math.abs;

/**
 * Enables cell based movement for an entity.
 * Moving to a cell is complete when entity's center (note without bbox center is 0,0) is
 * aligned with the cell's center.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public final class NewCellMoveComponent extends Component {

    private int nextCellX;
    private int nextCellY;

    private int cellWidth;
    private int cellHeight;
    private double speed;
    private boolean isAllowRotation = false;

    private ReadOnlyBooleanWrapper isMovingProp = new ReadOnlyBooleanWrapper(false);

    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    public NewCellMoveComponent(int cellWidth, int cellHeight, double speed) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.speed = speed;
    }

    public ReadOnlyBooleanProperty movingProperty() {
        return isMovingProp.getReadOnlyProperty();
    }

    public boolean isMoving() {
        return isMovingProp.get();
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    /**
     * Note: entity's anchored position  is used to compute this.
     *
     * @return the cell x where entity is currently at
     */
    public int getCellX() {
        var center = entity.getAnchoredPosition();
        return (int) (center.getX() / cellWidth);
    }

    /**
     * Note: entity's anchored position is used to compute this.
     *
     * @return the cell y where entity is currently at
     */
    public int getCellY() {
        var center = entity.getAnchoredPosition();
        return (int) (center.getY() / cellHeight);
    }

    /**
     * Sets (anchored) position of the entity to given cell.
     */
    public void setPositionToCell(Cell cell) {
        setPositionToCell(cell.getX(), cell.getY());
    }

    /**
     * Sets (anchored) position of the entity to given cell, identified using given cell X, Y.
     */
    public void setPositionToCell(int cellX, int cellY) {
        // cell center
        int cx = cellX * cellWidth + cellWidth / 2;
        int cy = cellY * cellHeight + cellHeight / 2;

        entity.setAnchoredPosition(cx, cy, entity.getLocalAnchor());

        isMovingProp.setValue(false);
        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;
    }

    public void moveToCell(Cell cell) {
        moveToCell(cell.getX(), cell.getY());
    }

    public void moveToCell(int cellX, int cellY) {
        nextCellX = cellX;
        nextCellY = cellY;
        isMovingProp.setValue(true);
    }

    /**
     * Allows entity to rotate (only 4 directions) based on its velocity.
     */
    public NewCellMoveComponent allowRotation(boolean isAllowRotation) {
        this.isAllowRotation = isAllowRotation;
        return this;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isMoving())
            return;

        double tpfSpeed = tpf * speed;

        // cell center
        int cx = nextCellX * cellWidth + cellWidth / 2;
        int cy = nextCellY * cellHeight + cellHeight / 2;

        var entityCenter = entity.getAnchoredPosition();

        // move in x and y per frame
        double dx = cx - entityCenter.getX();
        double dy = cy - entityCenter.getY();

        if (isAllowRotation)
            updateRotation(dx, dy);

        int offsetX = (int) (entityCenter.getX() - entity.getX());
        int offsetY = (int) (entityCenter.getY() - entity.getY());

        if (abs(dx) <= tpfSpeed) {
            isMovingLeft = false;
            isMovingRight = false;
            entity.setX(cx - offsetX);
        } else {
            isMovingLeft = Math.signum(dx) < 0;
            isMovingRight = Math.signum(dx) > 0;
            entity.translateX(tpfSpeed * Math.signum(dx));
        }

        if (abs(dy) <= tpfSpeed) {
            isMovingUp = false;
            isMovingDown = false;
            entity.setY(cy - offsetY);
        } else {
            isMovingUp = Math.signum(dy) < 0;
            isMovingDown = Math.signum(dy) > 0;
            entity.translateY(tpfSpeed * Math.signum(dy));
        }

        // center after movement
        entityCenter = entity.getAnchoredPosition();

        if ((int) entityCenter.getX() == cx && (int) entityCenter.getY() == cy) {
            isMovingProp.setValue(false);
            isMovingLeft = false;
            isMovingRight = false;
            isMovingUp = false;
            isMovingDown = false;
        }
    }

    /**
     * @param dx move distance in X
     * @param dy move distance in Y
     */
    private void updateRotation(double dx, double dy) {
        if (dx > 0) {
            entity.setRotation(0);
        } else if (dx < 0) {
            entity.setRotation(180);
        } else if (dy > 0) {
            entity.setRotation(90);
        } else if (dy < 0) {
            entity.setRotation(270);
        }
    }
}
