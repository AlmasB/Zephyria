package com.almasb.zeph.data

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Maps {

    // TODO: DSL map { }
    val TUTORIAL_MAP = MapData()
}

// TODO: extract from package?

class MapData(var name: String = "") {

}

data class MapPoint(val x: Int, val y: Int)