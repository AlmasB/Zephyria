package com.almasb.zeph.character.npc

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.DataDSL

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

@DataDSL
class NPCDataBuilder(
        var description: Description = Description(),
        var dialogueName: String = ""
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): NPCData {
        return NPCData(description, dialogueName)
    }
}

@DataDSL
fun npc(setup: NPCDataBuilder.() -> Unit): NPCData {
    val builder = NPCDataBuilder()
    builder.setup()
    return builder.build()
}

data class NPCData(
        val description: Description,
        val dialogueName: String
)