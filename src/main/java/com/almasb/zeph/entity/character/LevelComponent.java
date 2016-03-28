package com.almasb.zeph.entity.character;

import com.almasb.ents.AbstractComponent;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class LevelComponent extends AbstractComponent {

    /**
     * Current base level
     */
    private int level = 1;
    private transient ReadOnlyIntegerWrapper levelProperty = new ReadOnlyIntegerWrapper(level);

    /**
     *
     * @return base level
     */
    public final int getLevel() {
        return level;
    }

    /**
     *
     * @return base level property
     */
    public final ReadOnlyIntegerProperty levelProperty() {
        return levelProperty.getReadOnlyProperty();
    }

    /**
     * Sets base level. Also updates levelProperty
     *
     * @param level
     */
    protected final void setLevel(int level) {
        this.level = level;
        levelProperty.set(level);
    }

    protected final void incLevel() {
        setLevel(getLevel() + 1);
    }
}
