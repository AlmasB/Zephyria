package com.almasb.zeph;

import com.almasb.fxgl.entity.*;
import com.almasb.zeph.character.CharacterData;
import com.almasb.zeph.character.components.CharacterComponent;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ZephFactory implements EntityFactory {

    @Spawns("char")
    public Entity newCharacter(SpawnData data) {
        CharacterData charData = data.get("charData");

        return Entities.builder()
                .from(data)
                .with(new CharacterComponent(charData))
                .build();
    }

    @Spawns("item")
    public Entity newItem(SpawnData data) {


        // TODO: this is where from [data] we parse everything related to item

        return Entities.builder()
                .from(data)
                // with Armor / Weapon Component
                .build();
    }
}
