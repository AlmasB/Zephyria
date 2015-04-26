package com.almasb.zeph.entity.orion;

/**
 * An effect that changes status of a character for the duration
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 * @version 1.0
 *
 */
public class StatusEffect implements java.io.Serializable {

    private static final long serialVersionUID = 480489685432710301L;

    public enum Status {
        /**
         * Stunned character is not able to perform any actions
         */
        STUNNED,

        /**
         * Silenced character is not able to use any skills
         */
        SILENCED,

        /**
         * Poisoned character doesn't regenerate hp/sp
         * and loses 1% of max hp/sp instead
         */
        POISONED
    }

    private float duration;
    private Status status;

    public StatusEffect(Status status, float duration) {
        this.duration = duration;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void reduceDuration(float value) {
        duration -= value;
    }

    public float getDuration() {
        return duration;
    }
}
