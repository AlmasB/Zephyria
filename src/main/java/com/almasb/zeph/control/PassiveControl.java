package com.almasb.zeph.control;


import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.zeph.Events.Event;

public class PassiveControl extends AbstractControl {

    private boolean passive = true;
    private int attackRange;
    private Entity target;

    private double timer = 0;

    public PassiveControl(int attackRange) {
        this.attackRange = attackRange;
    }

    @Override
    public void onAdded(Entity entity) {
//        entity.addFXGLEventHandler(Event.BEING_ATTACKED, event -> {
//            target = event.getSource();
//            passive = false;
//        });
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {
//        if (!passive) {
//            if (target.getPosition().distance(entity.getPosition()) <= attackRange) {
//                entity.fireFXGLEvent(new FXGLEvent(Event.ATTACKING, target));
//                timer = 0;
//            }
//            else {
//                timer += 0.016;
//                if (timer >= 3.0)
//                    passive = true;
//            }
//        }

    }
}
