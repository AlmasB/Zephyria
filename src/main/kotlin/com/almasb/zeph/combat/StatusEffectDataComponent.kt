package com.almasb.zeph.combat

import com.almasb.ents.AbstractComponent
import javafx.beans.property.SimpleDoubleProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class StatusEffectDataComponent(val status: Status, duration: Double) : AbstractComponent() {

    val duration = SimpleDoubleProperty(duration)
}