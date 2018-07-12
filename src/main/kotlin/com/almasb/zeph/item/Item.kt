package com.almasb.zeph.item

import com.almasb.zeph.Description
import javafx.beans.property.SimpleStringProperty

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class Item(val description: Description) {

    val dynamicDescription = SimpleStringProperty("")

    // TODO: copy() ?
}