package com.almasb.zeph.entity.editor;

import com.almasb.zeph.combat.Element;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class WeaponsTabController {

    //private EntityManager entityManager = EntityManager.INSTANCE;
    private Path file;

    @FXML
    private ListView<String> list;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private TextField textureName;
    @FXML
    private TextField damage;
    @FXML
    private ChoiceBox<Element> element;
    @FXML
    private TextField refineLevel;

    public void initialize() {
        element.getItems().setAll(Element.values());
        element.getSelectionModel().selectFirst();

        list.getSelectionModel().selectedItemProperty().addListener((obs, old, weapon) -> {
            if (weapon == null)
                return;

//            id.setText(String.valueOf(weapon.getId()));
//            name.setText(weapon.getName());
//            description.setText(weapon.getDescription());
//            textureName.setText(weapon.getTextureName());
//            damage.setText(String.valueOf(weapon.getDamage()));
//            element.getSelectionModel().select(weapon.getElement());
//            refineLevel.setText(String.valueOf(weapon.getRefineLevel()));
        });
    }

    public void createItem() {
        try {
//            Weapon weapon = new Weapon();
//            weapon.setName(name.getText());
//            weapon.setDamage(Integer.parseInt(damage.getText()));
//            weapon.setDescription(description.getText());
//            weapon.setElement(element.getValue());
//            weapon.setId(Integer.parseInt(id.getText()));
//            weapon.setRefineLevel(Integer.parseInt(refineLevel.getText()));
//            weapon.setTextureName(textureName.getText());

            //list.getItems().add(weapon);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editItem() {
        try {
//            Weapon weapon = list.getSelectionModel().getSelectedItem();
//            weapon.setName(name.getText());
//            weapon.setDamage(Integer.parseInt(damage.getText()));
//            weapon.setDescription(description.getText());
//            weapon.setElement(element.getValue());
//            weapon.setId(Integer.parseInt(id.getText()));
//            weapon.setRefineLevel(Integer.parseInt(refineLevel.getText()));
//            weapon.setTextureName(textureName.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFile() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("./"));
        chooser.setTitle("Open");

        File chosenFile = chooser.showOpenDialog(null);
        if (chosenFile != null) {
            file = chosenFile.toPath();
            try (InputStream is = Files.newInputStream(chosenFile.toPath())) {
                //List<Weapon> weapons = entityManager.loadWeapons(is);
                //list.getItems().setAll(weapons);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        try {
           // entityManager.saveWeapons(list.getItems(), file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
