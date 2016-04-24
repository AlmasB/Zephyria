package com.almasb.zeph.ui;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.zeph.entity.DescriptionComponent;
import com.almasb.zeph.entity.character.PlayerEntity;
import com.almasb.zeph.entity.character.control.PlayerControl;
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

    private PlayerEntity player;

    private Pane root = new Pane();

    public HotbarView(PlayerEntity player) {
        super("Hotbar", WindowDecor.MINIMIZE);

        this.player = player;

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

    // TODO: remove ad-hoc
    private int index = 0;

    private void addSkill(SkillEntity skill) {
        DescriptionComponent desc = skill.getDesc();

        Texture view = FXGL.getAssetLoader().loadTexture(desc.getTextureName().get());
        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setTranslateX(40 + index * 60);
        view.setTranslateY(46);

        view.setCursor(Cursor.HAND);

        Text textLevel = new Text();
        textLevel.setTranslateX(45 + index * 55);
        textLevel.setTranslateY(110);
        textLevel.setFill(Color.WHITE);
        textLevel.textProperty().bind(skill.getLevel().asString("Lv. %d"));

        Text btn = new Text("+");
        btn.setTranslateX(45 + index * 60);
        btn.setTranslateY(25);
        btn.setCursor(Cursor.HAND);
        btn.setStroke(Color.YELLOWGREEN.brighter());
        btn.setStrokeWidth(3);
        btn.setFont(Font.font(16));
        btn.visibleProperty().bind(player.getSkillPoints().greaterThan(0).and(skill.getLevel().lessThan(10)));

        final int skillIndex = index;
        btn.setOnMouseClicked(event -> {
            player.getControlUnsafe(PlayerControl.class).increaseSkillLevel(skillIndex);
        });

        Tooltip tooltip = new Tooltip();

        Text text = new Text();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);
        text.setWrappingWidth(200);
        text.textProperty().bind(desc.getDescription());

        tooltip.setGraphic(text);
        Tooltip.install(view, tooltip);

        root.getChildren().addAll(view, btn, textLevel);

        index++;
    }
}
