//package com.almasb.zeph.entity.ai
//
//import com.almasb.fxgl.ecs.AbstractControl
//import com.almasb.fxgl.ecs.Entity
//import com.almasb.fxgl.app.FXGL
//import com.almasb.fxgl.entity.Entities
//import com.almasb.fxgl.entity.GameEntity
//import com.almasb.fxgl.entity.component.CollidableComponent
//import com.almasb.fxgl.entity.control.OffscreenCleanControl
//import com.almasb.fxgl.entity.control.ProjectileControl
//import com.almasb.fxgl.texture.AnimatedTexture
//import com.almasb.zeph.CharacterAnimation
//import com.almasb.zeph.Config
//import com.almasb.zeph.EntityType
//import com.almasb.zeph.entity.character.CharacterEntity
//import com.almasb.zeph.item.component.OwnerComponent
//import javafx.beans.property.SimpleObjectProperty
//import javafx.util.Duration
//
///**
// *
// *
// * @author Almas Baimagambetov (almaslvl@gmail.com)
// */
//class AttackControl : AbstractControl() {
//
//    var enabled = true
//
//    private lateinit var char: CharacterEntity
//    private lateinit var animation: AnimatedTexture
//
//    val selected = SimpleObjectProperty<Entity>()
//
//    override fun onAdded(entity: Entity) {
//        char = entity as CharacterEntity
//        animation = char.data.animation
//    }
//
//    override fun onUpdate(entity: Entity, tpf: Double) {
//        if (selected.value is CharacterEntity)
//            startAttack(char, selected.value as GameEntity)
//    }
//
//    private fun startAttack(attacker: CharacterEntity, target: GameEntity) {
//        if (!enabled)
//            return
//
//        if (!attacker.isActive || !target.isActive)
//            return
//
//        val atkRange = attacker.weapon.value.range
//
//        // are we close enough?
//        if (attacker.positionComponent.distance(target.positionComponent) <= atkRange * Config.tileSize) {
//            val control = attacker.charConrol
//
//            // can we attack already?
//            if (!control.canAttack())
//                return
//
//            control.resetAtkTick()
//
//            attack(attacker, target)
//        } else {
//
//            // move closer
//        }
//    }
//
//    private fun attack(attacker: CharacterEntity, target: GameEntity) {
//        val vector = target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld)
//
//        val animation = attacker.data.animation
//
//        if (Math.abs(vector.x) >= Math.abs(vector.y)) {
//            if (vector.x >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
//            }
//        } else {
//            if (vector.y >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
//            }
//        }
//
//        // TODO: generalize
//
////        attacker.getControl(PlayerControl::class.java).ifPresent { c ->
////            if (playerControl.getRightWeapon().data.type === WeaponType.BOW) {
////                if (Math.abs(vector.x) >= Math.abs(vector.y)) {
////                    if (vector.x >= 0) {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
////                    } else {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
////                    }
////                } else {
////                    if (vector.y >= 0) {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
////                    } else {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
////                    }
////                }
////            }
////        }
//
//        FXGL.getMasterTimer().runOnceAfter({
//            if (!attacker.isActive || !target.isActive)
//                return@runOnceAfter
//
//            Entities.builder()
//                    .type(EntityType.PROJECTILE)
//                    .at(attacker.boundingBoxComponent.centerWorld)
//                    .viewFromTextureWithBBox("projectiles/arrow2.png")
//                    .with(ProjectileControl(target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld), 60 * 5.0))
//                    .with(OffscreenCleanControl())
//                    .with(OwnerComponent(attacker))
//                    .with(CollidableComponent(true))
//                    .buildAndAttach(FXGL.getApp().gameWorld)
//        }, Duration.seconds(0.8))
//    }
//}