package com.almasb.zeph.quest

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.character.npc.NPCData
import com.almasb.zeph.combat.Experience
import com.almasb.zeph.data.MapData
import com.almasb.zeph.data.MapPoint
import com.almasb.zeph.emptyDescription
import com.almasb.zeph.item.ItemData

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

@DataDSL
class QuestDataBuilder(
        var description: Description = emptyDescription,
        var rewardMoney: Int = 0,
        var rewardXP: Experience = Experience(0, 0, 0),
        val rewardItems: MutableList<ItemData> = arrayListOf(),
        val requiredItems: MutableMap<ItemData, Int> = linkedMapOf(),
        val requiredMonsters: MutableMap<CharacterData, Int> = linkedMapOf(),
        val requiredTalkNPCs: MutableList<NPCData> = arrayListOf(),
        val requiredLocations: MutableMap<MapData, MapPoint> = linkedMapOf()
) {

    fun desc(setup: DescriptionBuilder.() -> Unit) {
        val builder = DescriptionBuilder()
        builder.setup()
        description = builder.build()
    }

    fun collect(setup: CollectQuestDataBuilder.() -> Unit) {
        val builder = CollectQuestDataBuilder()
        builder.setup()

        requiredItems.putAll(builder.requiredItems)
    }

    fun kill(setup: KillQuestDataBuilder.() -> Unit) {
        val builder = KillQuestDataBuilder()
        builder.setup()

        requiredMonsters.putAll(builder.requiredMonsters)
    }

    fun go(setup: GoQuestDataBuilder.() -> Unit) {
        val builder = GoQuestDataBuilder()
        builder.setup()

        requiredLocations.putAll(builder.requiredLocations)
    }

    fun talk(vararg npcs: NPCData) {
        requiredTalkNPCs += npcs
    }

    fun build(): QuestData {
        requiredItems.forEach { (itemData, numItems) ->
            description = description.appendDescription("Collect: ${itemData.description.name} x$numItems")
        }

        requiredMonsters.forEach { (mobData, numItems) ->
            description = description.appendDescription("Kill: ${mobData.description.name} x$numItems")
        }

        requiredLocations.forEach { (map, point) ->
            description = description.appendDescription("Go to: $map at $point")
        }

        return QuestData(
                description,
                rewardMoney,
                rewardXP,
                rewardItems,
                requiredItems,
                requiredMonsters,
                requiredTalkNPCs,
                requiredLocations
        )
    }
}

@DataDSL
class CollectQuestDataBuilder {

    val requiredItems: MutableMap<ItemData, Int> = linkedMapOf()

    infix fun ItemData.x(amount: Int) {
        requiredItems[this] = amount
    }
}

@DataDSL
class KillQuestDataBuilder {

    val requiredMonsters: MutableMap<CharacterData, Int> = linkedMapOf()

    infix fun CharacterData.x(amount: Int) {
        requiredMonsters[this] = amount
    }
}

@DataDSL
class GoQuestDataBuilder {

    val requiredLocations: MutableMap<MapData, MapPoint> = linkedMapOf()

    infix fun MapData.at(point: MapPoint) {
        requiredLocations[this] = point
    }

    fun p(x: Int, y: Int) = MapPoint(x, y)
}

@DataDSL
fun quest(setup: QuestDataBuilder.() -> Unit): QuestData {
    val builder = QuestDataBuilder()
    builder.setup()
    return builder.build()
}

data class QuestData(
        val description: Description,
        val rewardMoney: Int,
        val rewardXP: Experience,
        val rewardItems: List<ItemData>,

        val requiredItems: Map<ItemData, Int>,
        val requiredMonsters: Map<CharacterData, Int>,

        val requiredTalkNPCs: List<NPCData>,

        val requiredLocations: Map<MapData, MapPoint>
)