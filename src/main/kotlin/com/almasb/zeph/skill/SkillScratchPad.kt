package com.almasb.zeph.skill

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

// onHotbarSkill




// TODO: generalize to use skill or attack
//private open fun useTargetSkill(target: Entity): Unit {
    //val skill: SkillComponent = playerComponent.skills.get(selectedSkillIndex)

//        if (skill.isOnCooldown() || skill.getManaCost().intValue() > playerComponent.getSp().getValue())
//            return;
    //val vector = target.boundingBoxComponent.centerWorld.subtract(player.getBoundingBoxComponent().centerWorld)

//        AnimatedTexture animation = player.getData().getAnimation();
//
//        if (Math.abs(vector.getX()) >= Math.abs(vector.getY())) {
//            if (vector.getX() >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.CAST_RIGHT);
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.CAST_LEFT);
//            }
//        } else {
//            if (vector.getY() >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.CAST_DOWN);
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.CAST_UP);
//            }
//        }
//
//        getMasterTimer().runOnceAfter(() -> {
//            if (!player.isActive() || !target.isActive())
//                return;
//
//            // we are using a skill
//
//            if (skill.getData().getHasProjectile()) {
//                Entities.builder()
//                        .type(EntityType.SKILL_PROJECTILE)
//                        .at(player.getBoundingBoxComponent().getCenterWorld())
//                        .viewFromTextureWithBBox(skill.getData().getTextureName())
//                        .with(new ProjectileControl(target.getBoundingBoxComponent().getCenterWorld().subtract(player.getBoundingBoxComponent().getCenterWorld()), 6))
//                        .with(new OffscreenCleanComponent())
//                        .with(new OwnerComponent(skill))
//                        .with(new CollidableComponent(true))
//                        .buildAndAttach(getGameWorld());
//            } else {
//                if (player.isInWeaponRange(target)) {
//
//                    SkillUseResult result = playerComponent.useTargetSkill(skill, target);
//                    showDamage(result.getDamage(), target.getPositionComponent().getValue());
//
//                    if (target.getHp().getValue() <= 0) {
//                        playerKilledChar(target);
//                    }
//
//                } else {
//                    playerActionControl.moveTo(target.getTileX(), target.getTileY());
//                }
//            }
//
//        }, Duration.seconds(0.8));
//}

//private void useAreaSkill() {
//        // TODO: we should fire projectile based on skill data component
//        SkillUseResult result = playerComponent.useAreaSkill(selectedSkillIndex, getInput().getMousePositionWorld());
//    }



// TODO: at some point we need to check if it's ally or enemy based on skill target type
//                if (selectingSkillTargetChar) {
//                    if (newEntity instanceof CharacterEntity) {
//                        useTargetSkill((CharacterEntity) newEntity);
//                    }
//
//                    selectingSkillTargetChar = false;
//                    selectedSkillIndex = -1;
//                    selected.set(null);
//                }



//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SKILL_PROJECTILE, EntityType.CHARACTER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                SkillEntity skill = (SkillEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();
//
//                proj.removeFromWorld();
//
//                CharacterEntity character = (CharacterEntity) target;
//
//                SkillUseResult result = playerComponent.useTargetSkill(skill, character);
//                showDamage(result.getDamage(), character.getPositionComponent().getValue());
//
//                if (character.getHp().getValue() <= 0) {
//                    playerKilledChar(character);
//                }
//            }
//        });