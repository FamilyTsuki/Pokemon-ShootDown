package com.pokemon.controllers;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Team;
import com.pokemon.core.AudioManager;
import com.pokemon.core.PokemonDataManager;
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

public class TeamBuilderController {

    @FXML private Button slot1Btn, slot2Btn, slot3Btn, slot4Btn;
    @FXML private Button slot5Btn, slot6Btn, startBtn;
    @FXML private VBox rootContainer;
    @FXML private Button remove1Btn, remove2Btn, remove3Btn;
    @FXML private Button remove4Btn, remove5Btn, remove6Btn;

    private Pokemon[] team = new Pokemon[6];
    private static final String CSV_PATH = "/com/pokemon/data/pokemons.csv";

    @FXML
    public void initialize() {
        if (rootContainer != null) {
            rootContainer.setOpacity(0);
            FadeTransition ft = new FadeTransition(
                Duration.millis(800), rootContainer);
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
        
        int slotIdx = Integer.parseInt(id.replaceAll("[^0-9]", "")) - 1;
        animateClick(clickedBtn);
        openSelectionFlow(clickedBtn, slotIdx);
    }

    private void openSelectionFlow(Button btn, int index) {
        try {
            Pokemon chosen = showSelector();
            if (chosen != null) {
                Pokemon finalPkmn = showEditor(chosen);
                if (finalPkmn != null) {
                    this.team[index] = finalPkmn;
                    updateSlotVisual(btn, finalPkmn);
                    updateStartButton();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pokemon showSelector() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/pokemon/views/pokemon_selector.fxml"));
        Parent root = loader.load();
        PokemonSelectorController ctrl = loader.getController();

        Stage stage = createModalStage("Select a Pokemon", root);
        stage.showAndWait();
        return ctrl.getSelectedPokemon();
    }

    private Pokemon showEditor(Pokemon p) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/pokemon/views/pokemon_editor.fxml"));
        Parent root = loader.load();
        PokemonEditorController ctrl = loader.getController();
        ctrl.setPokemon(p);

        Stage stage = createModalStage("Configure " + p.getName(), root);
        stage.showAndWait();
        return ctrl.isSaveClicked() ? ctrl.getSelectedPokemon() : null;
    }

    private Stage createModalStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        
        stage.setScene(new Scene(root));
        
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        return stage;
    }

    private void updateSlotVisual(Button btn, Pokemon p) {
        btn.setText("");
        try {
            String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                stream = getClass().getResourceAsStream(
                    "/com/pokemon/assets/sprites/missingno.png");
            }
            ImageView iv = new ImageView(new Image(stream));
            iv.setFitHeight(80); iv.setFitWidth(80);
            iv.setPreserveRatio(true);
            btn.setGraphic(iv);
        } catch (Exception e) {
            btn.setText(p.getName().toUpperCase());
        }
        finishVisualUpdate(btn);
    }

    private void finishVisualUpdate(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), btn);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(1.0); st.setToY(1.0);
        st.play();
        int idx = Integer.parseInt(btn.getId().replaceAll("[^0-9]", ""));
        getRemoveButtonByIndex(idx).setVisible(true);
    }

    private void animateClick(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setFromX(1.0); st.setToX(0.9);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    private Pokemon[] generateCpuTeam(int size) {
        Pokemon[] cpuTeam = new Pokemon[6];
        Random rand = new Random();
        List<Pokemon> species = PokemonDataManager.loadPokemonsFromCSV(CSV_PATH);

        if (species.isEmpty()) return cpuTeam;

        for (int i = 0; i < size; i++) {
            Pokemon base = species.get(rand.nextInt(species.size()));
            cpuTeam[i] = cloneAndRandomizeMoves(base);
        }
        return cpuTeam;
    }

    private Pokemon cloneAndRandomizeMoves(Pokemon base) {
        Pokemon p = new Pokemon(
            base.getId(), base.getName(), base.getMaxHp(),
            base.getAttack(), base.getDefense(), base.getSpeed(),
            base.getTypes(), new Attack[4], null, 
            base.getLearble(), base.getSpAttack(), base.getSpDefense()
        );
        
        Attack[] pool = p.getLearble();
        if (pool != null) {
            List<Attack> valid = new ArrayList<>();
            for (Attack a : pool) if (a != null) valid.add(a);
            if (!valid.isEmpty()) {
                Collections.shuffle(valid);
                Attack[] atks = new Attack[4];
                for (int i=0; i < Math.min(4, valid.size()); i++) {
                    atks[i] = valid.get(i);
                }
                p.setAttacks(atks);
            }
        }
        return p;
    }

    @FXML
    private void handleStartBattle(ActionEvent event) throws IOException {
        AudioManager.playSound("clic.wav");
        int count = 0;
        for (Pokemon p : team) if (p != null) count++;
        if (count < 3) return;

        AudioManager.stopMusic();
        AudioManager.playMusic("fight.mp3");

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/com/pokemon/views/battle_view.fxml"));
        Parent root = loader.load();
        BattleController ctrl = loader.getController();
        ctrl.setupBattle(new Team(team), new Team(generateCpuTeam(count)));

        switchScene(event, root);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        AudioManager.playSound("clic.wav");
        Parent root = FXMLLoader.load(getClass().getResource(
            "/com/pokemon/views/start_view.fxml"));
        switchScene(event, root);
    }

    private void switchScene(ActionEvent event, Parent root) {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        
        stage.setScene(new Scene(root));
        
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }
    
    @FXML
    private void handleRemovePokemon(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        Button removeBtn = (Button) event.getSource();
        int idx = Integer.parseInt(removeBtn.getId().replaceAll("[^0-9]", ""))-1;

        this.team[idx] = null;
        Button slotBtn = getSlotButtonByIndex(idx + 1);
        slotBtn.setGraphic(null);
        slotBtn.setText("+");
        
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
        List<Pokemon> species = PokemonDataManager.loadPokemonsFromCSV(CSV_PATH);
        if (species.isEmpty()) return;

        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            Pokemon randomPkmn = cloneAndRandomizeMoves(
                species.get(rand.nextInt(species.size())));
            this.team[i] = randomPkmn;
            updateSlotVisual(getSlotButtonByIndex(i + 1), randomPkmn);
        }
        updateStartButton();
    }

    private void updateStartButton() {
        if (startBtn == null) return;
        int count = 0;
        for (Pokemon p : team) if (p != null) count++;

        if (count < 3) {
            startBtn.setDisable(true);
            startBtn.setText((3 - count) + " POKEMON REQUIRED");
        } else {
            startBtn.setDisable(false);
            startBtn.setText("LAUNCH BATTLE");
        }
    }
}