package com.almasb.zeph.entity.character.component

import com.almasb.ents.AbstractComponent
import javafx.beans.property.SimpleIntegerProperty

/**
 * TODO: probably move to PlayerEntity?
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PlayerDataComponent : AbstractComponent() {

    val attributePoints = SimpleIntegerProperty()
    val statsPoints = SimpleIntegerProperty()

    val money = SimpleIntegerProperty()

    val statLevel = SimpleIntegerProperty()
    val jobLevel = SimpleIntegerProperty()
}