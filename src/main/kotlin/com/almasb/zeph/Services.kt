package com.almasb.zeph

import com.almasb.fxgl.app.ServiceType

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Services {
    val GAME_APP = object : ServiceType<GameDataService> {
        override fun serviceProvider(): Class<out GameDataService> {
            return GameDataService::class.java
        }

        override fun service(): Class<GameDataService> {
            return GameDataService::class.java
        }
    }
}