package com.almasb.zeph.data

import com.almasb.zeph.character.npc.npc

/**
 * NPCs [2500-2999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class NPCs {

    val ROSIE = npc {
        desc {
            id = 2500
            name = "Rosie"
            textureName = "chars/npcs/rosie.png"
        }

        dialogueName = "dialogues/rosie.json"
    }

    val CHARLES = npc {
        desc {
            id = 2501
            name = "Charles"
            textureName = "chars/npcs/charles.png"
        }

        dialogueName = "dialogues/charles.json"
    }
}