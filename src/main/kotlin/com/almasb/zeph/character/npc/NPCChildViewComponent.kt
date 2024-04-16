package com.almasb.zeph.character.npc

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import com.almasb.zeph.Config
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text

/**
 * Provides extra view to an NPC.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class NPCChildViewComponent(private val npcName: String) : ChildViewComponent(0.0, 10.0, isTransformApplied = false) {

    override fun onAdded() {
        super.onAdded()

        val view = makeView()
        viewRoot.children += view

        viewRoot.visibleProperty().bind(entity.viewComponent.parent.hoverProperty())
    }

    private fun makeView(): Node {
        val text = Text()
        text.font = Font.font(14.0)
        text.fill = Color.WHITE
        text.textProperty().bind(SimpleStringProperty(npcName))
        text.translateX = Config.SPRITE_SIZE.toDouble() / 2 - text.layoutBounds.width / 2
        text.translateY = Config.SPRITE_SIZE.toDouble() + 8.0

        return Group(text)
    }
}