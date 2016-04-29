package com.almasb.zeph.entity.ai

import com.almasb.astar.AStarNode
import com.almasb.ents.AbstractControl
import com.almasb.ents.Entity
import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.entity.GameEntity
import com.almasb.fxgl.entity.component.CollidableComponent
import com.almasb.fxgl.entity.control.OffscreenCleanControl
import com.almasb.fxgl.entity.control.ProjectileControl
import com.almasb.fxgl.texture.DynamicAnimatedTexture
import com.almasb.zeph.CharacterAnimation
import com.almasb.zeph.Services
import com.almasb.zeph.entity.EntityType
import com.almasb.zeph.entity.character.CharacterEntity
import com.almasb.zeph.entity.character.PlayerEntity
import com.almasb.zeph.entity.character.control.CharacterControl
import com.almasb.zeph.entity.character.control.PlayerControl
import com.almasb.zeph.entity.item.WeaponType
import com.almasb.zeph.entity.item.component.OwnerComponent
import javafx.util.Duration
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AttackControl : AbstractControl() {

    var enabled = true

    private lateinit var char: CharacterEntity
    private lateinit var animation: DynamicAnimatedTexture

    private val random = Random()
    private var time = 0.0

    private val player: PlayerEntity

    init {
        player = FXGL.getService(Services.GAME_APP).player
    }

    override fun onAdded(entity: Entity) {
        char = entity as CharacterEntity
        animation = char.data.animation
    }

    override fun onUpdate(entity: Entity, tpf: Double) {
        startAttack(char, player)
    }

    private fun startAttack(attacker: Entity, target: Entity) {
        if (!enabled)
            return

        if (!attacker.isActive || !target.isActive)
            return

        val a = attacker.getControlUnsafe(CharacterControl::class.java)

        if (!a.canAttack())
            return

        a.resetAtkTick()

        attack(attacker as CharacterEntity, target as CharacterEntity)
    }

    private fun attack(attacker: CharacterEntity, target: GameEntity) {
        val vector = target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld)

        val animation = attacker.data.animation

        if (Math.abs(vector.x) >= Math.abs(vector.y)) {
            if (vector.x >= 0) {
                animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
            } else {
                animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
            }
        } else {
            if (vector.y >= 0) {
                animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
            } else {
                animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
            }
        }

        // TODO: generalize

//        attacker.getControl(PlayerControl::class.java).ifPresent { c ->
//            if (playerControl.getRightWeapon().data.type === WeaponType.BOW) {
//                if (Math.abs(vector.x) >= Math.abs(vector.y)) {
//                    if (vector.x >= 0) {
//                        animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
//                    } else {
//                        animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
//                    }
//                } else {
//                    if (vector.y >= 0) {
//                        animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
//                    } else {
//                        animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
//                    }
//                }
//            }
//        }

        FXGL.getMasterTimer().runOnceAfter({
            if (!attacker.isActive || !target.isActive)
                return@runOnceAfter

            Entities.builder()
                    .type(EntityType.PROJECTILE)
                    .at(attacker.boundingBoxComponent.centerWorld)
                    .viewFromTextureWithBBox("projectiles/arrow2.png")
                    .with(ProjectileControl(target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld), 5.0))
                    .with(OffscreenCleanControl())
                    .with(OwnerComponent(attacker))
                    .with(CollidableComponent(true))
                    .buildAndAttach(FXGL.getGame().gameWorld)
        }, Duration.seconds(0.8))
    }
}