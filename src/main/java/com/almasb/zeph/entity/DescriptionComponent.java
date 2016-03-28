package com.almasb.zeph.entity;

import com.almasb.ents.AbstractComponent;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public final class DescriptionComponent extends AbstractComponent implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Object ID in database
     */
    private int id;
    private transient ReadOnlyIntegerProperty idProperty;

    /**
     * @return id
     */
    public final int getID() {
        return id;
    }

    /**
     * @return id property
     */
    public final ReadOnlyIntegerProperty idProperty() {
        if (idProperty == null)
            idProperty = new ReadOnlyIntegerWrapper(id).getReadOnlyProperty();
        return idProperty;
    }

    /**
     * In game name
     */
    private String name;
    private transient ReadOnlyStringProperty nameProperty;

    /**
     * @return name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return name property
     */
    public final ReadOnlyStringProperty nameProperty() {
        if (nameProperty == null)
            nameProperty = new ReadOnlyStringWrapper(name).getReadOnlyProperty();
        return nameProperty;
    }

    /**
     * Entity description
     */
    private String description;
    private transient ReadOnlyStringProperty descriptionProperty;

    /**
     * @return description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @return description property
     */
    public final ReadOnlyStringProperty descriptionProperty() {
        if (descriptionProperty == null)
            descriptionProperty = new ReadOnlyStringWrapper(description).getReadOnlyProperty();
        return descriptionProperty;
    }

    /**
     * File name of the texture in assets/textures/
     */
    private String textureName;

    /**
     * @return texture name
     */
    public final String getTextureName() {
        return textureName;
    }

    public DescriptionComponent(int id, String name, String description, String textureName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.textureName = textureName;
    }

    /**
     * Converts to format: [id] name (textureName) - description.
     */
    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s", id, name, textureName, description);
    }
}
