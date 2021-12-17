package com.almasb.zeph.gameplay

import com.almasb.fxgl.core.EngineService

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ClockService : EngineService() {

    val clock = Clock(60)

    override fun onGameUpdate(tpf: Double) {
        clock.onUpdate(tpf)
    }
}