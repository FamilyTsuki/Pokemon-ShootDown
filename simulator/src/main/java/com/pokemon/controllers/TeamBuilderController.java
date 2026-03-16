package com.pokemon.controllers;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Team;
import com.pokemon.pokemonList.Blastoise;
import com.pokemon.pokemonList.Bulbasaur;
import com.pokemon.pokemonList.Charizard;
import com.pokemon.pokemonList.Charmander;
import com.pokemon.pokemonList.Charmeleon;
import com.pokemon.pokemonList.Ivysaur;
import com.pokemon.pokemonList.Squirtle;
import com.pokemon.pokemonList.Venusaur;
import com.pokemon.pokemonList.Wartortle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private Pokemon[] generateCpuTeam(int teamSize) {
    Pokemon[] cpuTeam = new Pokemon[6];
    Random random = new Random();

    Pokemon[] availableSpecies = {
        new Bulbasaur(), new Ivysaur(), new Venusaur(),
        new Charmander(), new Charmeleon(), new Charizard(),
        new Squirtle(), new Wartortle(), new Blastoise()
    };

    for (int i = 0; i < teamSize; i++) {
        Pokemon randomPkmn = availableSpecies[random.nextInt(availableSpecies.length)];
        
        Pokemon newCpuPkmn;
        try {
            newCpuPkmn = randomPkmn.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            newCpuPkmn = randomPkmn;
        }

        Attack[] learnableMoves = newCpuPkmn.getLearble();
        if (learnableMoves != null && learnableMoves.length > 0) {
            
            List<Attack> movePool = new ArrayList<>();
            for(Attack a : learnableMoves) if(a != null) movePool.add(a);
            
            Collections.shuffle(movePool); 
            
            int numMoves = Math.min(4, movePool.size());
            Attack[] finalMoves = new Attack[numMoves];
            for (int j = 0; j < numMoves; j++) {
                finalMoves[j] = movePool.get(j);
            }
            newCpuPkmn.setAttacks(finalMoves);
        }

        cpuTeam[i] = newCpuPkmn;
    }
    return cpuTeam;
    }
    @FXML
private void handleStartBattle(ActionEvent event) throws IOException {

    int playerCount = 0;
    for (Pokemon p : team) {
        if (p != null) playerCount++;
    }

    if (playerCount == 0) {
        System.out.println("Erreur : Équipe vide !");
        return;
    }

    Team playerTeamObj = new Team(this.team);

    Pokemon[] cpuArray = generateCpuTeam(playerCount); 
    Team cpuTeamObj = new Team(cpuArray);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/battle_view.fxml"));
    Parent root = loader.load();
    
    BattleController battleCtrl = loader.getController();
    
    battleCtrl.setupBattle(playerTeamObj, cpuTeamObj);

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