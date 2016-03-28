package com.almasb.zeph.entity.character.component;

import com.almasb.ents.component.DoubleComponent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class HPComponent extends DoubleComponent {

    private DoubleProperty maxValue = new ReadOnlyDoubleWrapper(0.1);

    /**
     * @return max value
     */
    public double getMaxValue() {
        return maxValue.get();
    }

    public DoubleProperty maxValueProperty() {
        return maxValue;
    }

    /**
     * Set max value.
     *
     * @param maxValue max value
     */
    public void setMaxValue(double maxValue) {
        maxValueProperty().set(maxValue);
    }

    /**
     * Damage component by given value.
     *
     * @param value the damage amount
     */
    public void damage(double value) {
        setValue(Math.max(0, getValue() - value));
    }

    /**
     * Damage component by given percentage. The percentage is calculated from
     * current value.
     *
     * @param value percentage of current value
     */
    public void damagePercentageCurrent(double value) {
        damage(value / 100 * getValue());
    }

    /**
     * Damage component by given percentage. The percentage is calculated from
     * max value.
     *
     * @param value percentage of max value
     */
    public void damagePercentageMax(double value) {
        damage(value / 100 * getMaxValue());
    }

    /**
     * Restore component by given value.
     *
     * @param value the amount to restore
     */
    public void restore(double value) {
        setValue(Math.min(getMaxValue(), getValue() + value));
    }

    /**
     * Restore component by given percentage. The percentage is calculated from
     * current value.
     *
     * @param value percentage of current value
     */
    public void restorePercentageCurrent(double value) {
        restore(value / 100 * getValue());
    }

    /**
     * Restore by given percentage. The percentage is calculated from
     * max value.
     *
     * @param value percentage of max value
     */
    public void restorePercentageMax(double value) {
        restore(value / 100 * getMaxValue());
    }

    /**
     * Check if value is 0. Note that because internal value is a double,
     * value of 0.xx will not return true.
     *
     * @return true iff value is 0
     */
    public boolean isZero() {
        return getValue() == 0;
    }




//    public HPComponent(double maxValue) {
//        super(maxValue);
//    }
//
//    /**
//     * Current hp
//     */
//    private int hp = 0;
//    private transient ReadOnlyIntegerWrapper hpProperty = new ReadOnlyIntegerWrapper(
//            hp);
//
//    /**
//     *
//     * @return current hp
//     */
//    public final int getHP() {
//        return hp;
//    }
//
//    /**
//     *
//     * @return current hp property
//     */
//    public final ReadOnlyIntegerProperty hpProperty() {
//        return hpProperty.getReadOnlyProperty();
//    }
//
//    /**
//     * Sets current hp. If the value is outside [0..getTotalStat(Stat.MAX_HP)],
//     * the value will be clamped.
//     *
//     * @param value
//     */
//    public final void setHP(int value) {
//        if (value < 0)
//            value = 0;
//        if (value > getTotalStat(Stat.MAX_HP))
//            value = (int) getTotalStat(Stat.MAX_HP);
//
//        hp = value;
//        hpProperty.set(value);
//    }
//
//    /**
//     * Restores hp. HP will not go outside getTotalStat(Stat.MAX_HP). No effect
//     * if the value is negative.
//     *
//     * @param value
//     */
//    protected final void restoreHP(float value) {
//        if (value <= 0)
//            return;
//        setHP(getHP() + (int) value);
//    }
//
//    /**
//     * Takes away value from hp. HP will not drop below 0. No effect if the
//     * value is negative.
//     *
//     * @param value
//     */
//    protected final void damageHP(float value) {
//        if (value <= 0)
//            return;
//        setHP(getHP() - (int) value);
//    }
}
