package com.almasb.zeph

import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.dsl.spawn
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.ai.RandomWanderComponent
import com.almasb.zeph.character.char
import com.almasb.zeph.character.components.*
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
import com.almasb.zeph.item.*
import javafx.geometry.Point2D
import java.util.function.Supplier

/**
 * Creates all entities.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ZephFactory : EntityFactory {

    @Spawns("char")
    fun newCharacter(data: SpawnData): Entity {
        val charData = data.get<CharacterData>("charData")

        try {
            val entity = CharacterEntity()
            entity.x = data.x
            entity.y = data.y
            entity.type = EntityType.CHARACTER
            entity.localAnchor = Point2D(Config.spriteSize / 2.0, Config.spriteSize - 10.0)
            entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(Config.spriteSize.toDouble(), Config.spriteSize.toDouble())))

            with(entity) {
                addComponent(StateComponent())
                addComponent(EffectComponent())
                addComponent(NewCellMoveComponent(Config.tileSize, Config.tileSize, Config.CHAR_MOVE_SPEED))
                addComponent(NewAStarMoveComponent(LazyValue(Supplier { FXGL.getAppCast<ZephyriaApp>().grid })))

                addComponent(AnimationComponent(charData.description.textureName))
                addComponent(CharacterComponent(charData))
                addComponent(CharacterActionComponent())

                addComponent(CharacterChildViewComponent())
                addComponent(RandomWanderComponent())
            }

            // TODO: parse properties from [charData]
            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw RuntimeException("Failed to create char: $data")
    }

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        val player = newCharacter(data)
        player.type = EntityType.PLAYER
        player.removeComponent(RandomWanderComponent::class.java)
        player.addComponent(PlayerComponent())
        return player
    }

    @Spawns("item")
    fun newItem(data: SpawnData): Entity {
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

fun newPlayer(): CharacterEntity {
    val charData = char {
        desc {
            id = 1
            name = "Developer"
            description = "It's you! $name"
            textureName = "chars/players/player_full.png"
        }

        charClass = CharacterClass.NOVICE
    }

    return spawn("player", SpawnData(0.0, 0.0).put("charData", charData)) as CharacterEntity
}