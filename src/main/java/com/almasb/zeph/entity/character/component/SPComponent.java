package com.almasb.zeph.entity.character.component;

import com.almasb.fxgl.ecs.component.DoubleComponent;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class SPComponent extends DoubleComponent {

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
}
