/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.zeph.entity.character.component;

import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarPathfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@Required(NewCellMoveComponent.class)
public final class NewAStarMoveComponent extends Component {

    private NewCellMoveComponent moveComponent;

    private LazyValue<AStarPathfinder> pathfinder;

    private List<AStarCell> path = new ArrayList<>();

    private Runnable delayedPathCalc = EmptyRunnable.INSTANCE;

    public NewAStarMoveComponent(AStarGrid grid) {
        this(new LazyValue<>(() -> grid));
    }

    /**
     * This ctor is for cases when the grid has not been constructed yet.
     */
    public NewAStarMoveComponent(LazyValue<AStarGrid> grid) {
        pathfinder = new LazyValue<>(() -> new AStarPathfinder(grid.get()));
    }

    @Override
    public void onAdded() {
        moveComponent.movingProperty().addListener((o, wasMoving, isMoving) -> {
            if (!isMoving) {
                delayedPathCalc.run();
                delayedPathCalc = EmptyRunnable.INSTANCE;
            }
        });
    }

    public boolean isMoving() {
        return moveComponent.isMoving();
    }

    public boolean isPathEmpty() {
        return path.isEmpty();
    }

    /**
     * @return true when the path is empty and entity is no longer moving
     */
    public boolean isAtDestination() {
        return !isMoving() && isPathEmpty();
    }

    public AStarGrid getGrid() {
        return pathfinder.get().getGrid();
    }

    public void moveToRightCell() {
        getGrid().getRight(moveComponent.getCellX(), moveComponent.getCellY())
                .ifPresent(this::moveToCell);
    }

    public void moveToLeftCell() {
        getGrid().getLeft(moveComponent.getCellX(), moveComponent.getCellY())
                .ifPresent(this::moveToCell);
    }

    public void moveToUpCell() {
        getGrid().getUp(moveComponent.getCellX(), moveComponent.getCellY())
                .ifPresent(this::moveToCell);
    }

    public void moveToDownCell() {
        getGrid().getDown(moveComponent.getCellX(), moveComponent.getCellY())
                .ifPresent(this::moveToCell);
    }

    public void moveToCell(AStarCell cell) {
        moveToCell(cell.getX(), cell.getY());
    }

    /**
     * Entity's anchored position is used to position it in the cell.
     */
    public void moveToCell(int x, int y) {
        int startX = moveComponent.getCellX();
        int startY = moveComponent.getCellY();

        moveToCell(startX, startY, x, y);
    }

    /**
     * Entity's anchored position  is used to position it in the cell.
     * This can be used to explicitly specify the start X and Y of the entity.
     */
    public void moveToCell(int startX, int startY, int targetX, int targetY) {
        if (!moveComponent.isMoving()) {
            path = pathfinder.get().findPath(startX, startY, targetX, targetY);
        } else {
            delayedPathCalc = () -> {
                path = pathfinder.get().findPath(moveComponent.getCellX(), moveComponent.getCellY(), targetX, targetY);
            };
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (path.isEmpty() || moveComponent.isMoving())
            return;

        var next = path.remove(0);

        moveComponent.moveToCell(next.getX(), next.getY());
    }
}
