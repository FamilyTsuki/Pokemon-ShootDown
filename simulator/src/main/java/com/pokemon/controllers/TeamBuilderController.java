package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.pokemonList.*; // Importe tes classes de Pokémon
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.io.IOException;

public class TeamBuilderController {
    
    @FXML private ComboBox<String> pkm1, pkm2, pkm3, pkm4, pkm5, pkm6;
    @FXML private ComboBox<String> item1, item2, item3, item4, item5, item6;

    @FXML
    public void initialize() {

        String[] pokemonOptions = {"Blastoise", "Charizard", "Venusaur"};
        String[] itemOptions = {"Leftovers", "Life Orb", "Choice Band"};

        ComboBox<String>[] pkmSelectors = new ComboBox[]{pkm1, pkm2, pkm3, pkm4, pkm5, pkm6};
        ComboBox<String>[] itemSelectors = new ComboBox[]{item1, item2, item3, item4, item5, item6};

        for (int i = 0; i < 6; i++) {
            if (pkmSelectors[i] != null) pkmSelectors[i].getItems().addAll(pokemonOptions);
            if (itemSelectors[i] != null) itemSelectors[i].getItems().addAll(itemOptions);
        }
    }

    @FXML
    private void handleStartBattle(ActionEvent event) throws IOException {
        
        String selected = pkm1.getValue();
        if (selected == null) selected = "Blastoise";

        // Création du Pokémon choisi
        Pokemon playerPkm = createPokemonInstance(selected);
        Pokemon cpuPkm = new Charizard();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/battle_view.fxml"));
        Parent root = loader.load();

        BattleController battleCtrl = loader.getController();
        battleCtrl.setupBattle(playerPkm, cpuPkm);

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/start_view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private Pokemon createPokemonInstance(String name) {
        return switch (name) {
            case "Charizard" -> new Charizard();
            case "Venusaur" -> new Venusaur();
            default -> new Blastoise();
        };
    }
}