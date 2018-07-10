package com.almasb.zeph.ui;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.InGameWindow;
import com.almasb.zeph.skill.SkillComponent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Hotbar skills UI.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class HotbarView extends InGameWindow {

    private Entity player;

    private Pane root = new Pane();
    private Pane skillsRoot = new Pane();
    private Pane framesRoot = new Pane();

    public HotbarView(Entity player) {
        super("Hotbar", WindowDecor.MINIMIZE);

        this.player = player;

        initMinimizeAnimation();
        initWindow();
        initSkillFrames();
        initSkillListener();
    }

    private void initMinimizeAnimation() {
//        EventHandler<ActionEvent> handler = getRightIcons().get(0).getOnAction();
//        getRightIcons().get(0).setOnAction(e -> {
//            ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), root);
//            st.setFromY(isMinimized() ? 0 : 1);
//            st.setToY(isMinimized() ? 1 : 0);
//            st.play();
//
//            handler.handle(e);
//        });
    }

    private void initWindow() {
        root.getChildren().addAll(skillsRoot, framesRoot);

        relocate(340, 0);

        setBackgroundColor(Color.rgb(25, 25, 133, 0.4));
        setPrefSize(70 * 9, 56 + 70);
        setCanResize(false);
        setContentPane(root);
    }

    private void initSkillFrames() {
        for (int i = 0; i < 9; i++) {
            Rectangle frame = new Rectangle(64, 64, null);
            frame.setArcWidth(25);
            frame.setArcHeight(25);
            frame.setTranslateX(1 + i * 69);
            frame.setTranslateY(30);
            frame.setStroke(Color.AQUAMARINE.darker());
            frame.setStrokeWidth(5);

            framesRoot.getChildren().addAll(frame);
        }

        framesRoot.setMouseTransparent(true);
    }

    private void initSkillListener() {
//        player.getSkills().forEach(this::addSkill);
//
//        player.getSkills().addListener((ListChangeListener<? super SkillComponent>) c -> {
//            while (c.next()) {
//                if (c.wasAdded()) {
//                    c.getAddedSubList().forEach(this::addSkill);
//                } else if (c.wasRemoved()) {
//                    throw new IllegalStateException("Skills must never be removed");
//                }
//            }
//        });
    }

    // this is OK since we don't remove skills, only add
    private int index = 0;

    private void addSkill(SkillComponent skill) {
//        Description desc = skill.getDesc();
//
//        Text btn = new Text("+");
//        btn.setTranslateX(25 + index * 69);
//        btn.setTranslateY(20);
//        btn.setCursor(Cursor.HAND);
//        btn.setStroke(Color.YELLOWGREEN.brighter());
//        btn.setStrokeWidth(3);
//        btn.setFont(Font.font(16));
//        btn.visibleProperty().bind(player.getSkillPoints().greaterThan(0).and(skill.getLevel().lessThan(10)));
//
//        final int skillIndex = index;
//        btn.setOnMouseClicked(event -> {
//            //player.getControlUnsafe(PlayerComponent.class).increaseSkillLevel(skillIndex);
//
//            Rectangle frame = (Rectangle) framesRoot.getChildren().get(skillIndex);
//
//            StrokeTransition st = new StrokeTransition(Duration.seconds(1), frame, Color.YELLOW, Color.AQUAMARINE.darker());
//            st.play();
//        });
//
//        Texture view = FXGL.getAssetLoader().loadTexture(desc.getTextureName().get());
//        view.setFitWidth(62);
//        view.setFitHeight(62);
//        view.setTranslateX(2 + index * 69);
//        view.setTranslateY(30);
//        view.setCursor(Cursor.HAND);
//        view.visibleProperty().bind(player.getSp().valueProperty().greaterThan(skill.getManaCost())
//                .and(skill.getCurrentCooldown().lessThanOrEqualTo(0)));
//
//        Tooltip tooltip = new Tooltip();
//
//        Text text = new Text();
//        text.setFont(Font.font(20));
//        text.setFill(Color.WHITE);
//        text.setWrappingWidth(200);
//        text.textProperty().bind(desc.getDescription());
//
//        tooltip.setGraphic(text);
//        Tooltip.install(view, tooltip);
//
//        skillsRoot.getChildren().addAll(view, btn);
//
//        index++;
    }
}
