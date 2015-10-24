package com.almasb.zeph.entity.editor;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainController {

    public void exit() {
        Platform.exit();
    }

    public void about() {
        Alert info = new Alert(AlertType.INFORMATION);
        info.setTitle("About...");
        info.setContentText("Item Editor 1.0 for Zephyria by AlmasB");
        info.showAndWait();
    }
}
