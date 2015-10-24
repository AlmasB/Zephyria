package com.almasb.zeph;

import com.almasb.fxgl.entity.FXGLEventType;

public final class Events {

    public enum Event implements FXGLEventType {
        /**
         * Triggered when a unit attacks another.
         */
        ATTACKING,

        /**
         * Triggered when a unit is being attacked
         */
        BEING_ATTACKED,

        /**
         * Triggered when a unit dies
         */
        DEATH
    }
}
