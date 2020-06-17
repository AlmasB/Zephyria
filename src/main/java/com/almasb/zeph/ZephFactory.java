package com.almasb.zeph;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.zeph.character.CharacterData;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.item.*;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ZephFactory implements EntityFactory {

    @Spawns("char")
    public Entity newCharacter(SpawnData data) {
        CharacterData charData = data.get("charData");

        try {

            CharacterEntity entity = new CharacterEntity(charData);
            entity.setX(data.getX());
            entity.setY(data.getY());
            entity.setType(EntityType.CHARACTER);

            entity.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(Config.tileSize, Config.tileSize)));

            // TODO: parse properties from [charData]


            return entity;

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to create char: " + data);
    }

    @Spawns("item")
    public Entity newItem(SpawnData data) {
        ItemData itemData = data.get("itemData");

        // TODO: this is where we parse everything related to item
        if (itemData instanceof UsableItemData) {
            data.put("usable", new UsableItem((UsableItemData) itemData));
        } else if (itemData instanceof WeaponData) {
            data.put("weapon", new Weapon((WeaponData) itemData));
        } else if (itemData instanceof ArmorData) {
            data.put("armor", new Armor((ArmorData) itemData));
        }

        return entityBuilder(data)
                .view(itemData.getDescription().getTextureName())
                .build();
    }
}
