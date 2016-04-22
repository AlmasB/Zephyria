package com.almasb.zeph.ui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.skill.SkillEntity;
import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class HotbarView extends InGameWindow {

    private Pane root = new Pane();

    public HotbarView(PlayerEntity player) {
        super("Hotbar", WindowDecor.MINIMIZE);

        relocate(340, 0);

        setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(590, 150);
        setResizableWindow(false);

        Texture background = FXGL.getAssetLoader().loadTexture("ui/hotbar.png");
        root.getChildren().add(background);

        player.getSkills().forEach(this::addSkill);

        player.getSkills().addListener((ListChangeListener<? super SkillEntity>) c -> {
            // TODO:
            System.out.println("TODO:");
        });

        setContentPane(root);

        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
        getRightIcons().get(0).setOnAction(e -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), root);
            st.setFromY(isMinimized() ? 0 : 1);
            st.setToY(isMinimized() ? 1 : 0);
            st.play();

            handler.handle(e);
        });
    }

    private void addSkill(SkillEntity skill) {
        DescriptionComponent desc = skill.getDesc();

        Texture view = FXGL.getAssetLoader().loadTexture(desc.getTextureName());
        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setTranslateX(40);
        view.setTranslateY(46);

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
