package com.almasb.zeph.entity.item

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
enum class WeaponType(val range: Int, val aspdFactor: Float) {

    ONE_H_SWORD(2, 0.85f), ONE_H_AXE(2, 0.95f), DAGGER(1, 1.25f), SPEAR(3, 0.85f), MACE(2, 1.0f), ROD(5, 0.9f), SHIELD(0, 0.9f), // 1H, shield only left-hand
    TWO_H_SWORD(2, 0.7f), TWO_H_AXE(2, 0.65f), KATAR(1, 0.85f), BOW(5, 0.75f);

    fun isTwoHanded() = this.ordinal >= TWO_H_SWORD.ordinal
}