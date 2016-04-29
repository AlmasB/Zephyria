package com.almasb.zeph

import com.almasb.astar.AStarGrid
import com.almasb.zeph.entity.character.PlayerEntity
import com.google.inject.Singleton

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@Singleton
class GameDataService {

    lateinit var player: PlayerEntity

    lateinit var grid: AStarGrid
}