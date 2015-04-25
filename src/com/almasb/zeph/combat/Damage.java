package com.almasb.zeph.combat;

public final class Damage {

    /*package-private*/ enum DamageType {
        PHYSICAL, MAGICAL, PURE
    }

    private boolean critical;
    private DamageType type;
    private int value;


}
