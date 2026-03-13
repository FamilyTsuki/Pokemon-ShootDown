package com.pokemon;

import com.pokemon.controllers.BattleController;
import com.pokemon.models.Pokemon;
import com.pokemon.pokemonList.Blastoise;
import com.pokemon.pokemonList.Charizard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {


public void start(Stage stage) throws Exception {
    
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/battle_view.fxml"));
    Parent root = loader.load();

    BattleController controller = loader.getController();

    Pokemon playerPkm = new Blastoise();
    Pokemon cpuPkm = new Blastoise();

    controller.setupBattle(playerPkm, cpuPkm);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
}

    public static void main(String[] args) {
        launch();
    }
}