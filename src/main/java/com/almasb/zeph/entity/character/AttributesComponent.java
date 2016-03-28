package com.almasb.zeph.entity.character;

import com.almasb.ents.AbstractComponent;
import com.almasb.zeph.combat.Attribute;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class AttributesComponent extends AbstractComponent {

    /**
     * Contains native character attribute values
     */
    private Map<Attribute, Integer> attributes = new HashMap<>();
    private transient Map<Attribute, ReadOnlyIntegerWrapper> attributeProperties = new HashMap<>();

    /**
     *
     * @param attr
     * @return base (native) character attribute value
     */
    public final int getBaseAttribute(Attribute attr) {
        return attributes.get(attr);
    }

    /**
     *
     * @param attr
     * @return base attribute value property
     */
    public final ReadOnlyIntegerProperty attributeProperty(Attribute attr) {
        return attributeProperties.get(attr).getReadOnlyProperty();
    }

    /**
     * Sets given attribute value
     *
     * @param attr
     * @param value
     */
    protected final void setAttribute(Attribute attr, int value) {
        attributes.put(attr, value);
        attributeProperties.get(attr).set(value);
    }

    /**
     * Contains attribute values given by equipped items or effects
     */
    private Map<Attribute, Integer> bAttributes = new HashMap<>();
    private transient Map<Attribute, ReadOnlyIntegerWrapper> bAttributeProperties = new HashMap<>();

    /**
     *
     * @param attr
     * @return bonus attribute value
     */
    public final int getBonusAttribute(Attribute attr) {
        return bAttributes.get(attr);
    }

    /**
     *
     * @param attr
     * @return bonus attr property
     */
    public final ReadOnlyIntegerProperty bAttributeProperty(Attribute attr) {
        return bAttributeProperties.get(attr).getReadOnlyProperty();
    }

    /**
     * Apply bonus attr that comes from item for example
     *
     * @param attr attribute
     * @param bonus value
     */
    public void addBonusAttribute(Attribute attr, int bonus) {
        int value = getBonusAttribute(attr) + bonus;
        bAttributes.put(attr, value);
        bAttributeProperties.get(attr).set(value);
    }

    /**
     *
     * @param attr
     * @return total value for attr, including bonuses
     */
    public int getTotalAttribute(Attribute attr) {
        return getBaseAttribute(attr) + getBonusAttribute(attr);
    }

    public NumberBinding totalAttributeProperty(Attribute attribute) {
        return attributeProperty(attribute).add(bAttributeProperty(attribute));
    }

    public AttributesComponent() {
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute, 0);
            attributeProperties.put(attribute, new ReadOnlyIntegerWrapper(0));
            bAttributes.put(attribute, 0);
            bAttributeProperties.put(attribute, new ReadOnlyIntegerWrapper(0));
        }
    }
}
