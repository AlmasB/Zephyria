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
        var dialogueName: String = "",
        var textureNameFull: String = ""
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun build(): NPCData {
        if (textureNameFull.isEmpty()) {
            textureNameFull = description.textureName.replace(".png", "_full.png")
        }

        return NPCData(
                description,
                dialogueName,
                textureNameFull
        )
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
        val dialogueName: String,
        val textureNameFull: String
)