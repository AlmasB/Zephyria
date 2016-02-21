package com.almasb.zeph.entity.character;

import com.almasb.zeph.entity.ID;

/**
 * A game character will have one of these classes.
 * Profession/job in a nutshell.
 *
 * @author Almas Baimagambetov
 */
public enum GameCharacterClass {
    MONSTER  (50, 50),
    NOVICE   (10, 10),

    WARRIOR  (40, 20, ID.Skill.Warrior.MIGHTY_SWING, ID.Skill.Warrior.ROAR, ID.Skill.Warrior.WARRIOR_HEART, ID.Skill.Warrior.ARMOR_MASTERY),
    CRUSADER (60, 35, ID.Skill.Crusader.DIVINE_ARMOR, ID.Skill.Crusader.FAITH, ID.Skill.Crusader.HOLY_LIGHT, ID.Skill.Crusader.LAST_STAND, ID.Skill.Crusader.PRECISION_STRIKE),
    PALADIN  (90, 50),
    GLADIATOR(65, 30, ID.Skill.Gladiator.BASH, ID.Skill.Gladiator.BLOODLUST, ID.Skill.Gladiator.DOUBLE_EDGE, ID.Skill.Gladiator.ENDURANCE, ID.Skill.Gladiator.SHATTER_ARMOR),
    KNIGHT   (100, 35),

    SCOUT    (30, 35, ID.Skill.Scout.EXPERIENCED_FIGHTER, ID.Skill.Scout.POISON_ATTACK, ID.Skill.Scout.TRICK_ATTACK, ID.Skill.Scout.WEAPON_MASTERY),
    ROGUE    (50, 40, ID.Skill.Rogue.CRITICAL_STRIKE, ID.Skill.Rogue.DOUBLE_STRIKE, ID.Skill.Rogue.TRIPLE_STRIKE, ID.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, ID.Skill.Rogue.SHAMELESS),
    ASSASSIN (75, 45),
    RANGER   (40, 40, ID.Skill.Ranger.BULLSEYE, ID.Skill.Ranger.EAGLE_EYE, ID.Skill.Ranger.ENCHANTED_ARROW, ID.Skill.Ranger.FAST_REFLEXES, ID.Skill.Ranger.PINPOINT_WEAKNESS),
    HUNTER   (50, 55),

    MAGE     (25, 45, ID.Skill.Mage.AIR_SPEAR, ID.Skill.Mage.EARTH_BOULDER, ID.Skill.Mage.FIREBALL, ID.Skill.Mage.ICE_SHARD),
    WIZARD   (35, 60, ID.Skill.Wizard.AMPLIFY_MAGIC, ID.Skill.Wizard.ICICLE_AVALANCHE, ID.Skill.Wizard.MAGIC_MASTERY, ID.Skill.Wizard.MENTAL_STRIKE, ID.Skill.Wizard.THUNDERBOLT_FIRESTORM),
    ARCHMAGE (45, 75),
    ENCHANTER(30, 65, ID.Skill.Enchanter.ASTRAL_PROTECTION, ID.Skill.Enchanter.CURSE_OF_WITCHCRAFT, ID.Skill.Enchanter.MAGIC_SHIELD, ID.Skill.Enchanter.MANA_BURN, ID.Skill.Enchanter.MIND_BLAST),
    SAGE     (40, 90);

    public final int hp;
    public final int sp;
    public final int[] skillIDs;

    GameCharacterClass(int hp, int sp, int... IDs) {
        this.hp = hp;
        this.sp = sp;
        this.skillIDs = IDs;
    }
}
