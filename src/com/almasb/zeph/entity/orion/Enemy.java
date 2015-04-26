package com.almasb.zeph.entity.orion;

import java.util.ArrayList;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.orion.Attribute.AttributeInfo;

public class Enemy extends GameCharacter {

    private static final long serialVersionUID = -4175008430166158773L;

    public enum EnemyType {
        NORMAL, MINIBOSS, BOSS
    }

    public transient final EnemyType type;

    private transient Element element;

    private transient DroppableItem[] drops;

    /**
     * Runtime ID of players who attacked this monster
     */
    private transient ArrayList<Integer> attackers = new ArrayList<Integer>();

    public Enemy(int id, String name, String description, String textureName,
            EnemyType type, Element element, int level, AttributeInfo attrs, Experience xp, DroppableItem... drops) {
        super(id, name, description, textureName, GameCharacterClass.MONSTER);
      this.type = type;
      this.element = element;
      this.baseLevel = level;
      this.xp = xp;
      this.drops = drops;
      attributes[STR] = (byte) attrs.str;
      attributes[VIT] = (byte) attrs.vit;
      attributes[DEX] = (byte) attrs.dex;
      attributes[AGI] = (byte) attrs.agi;
      attributes[INT] = (byte) attrs.int_;
      attributes[WIS] = (byte) attrs.wis;
      attributes[WIL] = (byte) attrs.wil;
      attributes[PER] = (byte) attrs.per;
      attributes[LUC] = (byte) attrs.luc;
      calculateStats();
      setHP((int)getTotalStat(MAX_HP));   // set current hp/sp to max
      setSP((int)getTotalStat(MAX_SP));
    }


    public Enemy(Enemy copy) {
        this(copy.id, copy.name, copy.description, copy.textureName, copy.type, copy.element, copy.baseLevel,
                new AttributeInfo().str(copy.getBaseAttribute(STR))
                .vit(copy.getBaseAttribute(VIT))
                .dex(copy.getBaseAttribute(DEX))
                .agi(copy.getBaseAttribute(AGI))
                .int_(copy.getBaseAttribute(INT))
                .wis(copy.getBaseAttribute(WIS))
                .wil(copy.getBaseAttribute(WIL))
                .per(copy.getBaseAttribute(PER))
                .luc(copy.getBaseAttribute(LUC)), copy.xp, copy.drops);
    }

    public void addAttackerRuntimeID(int runtimeID) {
        if (!attackers.contains(runtimeID)) {
            attackers.add(runtimeID);
        }
    }

    /**
     *
     * @return
     *          runtime IDs of attackers
     */
    public ArrayList<Integer> getAttackers() {
        return attackers;
    }

    /**
     *
     * @param p
     *           The player who landed the killing blow
     * @param players
     *          players who attacked the monster, including the killing person
     * @return
     */
    public void onDeath(Player p, ArrayList<Player> players) {
        //alive = false;

        for (Player attacker : players) {
            // if the killer
            if (p == attacker) {

            }

            for (DroppableItem item : drops) {
                if (GameMath.checkChance(item.dropChance)) {
                    attacker.getInventory().addItem(ObjectManager.getItemByID(item.itemID));
                }
            }

            attacker.incMoney(GameMath.random(this.baseLevel * 100));
            attacker.gainXP(getXP());
        }
    }

    /**
     *
     * @return
     *          Experience object containing base/stat/job xp
     *          for this enemy
     */
    public Experience getXP() {
        return xp;
    }

    @Override
    public Element getWeaponElement() {
        return element;
    }

    @Override
    public Element getArmorElement() {
        return element;
    }

    @Override
    public Entity toEntity() {
        Entity e = new Entity("enemy");
        e.setProperty("enemy_data", new Enemy(this));
        return e;
    }

    public static class EnemyBuilder {
        public Enemy build() {
            return null;
        }
    }
}
