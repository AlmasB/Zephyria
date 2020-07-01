package com.almasb.zeph.ui;

import com.almasb.zeph.character.CharacterEntity;
import com.almasb.zeph.skill.Skill;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Hotbar skills UI.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class HotbarView extends Parent {

    private static final int BG_WIDTH = 767;
    private static final int BG_HEIGHT = 110;

    private CharacterEntity player;

    private HBox framesRoot = new HBox();

    private boolean isMinimized = false;

    public HotbarView(CharacterEntity player) {
        this.player = player;

        initView();
        initSkillFrames();
        initSkillListener();
    }

    private void initView() {
        setLayoutX(getAppWidth() / 2.0 - BG_WIDTH  / 2.0);
        setLayoutY(getAppHeight() - BG_HEIGHT + 5);

        var btn = new Rectangle(30, 20);
        btn.setStroke(Color.WHITE);
        btn.setLayoutX(368);
        btn.setLayoutY(-22);
        btn.setOnMouseClicked(e -> {
            animationBuilder()
                    .duration(Duration.seconds(0.33))
                    .translate(this)
                    .from(isMinimized ? new Point2D(0.0, BG_HEIGHT) : new Point2D(0.0, 0.0))
                    .to(isMinimized ? new Point2D(0.0, 0.0) : new Point2D(0.0, BG_HEIGHT))
                    .buildAndPlay();

            isMinimized = !isMinimized;
        });

        Rectangle border = new Rectangle(BG_WIDTH, BG_HEIGHT);
        border.setStrokeWidth(2);
        border.setArcWidth(10);
        border.setArcHeight(10);

        Shape borderShape = Shape.union(border, new Circle(BG_WIDTH / 2.0, 0.0, 30));
        borderShape.setFill(Color.rgb(25, 25, 25, 0.8));
        borderShape.setStroke(Color.WHITE);

        framesRoot.setLayoutX(1);
        getChildren().addAll(borderShape, framesRoot, btn);
    }

    private void initSkillFrames() {
        framesRoot.getChildren().addAll(
                new ItemView(KeyCode.Q),
                new ItemView(KeyCode.W)
        );

        for (int i = 1; i <= 9; i++) {
            var view = new SkillView(i);

            framesRoot.getChildren().addAll(view);
        }

        framesRoot.getChildren().addAll(
                new ItemView(KeyCode.E),
                new ItemView(KeyCode.R)
        );

        //framesRoot.setMouseTransparent(true);
        framesRoot.setAlignment(Pos.BASELINE_CENTER);
    }


    private static class SkillView extends VBox {
        private StackPane stack = new StackPane();
        private Skill skill = null;

        SkillView(int index) {
            super(5);

            Rectangle frame = new Rectangle(64, 64, null);
            frame.setStroke(Color.AQUAMARINE.darker());
            frame.setStrokeWidth(5);

            stack.getChildren().addAll(frame, new Rectangle());

            var text = getUIFactoryService().newText(index + "", Color.WHITE, 16.0);

            setAlignment(Pos.BASELINE_CENTER);

            getChildren().addAll(stack, text);
        }

        public void setSkill(Skill skill) {
            this.skill = skill;

            stack.getChildren().set(1, texture(skill.getData().getDescription().getTextureName(), 64, 64));

            setCursor(Cursor.HAND);

            // TODO: indicate skill is on cooldown or no mana
//        view.visibleProperty().bind(player.getSp().valueProperty().greaterThan(skill.getManaCost())
//                .and(skill.getCurrentCooldown().lessThanOrEqualTo(0)));
//
            Tooltip tooltip = new Tooltip();

            Text text = new Text();
            text.setFont(Font.font(20));
            text.setFill(Color.WHITE);
            text.setWrappingWidth(200);
            text.textProperty().bind(skill.getDynamicDescription());

            tooltip.setGraphic(text);
            Tooltip.install(this, tooltip);
        }
    }

    private static class ItemView extends VBox {
        ItemView(KeyCode key) {
            super(5);

            var frame = new Rectangle(34, 34, null);
            frame.setStroke(Color.YELLOW.darker());
            frame.setStrokeWidth(2.0);

            setAlignment(Pos.BASELINE_CENTER);

            getChildren().addAll(frame, getUIFactoryService().newText(key.toString(), Color.WHITE, 16.0));
        }
    }

    private void initSkillListener() {
        player.getCharacterComponent().getSkills().forEach(this::addSkill);

        player.getCharacterComponent().getSkills().addListener((ListChangeListener<? super Skill>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(this::addSkill);
                } else if (c.wasRemoved()) {
                    throw new IllegalStateException("Skills must never be removed");
                }
            }
        });
    }

    // this is OK since we don't remove skills, only add
    private int index = 0;

    private void addSkill(Skill skill) {
        SkillView frame = (SkillView) framesRoot.getChildren().stream().filter(v -> v instanceof SkillView).collect(Collectors.toList()).get(index);
        frame.setSkill(skill);

        var bg = new Rectangle(64, 64, Color.color(0, 0, 0, 0.5));

        Text btn = new Text("+");
        btn.setCursor(Cursor.HAND);
        btn.setStroke(Color.YELLOWGREEN.brighter());
        btn.setStrokeWidth(3);
        btn.setFont(Font.font(16));


        var stack = new StackPane(bg, btn);
        stack.visibleProperty().bind(player.getPlayerComponent().getSkillPoints().greaterThan(0).and(skill.getLevelProperty().lessThan(10)));

        frame.stack.getChildren().add(stack);

        final int skillIndex = index;
        btn.setOnMouseClicked(event -> {
            player.getPlayerComponent().increaseSkillLevel(skillIndex);

//            StrokeTransition st = new StrokeTransition(Duration.seconds(1), frame, Color.YELLOW, Color.AQUAMARINE.darker());
//            st.play();
        });

        index++;
    }
}
