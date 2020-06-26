package com.almasb.zeph.quest

import com.almasb.zeph.Description
import com.almasb.zeph.DescriptionBuilder
import com.almasb.zeph.character.CharacterData
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.character.DataDSL
import com.almasb.zeph.combat.*
import com.almasb.zeph.item.*

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */



@DataDSL
class QuestDataBuilder(
        var description: Description = Description(),
        var rewardMoney: Int = 0,
        var rewardXP: Experience = Experience(0, 0, 0),
        val rewardItems: MutableList<ItemData> = arrayListOf(),
        val requiredItems: MutableMap<ItemData, Int> = linkedMapOf(),
        val requiredMonsters: MutableMap<CharacterData, Int> = linkedMapOf()
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
        TODO()
    }

    fun go(setup: GoQuestDataBuilder.() -> Unit) {
        TODO()
    }

    fun talk(setup: TalkQuestDataBuilder.() -> Unit) {
        TODO()
    }

    fun build(): QuestData {
        return QuestData(
                description,
                rewardMoney,
                rewardXP,
                rewardItems,
                requiredItems,
                requiredMonsters,
                arrayListOf(),
                ""
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

}

@DataDSL
class TalkQuestDataBuilder {

}

@DataDSL
class GoQuestDataBuilder {

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

        // TODO: talk to quests
        val requiredTalkNPCs: List<Any>,

        // TODO: go to quests
        val requiredLocations: Any
)