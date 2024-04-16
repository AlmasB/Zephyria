package com.almasb.zeph.character.npc

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterClass
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.character.char
import com.almasb.zeph.combat.Attribute
import com.almasb.zeph.emptyDescription

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

@DataDSL
class NPCDataBuilder(
        var description: Description = emptyDescription,
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
) {

    fun toCharData(): CharacterData {
        val npcData = this

        return char {
            desc {
                id = npcData.description.id
                name = npcData.description.name
                description = npcData.description.description
                textureName = npcData.description.textureName
            }

            charClass = CharacterClass.NOVICE

            attributes {
                Attribute.values().forEach {
                    it +1
                }
            }
        }
    }
}