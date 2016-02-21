package com.almasb.zeph.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.zeph.entity.character.CharacterControl;

public class AgressiveControl extends AbstractControl {

    private int lineOfSight;
    private Entity target;
    private CharacterControl character;

    public AgressiveControl(int lineOfSight, Entity target) {
        this.lineOfSight = lineOfSight;
        this.target = target;
    }

    @Override
    public void onAdded(Entity entity) {
        //character = entity.getProperty("data");
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
//        if (target.getPosition().distance(entity.getPosition()) <= lineOfSight) {
//            entity.fireFXGLEvent(new FXGLEvent(Event.ATTACKING, target));
//        }
    }
}
