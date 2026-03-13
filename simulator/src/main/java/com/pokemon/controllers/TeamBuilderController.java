package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class TeamBuilderController {

    @FXML private Button slot1Btn, slot2Btn, slot3Btn, slot4Btn, slot5Btn, slot6Btn;
    
    private Pokemon[] team = new Pokemon[6];

    @FXML
    private void handleSelectPokemon(ActionEvent event) {
        Button clickedBtn = (Button) event.getSource();
        

        String id = clickedBtn.getId();
        if (id == null) return; 
        int slotIndex = Integer.parseInt(id.replaceAll("[^0-9]", "")) - 1;

        try {
            FXMLLoader selectorLoader = new FXMLLoader(getClass().getResource("/com/pokemon/views/pokemon_selector.fxml"));
            Parent selectorRoot = selectorLoader.load();
            PokemonSelectorController selectorCtrl = selectorLoader.getController();
            
            Stage selectorStage = new Stage();
            selectorStage.initModality(Modality.APPLICATION_MODAL);
            selectorStage.setScene(new Scene(selectorRoot));
            selectorStage.showAndWait(); 
            
            Pokemon chosenFromList = selectorCtrl.getSelectedPokemon();
            System.out.println(selectorCtrl.getSelectedPokemon());
            if (chosenFromList != null) {

                FXMLLoader editorLoader = new FXMLLoader(getClass().getResource("/com/pokemon/views/pokemon_editor.fxml"));
                Parent editorRoot = editorLoader.load();
                PokemonEditorController editorCtrl = editorLoader.getController();
                
                editorCtrl.setPokemon(chosenFromList); 

                Stage editorStage = new Stage();
                editorStage.initModality(Modality.APPLICATION_MODAL);
                editorStage.setScene(new Scene(editorRoot));
                editorStage.showAndWait(); 

                if (editorCtrl.isSaveClicked()) {
                    Pokemon finalPokemon = editorCtrl.getSelectedPokemon();
                    this.team[slotIndex] = finalPokemon;
                    

                    clickedBtn.setText(finalPokemon.getName().toUpperCase());
                    clickedBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: #f1c40f; -fx-border-width: 2;");
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStartBattle(ActionEvent event) throws IOException {
        if (team[0] == null) {
            System.out.println("Erreur: Choisissez au moins un Pokémon !");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/battle_view.fxml"));
        Parent root = loader.load();
        BattleController battleCtrl = loader.getController();
        
        battleCtrl.setupBattle(team[0], new com.pokemon.pokemonList.Charizard());

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
}