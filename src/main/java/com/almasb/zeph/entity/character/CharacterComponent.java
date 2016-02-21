package com.almasb.zeph.entity.character;

import com.almasb.ents.AbstractComponent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class CharacterComponent extends AbstractComponent {

    /**
     * Class of this game character.
     */
    private GameCharacterClass charClass;

    private final ReadOnlyObjectWrapper<GameCharacterClass> charClassProperty = new ReadOnlyObjectWrapper<>();

    /**
     * @return game character class
     */
    public final GameCharacterClass getGameCharacterClass() {
        return charClass;
    }

    /**
     * @return game character class property
     */
    public final ReadOnlyObjectProperty<GameCharacterClass> charClassProperty() {
        return charClassProperty.getReadOnlyProperty();
    }
}
