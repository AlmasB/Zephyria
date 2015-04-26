package com.almasb.zeph.entity.orion;


/**
 * Skill that can be learnt/used by game characters
 * There is almost no limit to what skills can do
 * Damage, restore, buff, etc
 *
 * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
 * @version 1.0
 *
 */
public abstract class Skill extends GameEntity implements java.io.Serializable {

    private static final long serialVersionUID = 442371944346845569L;

    /**
     * Active skills need to be cast, have mana cost and cooldown
     * Whereas not active skills (passive) are always ON
     */
    public final boolean active;

    /**
     * Skill cooldowns in seconds
     */
    protected float skillCooldown, currentCooldown = 0.0f;

    public static final int MAX_LEVEL = 10;

    protected int level = 0;

    public Skill(Integer id, String name, String description, String textureName, Boolean active, Float cooldown) {
        super(id, name, description, textureName);
        this.active = active;
        this.skillCooldown = cooldown;
    }

    public void use(GameCharacter caster, GameCharacter target) {
        useImpl(caster, target);
        putOnCooldown();
    }

    public abstract int getManaCost();

    /**
     * Do not use this method directly (it is now hidden anyway)
     * It is needed to provide overridability to new skills
     *
     * use public method - {@link use()}
     *
     * @param caster
     * @param target
     */
    protected abstract void useImpl(GameCharacter caster, GameCharacter target);

    public boolean levelUp() {
        if (level < MAX_LEVEL) {
            level++;
            return true;
        }
        return false;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public float getCurrentCooldown() {
        return currentCooldown;
    }

    public void reduceCurrentCooldown(float value) {
        currentCooldown = Math.max(currentCooldown - value, 0);
    }

    public void putOnCooldown() {
        currentCooldown = skillCooldown;
    }

    public boolean isSelfTarget() {
        return false;
    }

    // add some methods for overriding
    // isPassive()
    // isSelfTarget()
}
