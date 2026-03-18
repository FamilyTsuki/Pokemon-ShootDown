package com.pokemon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneSwitcher {
    public static void switchTo(Stage stage, String path) throws IOException {
        URL res = SceneSwitcher.class.getResource(path);
        if (res == null) throw new IOException("FXML not found: " + path);
        
        Parent root = FXMLLoader.load(res);
        stage.getScene().setRoot(root);
    }
}