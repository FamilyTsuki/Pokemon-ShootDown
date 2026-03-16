package com.pokemon.controllers;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Team;
import com.pokemon.pokemonList.*;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class TeamBuilderController {

    @FXML private Button slot1Btn, slot2Btn, slot3Btn, slot4Btn, slot5Btn, slot6Btn;
    @FXML private VBox rootContainer;

    private Pokemon[] team = new Pokemon[6];

    @FXML
    public void initialize() {
        if (rootContainer != null) {
            rootContainer.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(800), rootContainer);
            ft.setToValue(1.0);
            ft.play();
        }
    }

    @FXML
    private void handleSelectPokemon(ActionEvent event) {
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
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(80);
            iv.setFitWidth(80);
            iv.setPreserveRatio(true);
            btn.setGraphic(iv);
        } catch (Exception e) {
            btn.setText(p.getName().toUpperCase());
        }

        btn.setStyle("-fx-background-color: #27ae60; " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: #f1c40f; " +
                     "-fx-border-width: 3; " +
                     "-fx-border-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(241, 196, 15, 0.5), 15, 0.5, 0, 0);");
        
        ScaleTransition st = new ScaleTransition(Duration.millis(300), btn);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(1.0); st.setToY(1.0);
        st.play();
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
            } catch (Exception e) { newCpuPkmn = randomPkmn; }

            Attack[] learnable = newCpuPkmn.getLearble();
            if (learnable != null) {
                List<Attack> pool = new ArrayList<>();
                for(Attack a : learnable) if(a != null) pool.add(a);
                Collections.shuffle(pool);
                int numAtk = Math.min(4, pool.size());
                Attack[] finalAtks = new Attack[4];
                for(int j=0; j<numAtk; j++) finalAtks[j] = pool.get(j);
                newCpuPkmn.setAttacks(finalAtks);
            }
            cpuTeam[i] = newCpuPkmn;
        }
        return cpuTeam;
    }

    @FXML
    private void handleStartBattle(ActionEvent event) throws IOException {
        int count = 0;
        for (Pokemon p : team) if (p != null) count++;

        if (count == 0) return;

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
        Parent root = FXMLLoader.load(getClass().getResource("/com/pokemon/views/start_view.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}