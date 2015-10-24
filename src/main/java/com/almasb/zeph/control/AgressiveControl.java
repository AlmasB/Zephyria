package com.almasb.zeph.control;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.FXGLEvent;
import com.almasb.zeph.Events.Event;
import com.almasb.zeph.entity.character.GameCharacter;

public class AgressiveControl extends AbstractControl {

    private int lineOfSight;
    private Entity target;
    private GameCharacter character;

    public AgressiveControl(int lineOfSight, Entity target) {
        this.lineOfSight = lineOfSight;
        this.target = target;
    }

    @Override
    protected void initEntity(Entity entity) {
        //character = entity.getProperty("data");
    }

    @Override
    public void onUpdate(Entity entity) {
        if (target.getPosition().distance(entity.getPosition()) <= lineOfSight) {
            entity.fireFXGLEvent(new FXGLEvent(Event.ATTACKING, target));
        }
    }
}
