package com.almasb.zeph;

import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum CharacterAnimation implements AnimationChannel {

    CAST_UP(0, 7),
    CAST_LEFT(1, 7),
    CAST_DOWN(2, 7),
    CAST_RIGHT(3, 7),

    WALK_RIGHT(11, 9),
    WALK_LEFT(9, 9),
    WALK_UP(8, 9),
    WALK_DOWN(10, 9),

    SLASH_UP(12, 6),
    SLASH_LEFT(13, 6),
    SLASH_DOWN(14, 6),
    SLASH_RIGHT(15, 6),

    SHOOT_UP(16, 13),
    SHOOT_LEFT(17, 13),
    SHOOT_DOWN(18, 13),
    SHOOT_RIGHT(19, 13),

    DEATH(20, 6);

    private int row, cycle;

    CharacterAnimation(int row, int cycle) {
        this.row = row;
        this.cycle = cycle;
    }

    @Override
    public Rectangle2D area() {
        return new Rectangle2D(0, Config.INSTANCE.getTileSize() * row, Config.INSTANCE.getTileSize() * cycle, Config.INSTANCE.getTileSize());
    }

    @Override
    public int frames() {
        return cycle;
    }

    @Override
    public Duration duration() {
        return Duration.seconds(1.2);
    }
}
