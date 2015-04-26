package uk.ac.brighton.uni.ab607.mmorpg.common.object;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.orion.EquippableItem;
import com.almasb.zeph.entity.orion.Player;
import com.almasb.zeph.entity.orion.Rune;
import com.almasb.zeph.entity.orion.GameCharacter.Stat;

public class Armor extends EquippableItem {

    private static final long serialVersionUID = 826666654020818850L;

    public enum ArmorType {
        HELM, BODY, SHOES
    }

    public final ArmorType type;

    private int armor, marmor;

    public Armor(int id, String name, String description, String textureName,
            ItemLevel level, Element element, ArmorType type, int armor, int marmor, Rune[] defRunes) {
        super(id, name, description, textureName, level, element, defRunes);
        this.type = type;
        this.armor = armor;
        this.marmor = marmor;
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
        Entity e = new Entity("armor");
        e.setProperty("armor_data", this);
        return e;
    }

    public static class ArmorBuilder {

        public Armor build() {
            return null;
        }
    }
}
