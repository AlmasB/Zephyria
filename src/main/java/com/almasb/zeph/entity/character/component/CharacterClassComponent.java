package com.almasb.zeph.entity.character.component;

import com.almasb.ents.AbstractComponent;
import com.almasb.zeph.entity.character.GameCharacterClass;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class CharacterClassComponent extends AbstractComponent {

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
