package com.almasb.zeph;

import java.nio.file.Paths;
import java.util.Arrays;

import com.almasb.zeph.entity.Element;
import com.almasb.zeph.entity.EntityManager;
import com.almasb.zeph.entity.item.Weapon;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Weapon w = new Weapon();
        w.setDamage(100);
        w.setDescription("A weapon lol");
        w.setElement(Element.NEUTRAL);
        w.setId(100);
        w.setName("Sword");
        w.setRefineLevel(1);
        w.setTextureName("file.png");

        Weapon w2 = new Weapon();
        w2.setDamage(130);
        w2.setDescription("Ad weapon lol");
        w2.setElement(Element.NEUTRAL);
        w2.setId(102);
        w2.setName("Sword2");
        w2.setRefineLevel(1);
        w2.setTextureName("file.png");

        EntityManager manager = EntityManager.INSTANCE;
        manager.saveWeapons(Arrays.asList(w, w2), Paths.get("./test.json"));
    }

}
