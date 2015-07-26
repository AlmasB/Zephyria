package com.almasb.zeph.entity.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.combat.Attribute;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.Experience;
import com.almasb.zeph.combat.GameMath;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.combat.Attribute.AttributeInfo;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.item.DroppableItem;

public class Enemy extends GameCharacter {

    private static final long serialVersionUID = -4175008430166158773L;

    public enum EnemyType {
        NORMAL, MINIBOSS, BOSS
    }

    public final EnemyType type;

    private Element element;

    private List<DroppableItem> drops = new ArrayList<>();

    /**
     * Runtime ID of players who attacked this monster
     */
    private transient ArrayList<Integer> attackers = new ArrayList<Integer>();

    public Enemy(int id, String name, String description, String textureName,
            EnemyType type, Element element, int level, AttributeInfo attrs, Experience xp, DroppableItem... drops) {
        super(id, name, description, textureName, GameCharacterClass.MONSTER);
      this.type = type;
      this.element = element;
      this.setBaseLevel(level);
      this.xp = xp;
      this.drops.addAll(Arrays.asList(drops));

      setAttribute(Attribute.STRENGTH, attrs.str);
      setAttribute(Attribute.VITALITY, attrs.vit);
      setAttribute(Attribute.DEXTERITY, attrs.dex);
      setAttribute(Attribute.AGILITY, attrs.agi);
      setAttribute(Attribute.INTELLECT, attrs.int_);
      setAttribute(Attribute.WISDOM, attrs.wis);
      setAttribute(Attribute.WILLPOWER, attrs.wil);
      setAttribute(Attribute.PERCEPTION, attrs.per);
      setAttribute(Attribute.LUCK, attrs.luc);

      updateStats();
      setHP((int)getTotalStat(Stat.MAX_HP));   // set current hp/sp to max
      setSP((int)getTotalStat(Stat.MAX_SP));
    }


    public Enemy(Enemy copy) {
        this(copy.getID(), copy.getName(), copy.getDescription(), copy.getTextureName(), copy.type, copy.element, copy.getBaseLevel(),
                new AttributeInfo().str(copy.getBaseAttribute(Attribute.STRENGTH))
                .vit(copy.getBaseAttribute(Attribute.VITALITY))
                .dex(copy.getBaseAttribute(Attribute.DEXTERITY))
                .agi(copy.getBaseAttribute(Attribute.AGILITY))
                .int_(copy.getBaseAttribute(Attribute.INTELLECT))
                .wis(copy.getBaseAttribute(Attribute.WISDOM))
                .wil(copy.getBaseAttribute(Attribute.WILLPOWER))
                .per(copy.getBaseAttribute(Attribute.PERCEPTION))
                .luc(copy.getBaseAttribute(Attribute.LUCK)), copy.xp, copy.drops.toArray(new DroppableItem[0]));
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
                    attacker.getInventory().addItem(EntityManager.getItemByID(item.itemID));
                }
            }

            attacker.incMoney(GameMath.random(getBaseLevel() * 100));
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
        Entity e = Entity.noType();
        e.setProperty("enemy_data", new Enemy(this));
        return e;
    }

    public static class EnemyBuilder {
        private int id;
        private String name;
        private String desc;
        private String texture;
        private EnemyType type = EnemyType.NORMAL;
        private Element element = Element.NEUTRAL;
        private int level = 1;
        private AttributeInfo attributes = new AttributeInfo();
        private Experience xp;
        private DroppableItem[] items = new DroppableItem[0];

        public EnemyBuilder id(int id) {
            this.id = id;
            return this;
        }

        public EnemyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EnemyBuilder description(String description) {
            this.desc = description;
            return this;
        }

        public EnemyBuilder textureName(String textureName) {
            this.texture = textureName;
            return this;
        }

        public EnemyBuilder level(int level) {
            this.level = level;
            return this;
        }

        public EnemyBuilder element(Element element) {
            this.element = element;
            return this;
        }

        public EnemyBuilder type(EnemyType type) {
            this.type = type;
            return this;
        }

        public EnemyBuilder attributes(AttributeInfo attrs) {
            this.attributes = attrs;
            return this;
        }

        public EnemyBuilder xp(Experience xp) {
            this.xp = xp;
            return this;
        }

        public EnemyBuilder drops(DroppableItem... items) {
            this.items = items;
            return this;
        }

        public Enemy build() {
            return new Enemy(id, name, desc, texture, type, element, level, attributes, xp, items);
        }
    }
}
