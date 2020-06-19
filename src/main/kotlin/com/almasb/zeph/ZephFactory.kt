package com.almasb.zeph

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.ai.RandomWanderComponent
import com.almasb.zeph.item.*

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    @Spawns("char")
    fun newCharacter(data: SpawnData): Entity? {
        val charData = data.get<CharacterData>("charData")
        try {
            val entity = CharacterEntity(charData)
            entity.x = data.x
            entity.y = data.y
            entity.type = EntityType.CHARACTER
            entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(Config.spriteSize.toDouble(), Config.spriteSize.toDouble())))
            entity.addComponent(RandomWanderComponent())

            // TODO: parse properties from [charData]
            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw RuntimeException("Failed to create char: $data")
    }

    @Spawns("item")
    fun newItem(data: SpawnData): Entity? {
        val itemData = data.get<ItemData>("itemData")

        // TODO: this is where we parse everything related to item
        if (itemData is UsableItemData) {
            data.put("usable", UsableItem(itemData))
        } else if (itemData is WeaponData) {
            data.put("weapon", Weapon(itemData))
        } else if (itemData is ArmorData) {
            data.put("armor", Armor(itemData))
        }
        return FXGL.entityBuilder(data)
                .view(itemData.description.textureName)
                .build()
    }
}