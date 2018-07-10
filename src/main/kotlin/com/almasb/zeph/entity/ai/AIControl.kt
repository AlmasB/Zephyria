//package com.almasb.zeph.entity.ai
//
//import com.almasb.fxgl.ai.pathfinding.AStarGrid
//import com.almasb.fxgl.ecs.AbstractControl
//import com.almasb.fxgl.ecs.Entity
//import com.almasb.fxgl.app.FXGL
//import com.almasb.zeph.Config
//import com.almasb.zeph.ZephyriaApp
//import com.almasb.zeph.entity.character.CharacterEntity
//import com.almasb.zeph.entity.character.Entity
//
///**
// *
// *
// * @author Almas Baimagambetov (almaslvl@gmail.com)
// */
//class AIControl : AbstractControl() {
//
//    lateinit var char: CharacterEntity
//
//    val moveControl = RandomWanderControl()
//    val attackControl = AttackControl()
//
//    private var attacking = false
//
//    private val range = 5
//    private val grid: AStarGrid
//    private val player: Entity
//
//    init {
//        grid = (FXGL.getApp() as ZephyriaApp).grid
//        player = (FXGL.getApp() as ZephyriaApp).player
//
//        attackControl.selected.value = player
//    }
//
//    override fun onAdded(entity: Entity) {
//        char = entity as CharacterEntity
//
//        attackControl.enabled = false
//
//        char.addControl(moveControl)
//        char.addControl(attackControl)
//    }
//
//    override fun onUpdate(entity: Entity, tpf: Double) {
//        if (isInRange()) {
//            if (!attacking) {
//                moveControl.enabled = false
//                attackControl.enabled = true
//                attacking = true
//            }
//        } else {
//            if (attacking) {
//                moveControl.enabled = true
//                attackControl.enabled = false
//                attacking = false
//            }
//        }
//    }
//
//    private fun isInRange() = player.positionComponent.distance(char.positionComponent) <= range * Config.tileSize
//}