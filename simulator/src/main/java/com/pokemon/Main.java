package com.pokemon;

import com.pokemon.controllers.BattleController;
import com.pokemon.models.Pokemon;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // On charge l'écran de démarrage au lieu du combat
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/start_view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Pokémon ShootDown - Accueil");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}