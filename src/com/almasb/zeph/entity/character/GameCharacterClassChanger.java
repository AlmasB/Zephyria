package com.almasb.zeph.entity.character;

import java.util.HashMap;

public class GameCharacterClassChanger {

    private static HashMap<GameCharacterClass, Ascension> reqList =
            new HashMap<GameCharacterClass, Ascension>();

    static {
        reqList.put(GameCharacterClass.NOVICE, new Ascension(2, 2, GameCharacterClass.WARRIOR, GameCharacterClass.SCOUT, GameCharacterClass.MAGE));

        reqList.put(GameCharacterClass.WARRIOR, new Ascension(3, 3, GameCharacterClass.CRUSADER, GameCharacterClass.GLADIATOR));
        reqList.put(GameCharacterClass.SCOUT, new Ascension(3, 3, GameCharacterClass.ROGUE, GameCharacterClass.RANGER));
        reqList.put(GameCharacterClass.MAGE, new Ascension(3, 3, GameCharacterClass.WIZARD, GameCharacterClass.ENCHANTER));

        reqList.put(GameCharacterClass.CRUSADER, new Ascension(5, 5, GameCharacterClass.PALADIN));
        reqList.put(GameCharacterClass.GLADIATOR, new Ascension(5, 5, GameCharacterClass.KNIGHT));

        reqList.put(GameCharacterClass.ROGUE, new Ascension(5, 5, GameCharacterClass.ASSASSIN));
        reqList.put(GameCharacterClass.RANGER, new Ascension(5, 5, GameCharacterClass.HUNTER));

        reqList.put(GameCharacterClass.WIZARD, new Ascension(5, 5, GameCharacterClass.ARCHMAGE));
        reqList.put(GameCharacterClass.ENCHANTER, new Ascension(5, 5, GameCharacterClass.SAGE));
    }

    /**
     * No instances
     */
    private GameCharacterClassChanger() {}

    public static boolean canChangeClass(Player ch) {
        Ascension r = reqList.get(ch.charClass);
        return r != null && ch.baseLevel >= r.baseLevel && ch.getJobLevel() >= r.jobLevel;
    }

    public static GameCharacterClass[] getAscensionClasses(Player ch) {
        return reqList.get(ch.charClass).classesTo;
    }

    private static class Ascension {

        public final GameCharacterClass[] classesTo;
        public final int baseLevel, jobLevel;

        public Ascension(int baseLevel, int jobLevel, GameCharacterClass... classes) {
            this.baseLevel = baseLevel;
            this.jobLevel = jobLevel;
            this.classesTo = classes;
        }
    }
}
