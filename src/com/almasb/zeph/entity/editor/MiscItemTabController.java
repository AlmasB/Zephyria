package com.almasb.zeph.entity.editor;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.almasb.zeph.entity.item.MiscItem;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class MiscItemTabController {

    //private EntityManager entityManager = EntityManager.INSTANCE;
    private Path file;

    @FXML
    private ListView<MiscItem> list;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private TextField textureName;
    @FXML
    private TextField sellPrice;

    public void initialize() {
        list.getSelectionModel().selectedItemProperty().addListener((obs, old, item) -> {
            if (item == null)
                return;

            id.setText(String.valueOf(item.getId()));
            name.setText(item.getName());
            description.setText(item.getDescription());
            textureName.setText(item.getTextureName());
            sellPrice.setText(String.valueOf(item.getSellPrice()));
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
//
//            list.getItems().add(weapon);
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
                //List<MiscItem> items = entityManager.loadMiscItems(is);
                //list.getItems().setAll(items);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        try {
            //entityManager.saveMiscItems(list.getItems(), file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
