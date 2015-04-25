package com.almasb.zeph.entity.item;

import javafx.beans.property.SimpleIntegerProperty;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.PropertyKey;
import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.control.WeaponControl;

public final class Weapon {

    public enum WeaponProperty implements PropertyKey {
        TYPE, ID, NAME, DESCRIPTION, REFINE_LEVEL, ELEMENT, DAMAGE
    }

    private int id;
    private String name;
    private String description;
    private String textureName;
    private int refineLevel;
    private Element element;
    private int damage;

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
                .setProperty(WeaponProperty.ID, id)
                .setProperty(WeaponProperty.DAMAGE, new SimpleIntegerProperty(damage))
                .setProperty(WeaponProperty.ELEMENT, element)
                .setProperty(WeaponProperty.REFINE_LEVEL, new SimpleIntegerProperty(refineLevel));

        weapon.addControl(new WeaponControl());

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
}
