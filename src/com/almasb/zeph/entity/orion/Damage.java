package com.almasb.zeph.entity.orion;


public final class Damage {

    public static final Damage NULL = new Damage();

    public enum DamageType {
        PHYSICAL, MAGICAL, PURE
    }

    private boolean critical;
    private DamageType type;
    private int value;
    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public DamageType getType() {
        return type;
    }

    public void setType(DamageType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
