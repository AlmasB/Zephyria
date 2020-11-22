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

    val SARAH = npc {
        desc {
            id = 2502
            name = "Sarah"
            textureName = "chars/npcs/sarah.png"
        }

        dialogueName = "dialogues/sarah.json"
    }

    val JULES = npc {
        desc {
            id = 2503
            name = "Jules"
            textureName = "chars/npcs/jules.png"
        }
    }

    val CORAGH = npc {
        desc {
            id = 2504
            name = "Coragh"
            textureName = "chars/npcs/coragh.png"
        }
    }
}