package com.almasb.zeph.combat;

/**
 * An effect that changes status of a character for the duration.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
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

    private double duration;
    private Status status;

    public StatusEffect(Status status, float duration) {
        this.duration = duration;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void reduceDuration(double value) {
        duration -= value;
    }

    public double getDuration() {
        return duration;
    }
}
