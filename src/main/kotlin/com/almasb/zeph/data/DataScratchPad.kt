package com.almasb.zeph.data

//    object Skill {
//
//        object Enchanter {
//            //            public static final int MAGIC_SHIELD = 7220;
//            //            public static final int ASTRAL_PROTECTION = 7221;
//            //            public static final int MIND_BLAST = 7222;
//            //            public static final int CURSE_OF_WITCHCRAFT = 7223;
//            //            public static final int MANA_BURN = 7224;
//
//            //            public static final String MAGIC_SHIELD = "Increases Armor rating for the duration";
//            //            public static final String ASTRAL_PROTECTION = "Passively increases MDEF";
//            //            public static final String MIND_BLAST = "Drains % of target's SP. Increases mana cost of all target's skills";
//            //            public static final String CURSE_OF_WITCHCRAFT = "Target cannot use skills for the duration";
//            //            public static final String MANA_BURN = "Burns target's SP and deals damage based on the SP burnt";
//        }
//
//
//        object Rogue {
//            //            public static final int SHAMELESS = 7130;
//            //            public static final int DOUBLE_STRIKE = 7131;
//            //            public static final int TRIPLE_STRIKE = 7132;
//            //            public static final int FIVE_FINGER_DEATH_PUNCH = 7133;
//            //            public static final int CRITICAL_STRIKE = 7134;
//
//            //            public static final String SHAMELESS = "Deals more damage if target's % HP is lower than yours. No cooldown but consumes mana";
//            //            public static final String DOUBLE_STRIKE = "Quickly performs two attacks with a chance to stun the target";
//            //            public static final String TRIPLE_STRIKE = "Quickly performs three attacks. Deals more damage if target is stunned";
//            //            public static final String CRITICAL_STRIKE = "Strikes the target with high chance of crit. Crit damage is greater for this skill";
//            //            public static final String FIVE_FINGER_DEATH_PUNCH = "Deals devastating damage to unarmoured targets";
//        }
//
//        object Ranger {
//            //            public static final int PINPOINT_WEAKNESS = 7230;
//            //            public static final int BULLSEYE = 7231;
//            //            public static final int FAST_REFLEXES = 7232;
//            //            public static final int ENCHANTED_ARROW = 7233;
//
//            //            public static final String FAST_REFLEXES = "Increases ASPD for the duration";
//            //            public static final String ENCHANTED_ARROW = "Stuns target. Stun lasts longer for target's with high armor rating";

//            //            public static final String PINPOINT_WEAKNESS = "Decreases target's defense for the duration";
//            //            public static final String BULLSEYE = "Deals armor ignoring damage to target."
//            //                    + "Target's defense is not ignored. "
//            //                    + "Damage is based on caster's DEX";
//        }
//    }


//    ////
//
//    ////
//    ////        // MAGE SKILL SET
//    ////

//    ////

//    ////

//    ////

//    ////
//
//    ////

//    ////

//    ////
//    ////        addSkill(new Skill(ID.Skill.Enchanter.MAGIC_SHIELD, "Magic Shield", Desc.Skill.Enchanter.MAGIC_SHIELD, true, 60.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 7104420977798092420L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 5 + level * 5;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                caster.addEffect(new Effect((25.0f), ID.Skill.Enchanter.MAGIC_SHIELD,
//    ////                        new Rune[] {},
//    ////                        new Essence[] {
//    ////                        new Essence(Stat.ARM, 5*level)
//    ////                }
//    ////                        ));
//    ////
//    ////                useResult = new SkillUseResult("ARM +" + 5*level);
//    ////            }
//    ////
//    ////            @Override
//    ////            public boolean isSelfTarget() {
//    ////                return true;
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Wizard.MENTAL_STRIKE, "Mental Strike", Desc.Skill.Wizard.MENTAL_STRIKE, true, 20.0f) {
//    ////
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -55046003688618764L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 5 + level * 5;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = caster.getTotalStat(Stat.MATK) * (1 + level*0.1f);
//    ////                caster.dealPureDamage(target, dmg);
//    ////
//    ////                useResult = new SkillUseResult(dmg + ",PURE");
//    ////            }
//    ////        });
//    ////
//    ////        // SCOUT SKILL SET
//    ////
//    ////        addSkill(new Skill(ID.Skill.Rogue.CRITICAL_STRIKE, "Critical Strike", Desc.Skill.Rogue.CRITICAL_STRIKE, true, 20.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -7584376145233708322L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level * 2;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = caster.getTotalStat(Stat.ATK) + 15 + 5 * level;
//    ////                caster.addBonusStat(Stat.CRIT_CHANCE, 50 + level * 3);
//    ////                int d = caster.dealPhysicalDamage(target, dmg);
//    ////                caster.addBonusStat(Stat.CRIT_CHANCE, -(50 + level * 3));
//    ////
//    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Ranger.PINPOINT_WEAKNESS, "Pinpoint Weakness", Desc.Skill.Ranger.PINPOINT_WEAKNESS, true, 15.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 2458408699758838323L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level * 2;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                target.addEffect(new Effect((10.0f), ID.Skill.Ranger.PINPOINT_WEAKNESS,
//    ////                        new Rune[] {},
//    ////                        new Essence[] {
//    ////                        new Essence(Stat.ARM, -2*level)
//    ////                }
//    ////                        ));
//    ////
//    ////                useResult = new SkillUseResult("ARM -" + 2*level);
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Rogue.DOUBLE_STRIKE, "Double Strike", Desc.Skill.Rogue.DOUBLE_STRIKE, true, 8.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 5685022402103377679L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level * 2;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                int dmg1 = caster.attack(target);
//    ////                int dmg2 = caster.attack(target);
//    ////                boolean stun = false;
//    ////                if (GameMath.checkChance(level*5)) {
//    ////                    target.addStatusEffect(new StatusEffect(Status.STUNNED, 2.5f));
//    ////                    stun = true;
//    ////                }
//    ////
//    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg1) + "," + GameMath.normalizeDamage(dmg2)
//    ////                        + (stun ? ",STUNNED" : ",X2"));
//    ////            }
//    ////        });
//    ////
//
//    ////
//    ////        addSkill(new Skill(ID.Skill.Rogue.SHAMELESS, "Shameless", Desc.Skill.Rogue.SHAMELESS, true, 0.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 2306928037030551618L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 10;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = caster.getTotalStat(Stat.ATK);
//    ////                float casterHPFactor = caster.getHP() / caster.getTotalStat(Stat.MAX_HP);
//    ////                float targetHPFactor = target.getHP() / target.getTotalStat(Stat.MAX_HP);
//    ////                if (casterHPFactor > targetHPFactor) {
//    ////                    dmg += level * 0.1f * (casterHPFactor - targetHPFactor) * dmg;
//    ////                }
//    ////
//    ////                int d = caster.dealPhysicalDamage(target, dmg);
//    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
//    ////            }
//    ////        });
//
//    ////
//    ////        addSkill(new Skill(ID.Skill.Rogue.TRIPLE_STRIKE, "Triple Strike", Desc.Skill.Rogue.TRIPLE_STRIKE, true, 40.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 8295208480454374043L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = caster.getTotalStat(Stat.ATK);
//    ////                if (target.hasStatusEffect(Status.STUNNED)) {
//    ////                    dmg += level * 15;
//    ////                }
//    ////
//    ////                int dmg1 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
//    ////                int dmg2 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
//    ////                int dmg3 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
//    ////
//    ////                useResult = new SkillUseResult(dmg1 + "," + dmg2 + "," + dmg3 + ",X3");
//    ////            }
//    ////        });
//    ////

//    ////
//    ////        addSkill(new Skill(ID.Skill.Crusader.PRECISION_STRIKE, "Precision Strike", Desc.Skill.Crusader.PRECISION_STRIKE, true, 20.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 2024648263069876L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = caster.getTotalStat(Stat.ATK) + (caster.getTotalAttribute(Attribute.STRENGTH) / 10) * level;
//    ////                caster.dealPureDamage(target, dmg);
//    ////
//    ////                useResult = new SkillUseResult((int)dmg);
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Crusader.DIVINE_ARMOR, "Divine Armor", Desc.Skill.Crusader.DIVINE_ARMOR, false, 0.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -7936080589333242098L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 0;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                // impl is in ARMOR_MASTERY
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Crusader.FAITH, "Faith", Desc.Skill.Crusader.FAITH, false, 0.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 4325213967370795918L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 0;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                // impl is in WARRIOR_HEART
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Gladiator.ENDURANCE, "Endurance", Desc.Skill.Gladiator.ENDURANCE, true, 40.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -7936080589333242098L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                caster.addEffect(new Effect(15.0f, ID.Skill.Gladiator.ENDURANCE, new Rune[] {},
//    ////                        new Essence[] {
//    ////                        new Essence(Stat.DEF, 2*level),
//    ////                        new Essence(Stat.HP_REGEN, 2*level)
//    ////                }));
//    ////
//    ////                useResult = new SkillUseResult("DEF +" + level*2 + ", HP REGEN +" + 2*level);
//    ////            }
//    ////
//    ////            @Override
//    ////            public boolean isSelfTarget() {
//    ////                return true;
//    ////            }
//    ////        });

//    ////
//    ////        addSkill(new Skill(ID.Skill.Enchanter.MANA_BURN, "Mana Burn", Desc.Skill.Enchanter.MANA_BURN, true, 20.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 1031700846462374399L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                int oldSP = target.getSP();
//    ////                target.setSP(Math.max(oldSP - 50 * level, 0));
//    ////                int dmg = caster.dealMagicalDamage(target, oldSP-target.getSP(), Element.NEUTRAL);
//    ////
//    ////                useResult = new SkillUseResult(dmg);
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Enchanter.CURSE_OF_WITCHCRAFT, "Curse of Witchcraft", Desc.Skill.Enchanter.CURSE_OF_WITCHCRAFT, true, 20.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 8295208480454374043L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                target.addStatusEffect(new StatusEffect(Status.SILENCED, level*3));
//    ////
//    ////                useResult = new SkillUseResult("SILENCED");
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Enchanter.MIND_BLAST, "Mind Blast", Desc.Skill.Enchanter.MIND_BLAST, true, 20.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = -3587620067204007562L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                // TODO: impl
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, "Five Finger Death Punch", Desc.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, true, 35.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 9128637084476710269L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = 20 + level*30 - target.getTotalStat(Stat.ARM);
//    ////                int d = caster.dealPhysicalDamage(target, dmg);
//    ////
//    ////                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Ranger.BULLSEYE, "Bullseye", Desc.Skill.Ranger.BULLSEYE, true, 60.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 3498114139079315L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 5 + level * 10;
//    ////            }
//    ////
//    ////            @Override
//    ////            public void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float dmg = 100 + 0.2f*level * caster.getTotalAttribute(Attribute.DEXTERITY) - target.getTotalStat(GameCharacter.DEF);
//    ////                caster.dealPureDamage(target, dmg);
//    ////
//    ////                useResult = new SkillUseResult(dmg + ",PURE");
//    ////            }
//    ////        });
//    ////

//    ////
//    ////        addSkill(new Skill(ID.Skill.Ranger.ENCHANTED_ARROW, "Enchanted Arrows", Desc.Skill.Ranger.ENCHANTED_ARROW, true, 35.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 5416340917264724397L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                float duration = target.getTotalStat(Stat.ARM) * 0.1f;
//    ////                target.addStatusEffect(new StatusEffect(Status.STUNNED, duration));
//    ////
//    ////                useResult = new SkillUseResult("STUNNED");
//    ////            }
//    ////        });
//    ////
//    ////        addSkill(new Skill(ID.Skill.Ranger.FAST_REFLEXES, "Fast Reflexes", Desc.Skill.Ranger.FAST_REFLEXES, true, 35.0f) {
//    ////            /**
//    ////             *
//    ////             */
//    ////            private static final long serialVersionUID = 5766544471206156505L;
//    ////
//    ////            @Override
//    ////            public int getManaCost() {
//    ////                return 3 + level*4;
//    ////            }
//    ////
//    ////            @Override
//    ////            protected void useImpl(GameCharacter caster, GameCharacter target) {
//    ////                caster.addEffect(new Effect(10.0f, ID.Skill.Ranger.FAST_REFLEXES, new Rune[] {},
//    ////                        new Essence[] {
//    ////                        new Essence(Stat.ASPD, level*2)
//    ////                }));
//    ////
//    ////                useResult = new SkillUseResult("ASPD +" + level*2);
//    ////            }
//    ////
//    ////            @Override
//    ////            public boolean isSelfTarget() {
//    ////                return true;
//    ////            }
//    ////        });
//    ////
//    ////
//    ////        // ENEMIES
//    ////        addEnemy(new Enemy(ID.Enemy.MINOR_FIRE_SPIRIT, "Minor Fire Spirit", Desc.Enemy.MINOR_FIRE_SPIRIT,
//    ////                EnemyType.NORMAL, Element.FIRE, 1, new AttributeInfo(),
//    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.KNIFE, 50), new DroppableItem(ID.Armor.THANATOS_BODY_ARMOR, 10)));
//    ////
//    ////        addEnemy(new Enemy(ID.Enemy.MINOR_EARTH_SPIRIT, "Minor Earth Spirit", Desc.Enemy.MINOR_EARTH_SPIRIT,
//    ////                EnemyType.NORMAL, Element.EARTH, 1, new AttributeInfo(),
//    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.IRON_SWORD, 15), new DroppableItem(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL, 5)));
//    ////
//    ////        addEnemy(new Enemy(ID.Enemy.MINOR_WATER_SPIRIT, "Minor Water Spirit", Desc.Enemy.MINOR_WATER_SPIRIT,
//    ////                EnemyType.NORMAL, Element.WATER, 1, new AttributeInfo(),
//    ////                new Experience(100, 100, 100), 0, new DroppableItem(ID.Armor.CHAINMAL, 25), new DroppableItem(ID.Weapon.SOUL_REAPER, 5)));
//    //    }







//class AttackControl : AbstractControl() {
//
//    var enabled = true
//
//    private lateinit var char: CharacterEntity
//    private lateinit var animation: AnimatedTexture
//
//    val selected = SimpleObjectProperty<Entity>()
//
//    override fun onAdded(entity: Entity) {
//        char = entity as CharacterEntity
//        animation = char.data.animation
//    }
//
//    override fun onUpdate(entity: Entity, tpf: Double) {
//        if (selected.value is CharacterEntity)
//            startAttack(char, selected.value as GameEntity)
//    }
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