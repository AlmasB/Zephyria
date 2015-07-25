package com.almasb.zeph.entity.orion;

public final class Damage {

    //public static final Damage NULL = new Damage();

    public enum DamageType {
        /**
         * Damage dealt by weapon of any kind or a skill
         * which is based on weapon attack. Can crit.
         */
        PHYSICAL,

        /**
         * Damage dealt by a magic based skill. Can crit.
         */
        MAGICAL,

        /**
         * Pure flat damage, most likely dealt by a skill.
         * Can NOT critically strike.
         */
        PURE
    }

    public enum DamageCritical {
        TRUE, FALSE
    }

    private boolean critical;
    private DamageType type;
    private int value;
    private Element element;

    public Damage(DamageType type, Element element, int value, DamageCritical critical) {
        this.type = type;
        this.value = value;
        this.element = element;
        this.critical = critical == DamageCritical.TRUE;
    }

    public Element getElement() {
        return element;
    }

//    public void setElement(Element element) {
//        this.element = element;
//    }

    public boolean isCritical() {
        return critical;
    }

//    public void setCritical(boolean critical) {
//        this.critical = critical;
//    }

    public DamageType getType() {
        return type;
    }

//    public void setType(DamageType type) {
//        this.type = type;
//    }

    public int getValue() {
        return value;
    }

//    public void setValue(int value) {
//        this.value = value;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value).append(type).append(critical ? " CRITICAL" : " NON_CRITICAL").append(" damage with ")
            .append(element).append(" element");
        return sb.toString();
    }
}
