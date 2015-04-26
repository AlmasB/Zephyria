package com.almasb.zeph.entity.item;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.almasb.fxgl.entity.AbstractControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.control.PlayerControl;
import com.almasb.zeph.entity.orion.Damage;
import com.almasb.zeph.entity.orion.Damage.DamageType;

public final class Weapon {

    public enum WeaponProperty implements PropertyKey {
        TYPE, ID, NAME, DESCRIPTION, REFINE_LEVEL, ELEMENT, DAMAGE, MODIFIERS
    }

    private int id;
    private String name;
    private String description;
    private String textureName;
    private int refineLevel;
    private Element element;
    private int damage;

    private List<ItemModifier> modifiers = new ArrayList<>();

    public Weapon() {

    }

//    @JsonCreator
//    /*package-private*/ Weapon(@JsonProperty("id") int id,
//            @JsonProperty("name") String name,
//            @JsonProperty("description") String description,
//            @JsonProperty("textureName") String textureName,
//            @JsonProperty("refineLevel") int refineLevel,
//            @JsonProperty("element") Element element,
//            @JsonProperty("damage") int damage) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.textureName = textureName;
//        this.refineLevel = refineLevel;
//        this.element = element;
//        this.damage = damage;
//    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public void setRefineLevel(int refineLevel) {
        this.refineLevel = refineLevel;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTextureName() {
        return textureName;
    }

    public int getRefineLevel() {
        return refineLevel;
    }

    public Element getElement() {
        return element;
    }

    public int getDamage() {
        return damage;
    }

    public Entity toEntity() {
        Entity weapon = new Entity(WeaponProperty.TYPE);
        weapon.setProperty(WeaponProperty.NAME, name)
                .setProperty(WeaponProperty.DESCRIPTION, description)
                .setProperty(WeaponProperty.ID, id);
//                .setProperty(WeaponProperty.DAMAGE, new SimpleIntegerProperty(damage))
//                .setProperty(WeaponProperty.ELEMENT, element)
//                .setProperty(WeaponProperty.REFINE_LEVEL, new SimpleIntegerProperty(refineLevel))
//                .setProperty(WeaponProperty.MODIFIERS, new ArrayList<>(modifiers));

        WeaponControl control = new WeaponControl();
        control.damage = (new SimpleIntegerProperty(damage));
        control.refineLevel = (new SimpleIntegerProperty(refineLevel));
        control.element = (element);
        control.modifiers = (new ArrayList<>(modifiers));
        weapon.addControl(control);

        //weapon.setGraphics(texture);
        return weapon;
    }

    public static Weapon fromEntity(Entity entity) {
        Weapon weapon = null;
        return weapon;
    }

    @Override
    public String toString() {
        return name;
    }

    public static class WeaponControl extends AbstractControl {

        private IntegerProperty damage;
        private IntegerProperty refineLevel;
        private Element element;

        private List<ItemModifier> modifiers;

        @Override
        protected void initEntity(Entity entity) {
//            damage = entity.getProperty(WeaponProperty.DAMAGE);
//            refineLevel = entity.getProperty(WeaponProperty.REFINE_LEVEL);
//            element = entity.getProperty(WeaponProperty.ELEMENT);
//            modifiers = entity.getProperty(WeaponProperty.MODIFIERS);
        }

        @Override
        public void onUpdate(Entity entity, long now) {
            // TODO Auto-generated method stub

        }

        public IntegerProperty damage() {
            return damage;
        }

        public IntegerProperty refineLevel() {
            return refineLevel;
        }

        public Element element() {
            return element;
        }

        public List<ItemModifier> modifiers() {
            return new ArrayList<>(modifiers);
        }

        public Damage getDamage() {
            Damage dmg = new Damage();
            dmg.setValue(damage.get() + refineLevel.get() * 10);
            dmg.setType(DamageType.PHYSICAL);
            dmg.setElement(element);
            dmg.setCritical(false);
            return dmg;
        }

        public void onEquip(PlayerControl player) {
            for (ItemModifier mod : modifiers) {

            }
        }

        public void onUnequip(PlayerControl player) {

        }
    }
}
