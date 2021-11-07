package com.almasb.zeph

import com.almasb.zeph.character.DataDSL

val emptyDescription = Description(
        -1,
        "",
        "",
        ""
)

// TODO: extract hardcoded text and in appendDescription() below
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
        val id: Int,
        val name: String,
        val description: String,
        val textureName: String
) {

    fun appendDescription(descriptionText: String): Description {
        if (description == "No description")
            return copy(description = descriptionText)

        return copy(description = this.description + "\n$descriptionText")
    }
}