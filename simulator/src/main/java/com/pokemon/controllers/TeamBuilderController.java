package com.pokemon.controllers;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Team;


import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.pokemon.core.AudioManager;

public class TeamBuilderController {

    @FXML private Button slot1Btn, slot2Btn, slot3Btn, slot4Btn, slot5Btn, slot6Btn;
    @FXML private VBox rootContainer;
    @FXML private Button remove1Btn, remove2Btn, remove3Btn, remove4Btn, remove5Btn, remove6Btn;
    @FXML private Button startBtn;

    private Pokemon[] team = new Pokemon[6];

    @FXML
    public void initialize() {
        if (rootContainer != null) {
            rootContainer.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(800), rootContainer);
            ft.setToValue(1.0);
            ft.play();
            updateStartButton();
        }
    }

    @FXML
    private void handleSelectPokemon(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        Button clickedBtn = (Button) event.getSource();
        String id = clickedBtn.getId();
        if (id == null) return;
        int slotIndex = Integer.parseInt(id.replaceAll("[^0-9]", "")) - 1;

        animateClick(clickedBtn);

        try {
            FXMLLoader selectorLoader = new FXMLLoader(getClass().getResource("/com/pokemon/views/pokemon_selector.fxml"));
            Parent selectorRoot = selectorLoader.load();
            PokemonSelectorController selectorCtrl = selectorLoader.getController();

            Stage selectorStage = new Stage();
            selectorStage.initModality(Modality.APPLICATION_MODAL);
            selectorStage.setTitle("Choisir un Pokémon");
            selectorStage.setScene(new Scene(selectorRoot));
            selectorStage.showAndWait();

            Pokemon chosenFromList = selectorCtrl.getSelectedPokemon();
            if (chosenFromList != null) {
                
                FXMLLoader editorLoader = new FXMLLoader(getClass().getResource("/com/pokemon/views/pokemon_editor.fxml"));
                Parent editorRoot = editorLoader.load();
                PokemonEditorController editorCtrl = editorLoader.getController();
                editorCtrl.setPokemon(chosenFromList);

                Stage editorStage = new Stage();
                editorStage.initModality(Modality.APPLICATION_MODAL);
                editorStage.setTitle("Configuration de " + chosenFromList.getName());
                editorStage.setScene(new Scene(editorRoot));
                editorStage.showAndWait();

                if (editorCtrl.isSaveClicked()) {
                    Pokemon finalPokemon = editorCtrl.getSelectedPokemon();
                    this.team[slotIndex] = finalPokemon;

                    updateSlotVisual(clickedBtn, finalPokemon);
                    updateStartButton();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSlotVisual(Button btn, Pokemon p) {
        btn.setText("");
        
        try {
            String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) stream = getClass().getResourceAsStream("/com/pokemon/assets/sprites/missingno.png");
            Image img = new Image(stream);
            ImageView iv = new ImageView(img);
            iv.setFitHeight(80);
            iv.setFitWidth(80);
            iv.setPreserveRatio(true);
            btn.setGraphic(iv);
        } catch (Exception e) {
            btn.setText(p.getName().toUpperCase());
        }

        ScaleTransition st = new ScaleTransition(Duration.millis(300), btn);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(1.0); st.setToY(1.0);
        st.play();
        int slotIndex = Integer.parseInt(btn.getId().replaceAll("[^0-9]", ""));
        getRemoveButtonByIndex(slotIndex).setVisible(true);
    }

    private void animateClick(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setFromX(1.0); st.setToX(0.9);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    private Pokemon[] generateCpuTeam(int teamSize) {
    Pokemon[] cpuTeam = new Pokemon[6];
    Random random = new Random();
    List<Pokemon> availableSpecies = com.pokemon.core.PokemonDataManager.loadPokemonsFromCSV("/com/pokemon/data/pokemons.csv");

    if (availableSpecies.isEmpty()) {
        System.err.println("Erreur : Aucun Pokémon chargé depuis le CSV pour l'équipe CPU !");
        return cpuTeam;
    }

    for (int i = 0; i < teamSize; i++) {
        Pokemon basePkmn = availableSpecies.get(random.nextInt(availableSpecies.size()));
        
        Pokemon newCpuPkmn = new Pokemon(
            basePkmn.getId(), basePkmn.getName(), basePkmn.getMaxHp(),
            basePkmn.getAttack(), basePkmn.getDefense(), basePkmn.getSpeed(),
            basePkmn.getTypes(), new Attack[4], null, 
            basePkmn.getLearble(), basePkmn.getSpAttack(), basePkmn.getSpDefense()
        );

        Attack[] pool = newCpuPkmn.getLearble();
        if (pool != null && pool.length > 0) {
            List<Attack> availableMoves = new ArrayList<>();
            for (Attack a : pool) if (a != null) availableMoves.add(a);
            
            if (!availableMoves.isEmpty()) {
                Collections.shuffle(availableMoves);
                Attack[] finalAtks = new Attack[4];
                for (int j = 0; j < Math.min(4, availableMoves.size()); j++) {
                    finalAtks[j] = availableMoves.get(j);
                }
                newCpuPkmn.setAttacks(finalAtks);
            }
        }
        
        cpuTeam[i] = newCpuPkmn;
    }
    return cpuTeam;
}

    @FXML
    private void handleStartBattle(ActionEvent event) throws IOException {
        AudioManager.playSound("clic.wav");
        AudioManager.stopMusic();
        AudioManager.playMusic("fight.mp3");
        int count = 0;
        for (Pokemon p : team) if (p != null) count++;

        if (count < 3) return;

        Team pTeam = new Team(this.team);
        Team cTeam = new Team(generateCpuTeam(count));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/battle_view.fxml"));
        Parent root = loader.load();
        BattleController ctrl = loader.getController();
        ctrl.setupBattle(pTeam, cTeam);

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        AudioManager.playSound("clic.wav");
        Parent root = FXMLLoader.load(getClass().getResource("/com/pokemon/views/start_view.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    @FXML
private void handleRemovePokemon(ActionEvent event) {
    AudioManager.playSound("clic.wav");
    Button removeBtn = (Button) event.getSource();
    int slotIndex = Integer.parseInt(removeBtn.getId().replaceAll("[^0-9]", "")) - 1;

    this.team[slotIndex] = null;

    Button slotBtn = getSlotButtonByIndex(slotIndex + 1);
    slotBtn.setGraphic(null);
    slotBtn.setText("+");
    slotBtn.getStyleClass().remove("pokemon-slot-filled");
    slotBtn.getStyleClass().add("pokemon-slot");

    removeBtn.setVisible(false);
    updateStartButton();
}

private Button getRemoveButtonByIndex(int index) {
    return switch (index) {
        case 1 -> remove1Btn; case 2 -> remove2Btn; case 3 -> remove3Btn;
        case 4 -> remove4Btn; case 5 -> remove5Btn; default -> remove6Btn;
    };
}

private Button getSlotButtonByIndex(int index) {
    return switch (index) {
        case 1 -> slot1Btn; case 2 -> slot2Btn; case 3 -> slot3Btn;
        case 4 -> slot4Btn; case 5 -> slot5Btn; default -> slot6Btn;
    };
}
@FXML
private void handleRandomTeam(ActionEvent event) {
    AudioManager.playSound("clic.wav");
    Random random = new Random();
    
    List<Pokemon> allSpecies = com.pokemon.core.PokemonDataManager.loadPokemonsFromCSV("/com/pokemon/data/pokemons.csv");

    if (allSpecies.isEmpty()) {
        System.err.println("Erreur : Impossible de charger les Pokémon pour l'équipe aléatoire.");
        return;
    }

    for (int i = 0; i < 6; i++) {
        Pokemon basePkmn = allSpecies.get(random.nextInt(allSpecies.size()));
        
        Pokemon newPkmn = new Pokemon(
            basePkmn.getId(), basePkmn.getName(), basePkmn.getMaxHp(),
            basePkmn.getAttack(), basePkmn.getDefense(), basePkmn.getSpeed(),
            basePkmn.getTypes(), new Attack[4], null, 
            basePkmn.getLearble(), basePkmn.getSpAttack(), basePkmn.getSpDefense()
        );
        Attack[] learnablePool = newPkmn.getLearble();
        if (learnablePool != null) {
            List<Attack> validMoves = new ArrayList<>();
            for (Attack a : learnablePool) {
                if (a != null) validMoves.add(a);
            }

            if (!validMoves.isEmpty()) {
                Collections.shuffle(validMoves);
                Attack[] finalAtks = new Attack[4];
                for (int j = 0; j < Math.min(4, validMoves.size()); j++) {
                    finalAtks[j] = validMoves.get(j);
                }
                newPkmn.setAttacks(finalAtks);
            }
        }

        this.team[i] = newPkmn;
        Button slotBtn = getSlotButtonByIndex(i + 1);
        updateSlotVisual(slotBtn, newPkmn);
    }
    updateStartButton();
}

private void updateStartButton() {
    if (startBtn == null) return; // Sécurité si le FXML n'est pas encore lié
    int count = 0;
    for (Pokemon p : team) if (p != null) count++;

    if (count < 3) {
        startBtn.setDisable(true);
        startBtn.setText((3 - count) + " POKÉMON REQUIS");
    } else {
        startBtn.setDisable(false);
        startBtn.setText("LANCER LE COMBAT");
    }
}
}