package com.almasb.zeph.entity.item;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.Rune;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.Player;

public class Armor extends EquippableItem {

    private static final long serialVersionUID = 826666654020818850L;

    public enum ArmorType {
        HELM, BODY, SHOES
    }

    public final ArmorType type;

    private int armor, marmor;

    public Armor(int id, String name, String description, String textureName,
            ItemLevel level, Element element, ArmorType type, int armor, int marmor, Rune... defRunes) {
        super(id, name, description, textureName, level, element, defRunes);
        this.type = type;
        this.armor = armor;
        this.marmor = marmor;
    }

    public Armor(Armor copy) {
        this(copy.getID(), copy.getName(), copy.getDescription(), copy.getTextureName(),
                copy.level, copy.element, copy.type, copy.armor, copy.marmor, copy.defaultRunes.toArray(new Rune[0]));
    }

    @Override
    public void onEquip(Player ch) {
        super.onEquip(ch);
        ch.addBonusStat(Stat.ARM, getArmorRating());
        ch.addBonusStat(Stat.MARM, getMArmorRating());
    }

    @Override
    public void onUnEquip(Player ch) {
        super.onUnEquip(ch);
        ch.addBonusStat(Stat.ARM, -getArmorRating());
        ch.addBonusStat(Stat.MARM, -getMArmorRating());
    }

    public int getArmorRating() {
        return armor + refineLevel * (refineLevel > 2 ? level.bonus + 1 : level.bonus);
    }

    public int getMArmorRating() {
        return marmor + refineLevel * (refineLevel > 2 ? level.bonus + 1 : level.bonus);
    }

    @Override
    public Entity toEntity() {
        Entity e = Entity.noType();
        e.setProperty("armor_data", this);
        return e;
    }

    public static class ArmorBuilder {
        private int id;
        private String name;
        private String desc;
        private String texture;
        private ItemLevel level = ItemLevel.NORMAL;
        private Element element = Element.NEUTRAL;
        private ArmorType type;
        private int armor;
        private int marmor;
        private Rune[] runes = new Rune[0];

        public ArmorBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ArmorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArmorBuilder description(String description) {
            this.desc = description;
            return this;
        }

        public ArmorBuilder textureName(String textureName) {
            this.texture = textureName;
            return this;
        }

        public ArmorBuilder itemLevel(ItemLevel level) {
            this.level = level;
            return this;
        }

        public ArmorBuilder element(Element element) {
            this.element = element;
            return this;
        }

        public ArmorBuilder type(ArmorType type) {
            this.type = type;
            return this;
        }

        public ArmorBuilder armor(int armor) {
            this.armor = armor;
            return this;
        }

        public ArmorBuilder marmor(int marmor) {
            this.marmor = marmor;
            return this;
        }

        public ArmorBuilder runes(Rune... runes) {
            this.runes = runes;
            return this;
        }

        public Armor build() {
            return new Armor(id, name, desc, texture, level, element, type, armor, marmor, runes);
        }
    }
}
