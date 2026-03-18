package com.pokemon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String path = "/com/pokemon/views/start_view.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(path));

        stage.setTitle("Pokémon ShootDown");
        stage.setScene(new Scene(root));
        
        stage.setFullScreen(true);
        
        stage.setFullScreenExitHint(""); 
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}