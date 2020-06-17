package com.almasb.zeph.data

import com.almasb.fxgl.dsl.components.Effect
import com.almasb.fxgl.dsl.components.EffectComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.zeph.character.CharacterEntity
import com.almasb.zeph.combat.Element
import com.almasb.zeph.item.usableItem
import javafx.util.Duration

/**
 * Usable items id range [6000-6999].
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class UsableItem {

    fun HEALING_POTION() = usableItem {
        desc {
            id = 6000
            name = "Healing Potion"
            description = "Restores 50 HP"
            textureName = "items/usable/healing_potion.png"
        }

        beforeUseScript = {
            // everyone can use it
            true
        }

        onUseScript = {
            it.hp.restore(50.0)
        }
    }

    fun MANA_POTION() = usableItem {
        desc {
            id = 6001
            name = "Mana Potion"
            description = "Restores 30 SP"
            textureName = "items/usable/mana_potion.png"
        }

        onUseScript = {
            it.sp.restore(30.0)
        }
    }

    fun FIRE_MIXTURE() = usableItem {
        desc {
            id = 6002
            name = "Fire Mixture"
            description = "Imbues the equipped weapon with the FIRE element for 2 minutes"
            textureName = "items/usable/fire_mixture.png"
        }

        onUseScript = {
            it.getComponent(EffectComponent::class.java).startEffect(WeaponElementChangeEffect(it, Element.FIRE, Duration.minutes(2.0)))
        }
    }
}

private class WeaponElementChangeEffect(val char: CharacterEntity, val element: Element, duration: Duration) : Effect(duration) {
    private lateinit var oldElement: Element

    override fun onStart(entity: Entity) {
        oldElement = char.characterComponent.weaponElement.value
        char.characterComponent.weaponElement.value = element
    }

    override fun onEnd(entity: Entity) {
        char.characterComponent.weaponElement.value = oldElement
    }
}