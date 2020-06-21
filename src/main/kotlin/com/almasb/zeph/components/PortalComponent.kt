package com.almasb.zeph.components

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.zeph.EntityType
import com.almasb.zeph.ZephyriaApp

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PortalComponent(
        val mapName: String,
        val toCellX: Int,
        val toCellY: Int
) : Component() {

    override fun onUpdate(tpf: Double) {

        // TODO: fix bbox size / collision

        if (getGameWorld().getSingleton(EntityType.PLAYER).isColliding(entity)) {
            FXGL.getGameController().gotoLoading(Runnable {
                FXGL.getAppCast<ZephyriaApp>().gotoMap(mapName, toCellX, toCellY)
            })
        }
    }
}