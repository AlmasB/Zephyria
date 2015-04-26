package com.almasb.zeph;

/**
 * Handles everything to do with math and calculations
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 * @version 1.0
 *
 */
public class GameMath {

    /**
     * Generates a random value between 1 (inclusive) and passed parameter (inclusive)
     * @param max - max value
     * @return any random number [1..max]
     */
    public static int random(int max) {
        return (int) (Math.random()*max) + 1;
    }

    /**
     * "Rolls" a dice and checks it against the chance percentage
     * @param chance - the chance against which the roll is checked
     *              100 will always return true, 1 - will return true approx. once in 100 rolls
     * @return true if chance succeeds, false otherwise
     */
    public static boolean checkChance(int chance) {
        return (int) (Math.random()*100) + 1 <= chance;
    }

    public static boolean checkChance(float chance) {
        return Math.random() * 100 + 1 <= chance;
    }
}
