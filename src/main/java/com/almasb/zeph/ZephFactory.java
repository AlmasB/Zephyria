package com.almasb.zeph;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.zeph.character.CharacterData;
import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.item.WeaponData;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ZephFactory implements EntityFactory {

    @Spawns("char")
    public Entity newCharacter(SpawnData data) {
        CharacterData charData = data.get("charData");

        CharacterEntity entity = new CharacterEntity(charData);
        entity.setX(data.getX());
        entity.setY(data.getY());
        entity.setType(EntityType.CHARACTER);

        entity.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(Config.tileSize, Config.tileSize)));

        // TODO: parse properties

        return entity;
    }

    @Spawns("item")
    public Entity newItem(SpawnData data) {
        WeaponData weaponData = data.get("weaponData");

        // TODO: this is where from [data] we parse everything related to item

        return entityBuilder(data)
                .view(weaponData.getDescription().getTextureName())
                .build();
    }
}
