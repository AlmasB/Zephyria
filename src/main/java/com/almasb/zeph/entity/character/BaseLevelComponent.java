package com.almasb.zeph.entity.character;

import com.almasb.ents.AbstractComponent;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class BaseLevelComponent extends AbstractComponent {

    /**
     * Current base level
     */
    private int baseLevel = 1;
    private transient ReadOnlyIntegerWrapper baseLevelProperty = new ReadOnlyIntegerWrapper(
            baseLevel);

    /**
     *
     * @return base level
     */
    public final int getBaseLevel() {
        return baseLevel;
    }

    /**
     *
     * @return base level property
     */
    public final ReadOnlyIntegerProperty baseLevelProperty() {
        return baseLevelProperty.getReadOnlyProperty();
    }

    /**
     * Sets base level. Also updates baseLevelProperty
     *
     * @param level
     */
    protected final void setBaseLevel(int level) {
        baseLevel = level;
        baseLevelProperty.set(level);
    }
}
