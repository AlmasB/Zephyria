package com.almasb.zeph.entity.character.component;

import com.almasb.ents.AbstractComponent;
import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Stat;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class StatsComponent extends AbstractComponent {

    /**
     * Contains native character stats calculated from both attributes
     * and bAttributes
     */
    private Map<Stat, Float> stats = new HashMap<>();
    private transient Map<Stat, ReadOnlyIntegerWrapper> statProperties = new HashMap<>();

    /**
     *
     * @param stat
     * @return base (native) character stat
     */
    public final float getBaseStat(Stat stat) {
        return stats.get(stat);
    }

    /**
     *
     * @param stat
     * @return base stat property
     */
    public final IntegerProperty statProperty(Stat stat) {
        return statProperties.get(stat);
    }

    /**
     * Set base stat value
     *
     * @param stat
     * @param value
     */
    private void setBaseStat(Stat stat, float value) {
        stats.put(stat, value);
        statProperties.get(stat).set((int) value);
    }

    /**
     * Contains stats given by equipped items or effects
     */
    private Map<Stat, Float> bStats = new HashMap<>();
    private transient Map<Stat, ReadOnlyIntegerWrapper> bStatProperties = new HashMap<>();

    /**
     *
     * @param stat
     * @return bonus stat value
     */
    public final float getBonusStat(Stat stat) {
        return bStats.get(stat);
    }

    /**
     *
     * @param stat
     * @return bonus stat property
     */
    public final ReadOnlyIntegerProperty bStatProperty(Stat stat) {
        return bStatProperties.get(stat).getReadOnlyProperty();
    }

    private Map<Stat, NumberBinding> bindings = new HashMap<>();

    public final NumberBinding totalStatProperty(Stat stat) {
        return bindings.get(stat);
    }

    /**
     * Apply bonus stat that comes from item for example
     *
     * @param stat
     *            stat
     * @param bonus
     *            value
     */
    public final void addBonusStat(Stat stat, int bonus) {
        float value = getBonusStat(stat) + bonus;
        bStats.put(stat, value);
        bStatProperties.get(stat).set((int) value);
    }

    /**
     *
     * @param stat
     * @return total value for stat, including bonuses
     */
    public float getTotalStat(Stat stat) {
        //return getBaseStat(stat) + getBonusStat(stat);
        return bindings.get(stat).floatValue();
    }

    public StatsComponent() {
        for (Stat stat : Stat.values()) {
            stats.put(stat, 0f);
            statProperties.put(stat, new ReadOnlyIntegerWrapper(0));
            bStats.put(stat, 0f);
            bStatProperties.put(stat, new ReadOnlyIntegerWrapper(0));

            bindings.put(stat, statProperty(stat).add(bStatProperty(stat)));
        }
    }
}
