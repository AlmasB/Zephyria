package com.almasb.zeph.character

import com.almasb.fxgl.core.util.LazyValue
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.state.StateComponent
import com.almasb.zeph.Config
import com.almasb.zeph.Inventory
import com.almasb.zeph.ZephyriaApp
import com.almasb.zeph.character.components.AnimationComponent
import com.almasb.zeph.character.components.CharacterActionComponent
import com.almasb.zeph.character.components.CharacterChildViewComponent
import com.almasb.zeph.character.components.CharacterComponent
import com.almasb.zeph.entity.character.component.NewAStarMoveComponent
import com.almasb.zeph.entity.character.component.NewCellMoveComponent
import javafx.geometry.Point2D
import java.util.function.Supplier

/**
 * This is a convenience class and DOES NOT have any logic.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
open class CharacterEntity(val data: CharacterData, val characterComponent: CharacterComponent) : Entity() {

    constructor(data: CharacterData) : this(data, CharacterComponent(data))

    val animationComponent = AnimationComponent(data.description.textureName)
    val actionComponent = CharacterActionComponent()

    val attributes = characterComponent.attributes

    val stats = characterComponent.stats

    val hp = characterComponent.hp
    val sp = characterComponent.sp

    val baseLevel = characterComponent.baseLevel

    val inventory: Inventory
        get() = characterComponent.inventory

    init {
        transformComponent.localAnchor = Point2D(Config.spriteSize / 2.0, (Config.spriteSize - 10).toDouble())

        addComponent(StateComponent())
        addComponent(EffectComponent())
        addComponent(characterComponent)
        addComponent(animationComponent)

        addComponent(CharacterChildViewComponent())

        addComponent(NewCellMoveComponent(ZephyriaApp.TILE_SIZE, ZephyriaApp.TILE_SIZE, Config.CHAR_MOVE_SPEED))
        addComponent(NewAStarMoveComponent(LazyValue(Supplier { FXGL.getAppCast<ZephyriaApp>().grid })))


        addComponent(actionComponent)
    }

    fun kill() {
        characterComponent.kill()
    }
}