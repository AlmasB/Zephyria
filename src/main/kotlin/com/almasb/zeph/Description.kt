package com.almasb.zeph

import com.almasb.zeph.character.DataDSL

@DataDSL
class DescriptionBuilder(
        var id: Int = 0,
        var name: String = "Unnamed",
        var description: String = "No description",
        var textureName: String = ""
) {

    fun build(): Description = Description(id, name, description, textureName)
}


/**
 * Static immutable description of an object.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class Description(
        val id: Int = -1,
        val name: String = "Unnamed object",
        val description: String = "No description",
        val textureName: String = "null_object.png"
)