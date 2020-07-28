package com.almasb.zeph.data

//class AttackControl : AbstractControl() {
//
//    private lateinit var char: CharacterEntity
//    private lateinit var animation: AnimatedTexture
//
//    val selected = SimpleObjectProperty<Entity>()
//
//    private fun startAttack(attacker: CharacterEntity, target: GameEntity) {
//        if (!enabled)
//            return
//
//        if (!attacker.isActive || !target.isActive)
//            return
//
//        val atkRange = attacker.weapon.value.range
//
//        // are we close enough?
//        if (attacker.positionComponent.distance(target.positionComponent) <= atkRange * Config.tileSize) {
//            val control = attacker.charConrol
//
//            // can we attack already?
//            if (!control.canAttack())
//                return
//
//            control.resetAtkTick()
//
//            attack(attacker, target)
//        } else {
//
//            // move closer
//        }
//    }
//
//    private fun attack(attacker: CharacterEntity, target: GameEntity) {
//        val vector = target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld)
//
//        val animation = attacker.data.animation
//
//        if (Math.abs(vector.x) >= Math.abs(vector.y)) {
//            if (vector.x >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
//            }
//        } else {
//            if (vector.y >= 0) {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
//            } else {
//                animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
//            }
//        }

//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.CHARACTER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
//                    return;
//
//                proj.removeFromWorld();
//
//                CharacterEntity character = (CharacterEntity) target;
//
//                DamageResult damage = player.getPlayerControl().attack(character);
//            }
//        });
//
//        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.PROJECTILE, EntityType.PLAYER) {
//            @Override
//            protected void onCollisionBegin(Entity proj, Entity target) {
//                if (proj.getComponentUnsafe(OwnerComponent.class).getValue() == target)
//                    return;
//
//                proj.removeFromWorld();
//
//                CharacterEntity attacker = (CharacterEntity) proj.getComponentUnsafe(OwnerComponent.class).getValue();
//                CharacterEntity character = (CharacterEntity) target;
//
//                DamageResult damage = attacker.getCharConrol().attack(character);
//            }
//        });

//
//        // TODO: generalize
//
////        attacker.getControl(PlayerControl::class.java).ifPresent { c ->
////            if (playerControl.getRightWeapon().data.type === WeaponType.BOW) {
////                if (Math.abs(vector.x) >= Math.abs(vector.y)) {
////                    if (vector.x >= 0) {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_RIGHT)
////                    } else {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_LEFT)
////                    }
////                } else {
////                    if (vector.y >= 0) {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_DOWN)
////                    } else {
////                        animation.setAnimationChannel(CharacterAnimation.SHOOT_UP)
////                    }
////                }
////            }
////        }
//
//        FXGL.getMasterTimer().runOnceAfter({
//            if (!attacker.isActive || !target.isActive)
//                return@runOnceAfter
//
//            Entities.builder()
//                    .type(EntityType.PROJECTILE)
//                    .at(attacker.boundingBoxComponent.centerWorld)
//                    .viewFromTextureWithBBox("projectiles/arrow2.png")
//                    .with(ProjectileControl(target.boundingBoxComponent.centerWorld.subtract(attacker.boundingBoxComponent.centerWorld), 60 * 5.0))
//                    .with(OffscreenCleanControl())
//                    .with(OwnerComponent(attacker))
//                    .with(CollidableComponent(true))
//                    .buildAndAttach(FXGL.getApp().gameWorld)
//        }, Duration.seconds(0.8))
//    }
//}



//    @Spawns("island")
//    fun newIsland(data: SpawnData): Entity {
//        val size = TILE_SIZE / 16
//
//        val group = Group()
//
//        val W = data.get<Double>("width").toInt() / size
//        val H = data.get<Double>("height").toInt() / size
//
//        val map = Grid(BiomeData::class.java, W, H, BiomeMapGenerator(W, H, 2.4))
//
//        map.forEach { cell: BiomeData ->
//            val texture = if (cell.elevation < 0.2) {
//                // water
//                ColoredTexture(size, size, Color.rgb(99, 155, 255))
//            } else if (cell.elevation < 0.8) {
//                // grass
//                ColoredTexture(size, size, Color.rgb(77, 146, 98, 0.85))
//            } else {
//                // in-land grass / mud?
//                ColoredTexture(size, size, Color.rgb(194, 152, 109, 0.5))
//            }
//
//            texture.translateX = cell.x * size.toDouble()
//            texture.translateY = cell.y * size.toDouble()
//
//            group.children += texture
//        }
//
//        group.clip = Ellipse(W * 1.0, H * 1.0, W * 1.0, H * 1.0)
//
//        val view = ImageView(toImage(group))
//
//        return entityBuilder(data)
//                .view(view)
//                .build()
//    }





// TODO: char selection indicator
//     private ObjectProperty<Entity> selected = new SimpleObjectProperty<>();
//
//    private DropShadow selectedEffect = new DropShadow(20, Color.WHITE);
//        selected.addListener((observable, oldValue, newEntity) -> {
//            if (oldValue != null) {
//                oldValue.getComponent(ViewComponent.class).ifPresent(c -> {
//                    c.getView().setEffect(null);
//                });
//            }
//
//            if (newEntity != null) {
//                newEntity.getComponent(ViewComponent.class).ifPresent(c -> {
//                    c.getView().setEffect(selectedEffect);
//                });
//            }
//
//            playerActionControl.getSelected().set(newEntity);
//        });
//        character.getViewComponent().getView().setOnMouseClicked(null);
//        selected.set(null);