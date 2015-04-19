package com.almasb.zeph.entity;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum EntityManager {
    INSTANCE;

    private ObjectMapper parser = new ObjectMapper();

    public List<Weapon> loadWeapons(InputStream is) throws Exception {
        List<Weapon> weapons = parser.readValue(is,
                new TypeReference<List<Weapon> >() {});
        return weapons;
    }

    public void saveWeapons(List<Weapon> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }

    public List<Armor> loadArmor(InputStream is) throws Exception {
        List<Armor> weapons = parser.readValue(is,
                new TypeReference<List<Armor> >() {});
        return weapons;
    }

    public void saveArmor(List<Armor> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }

    public List<IngredientItem> loadIngredientItems(InputStream is) throws Exception {
        List<IngredientItem> weapons = parser.readValue(is,
                new TypeReference<List<IngredientItem> >() {});
        return weapons;
    }

    public void saveIngredientItems(List<IngredientItem> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }

    public List<MiscItem> loadMiscItems(InputStream is) throws Exception {
        List<MiscItem> weapons = parser.readValue(is,
                new TypeReference<List<MiscItem> >() {});
        return weapons;
    }

    public void saveMiscItems(List<MiscItem> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }

    public List<QuestItem> loadQuestItems(InputStream is) throws Exception {
        List<QuestItem> weapons = parser.readValue(is,
                new TypeReference<List<QuestItem> >() {});
        return weapons;
    }

    public void saveQuestItems(List<QuestItem> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }

    public List<UsableItem> loadUsableItems(InputStream is) throws Exception {
        List<UsableItem> weapons = parser.readValue(is,
                new TypeReference<List<UsableItem> >() {});
        return weapons;
    }

    public void saveUsableItems(List<UsableItem> data, Path file) throws Exception {
        try (OutputStream os = Files.newOutputStream(file)) {
            parser.writeValue(os, data);
        }
    }
}
