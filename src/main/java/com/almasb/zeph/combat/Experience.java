package com.almasb.zeph.combat;

public class Experience implements java.io.Serializable {
    private static final long serialVersionUID = 2762180993708324531L;

    public int base, stat, job;

    public Experience(int base, int stat, int job) {
        this.base = base;
        this.stat = stat;
        this.job = job;
    }

    public void add(Experience xp) {
        this.base += xp.base;
        this.stat += xp.stat;
        this.job += xp.job;
    }
}