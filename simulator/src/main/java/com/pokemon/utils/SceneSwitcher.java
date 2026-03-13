package com.pokemon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {
    public static void switchTo(Stage stage, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(SceneSwitcher.class.getResource(fxmlPath));
        stage.getScene().setRoot(root);
    }
}