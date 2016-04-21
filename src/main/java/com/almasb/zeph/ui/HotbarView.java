package com.almasb.zeph.ui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.skill.SkillEntity;
import javafx.collections.ListChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class HotbarView extends InGameWindow {

    private Pane root = new Pane();

    public HotbarView(PlayerEntity player) {
        super("Hotbar", WindowDecor.MINIMIZE);

        relocate(400, 600);

        setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(600, 100);
        setResizableWindow(false);

        Texture background = FXGL.getAssetLoader().loadTexture("ui/hotbar.png");
        root.getChildren().add(background);

        player.getSkills().forEach(this::addSkill);

        player.getSkills().addListener((ListChangeListener<? super SkillEntity>) c -> {
            // TODO:
            System.out.println("TODO:");
        });

        setContentPane(root);
    }

    private void addSkill(SkillEntity skill) {
        DescriptionComponent desc = skill.getDesc();

        Texture view = FXGL.getAssetLoader().loadTexture(desc.getTextureName());
        view.setFitWidth(64);
        view.setFitHeight(64);

        view.setCursor(Cursor.HAND);

        Tooltip tooltip = new Tooltip();

        Text text = new Text();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);
        text.setWrappingWidth(200);
        text.setText(desc.getDescription());

        tooltip.setGraphic(text);
        Tooltip.install(view, tooltip);

        root.getChildren().add(view);
    }
}
