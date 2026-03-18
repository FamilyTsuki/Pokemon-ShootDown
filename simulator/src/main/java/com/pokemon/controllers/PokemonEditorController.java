package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.models.items.Ballon;
import com.pokemon.models.items.Reste;
import com.pokemon.models.Attack;
import com.pokemon.models.Item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import com.pokemon.core.AudioManager;

public class PokemonEditorController {

    @FXML private Label pokemonNameLabel;
    @FXML private ImageView pokemonSprite;
    @FXML private ComboBox<String> move1, move2, move3, move4;
    @FXML private ComboBox<String> itemComboBox;
    @FXML private Label itemDescriptionLabel; 

    private Pokemon currentPokemon;
    private boolean saveClicked = false;
    private boolean isUpdating = false;
    
    private final List<Item> availableItems = new ArrayList<>();

    public void setPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        this.pokemonNameLabel.setText(pokemon.getName().toUpperCase());

        try {
            String imagePath = "/com/pokemon/assets/sprites/" + pokemon.getId() + ".png";
            var stream = getClass().getResourceAsStream(imagePath);
            if (stream == null) stream = getClass().getResourceAsStream("/com/pokemon/assets/sprites/missingno.png");
            if (stream != null) pokemonSprite.setImage(new Image(stream));
        } catch (Exception e) { e.printStackTrace(); }

        setupMoveComboBoxes(pokemon);
        setupItemComboBox();
    }

    private void setupItemComboBox() {
        availableItems.clear();
        availableItems.add(new Ballon());
        availableItems.add(new Reste());

        ObservableList<String> itemNames = FXCollections.observableArrayList();
        itemNames.add("None");
        for (Item it : availableItems) {
            itemNames.add(it.getName());
        }

        itemComboBox.setItems(itemNames);

        itemComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateItemDescription(newVal);
        });

        if (currentPokemon.getItem() != null) {
            String itemName = currentPokemon.getItem().getName();
            itemComboBox.getSelectionModel().select(itemName);
            updateItemDescription(itemName);
        } else {
            itemComboBox.getSelectionModel().select("None");
            updateItemDescription("None");
        }
    }

    private void updateItemDescription(String itemName) {
        if (itemName == null || itemName.equals("None")) {
            itemDescriptionLabel.setText("Aucun objet tenu.");
            return;
        }

        for (Item it : availableItems) {
            if (it.getName().equals(itemName)) {
                itemDescriptionLabel.setText(it.getDescription());
                return;
            }
        }
    }

    @FXML
    private void handleSave() {
        AudioManager.playSound("clic.wav");
        if (currentPokemon != null) {
            saveMoves();
            String selectedName = itemComboBox.getValue();
            if (selectedName == null || selectedName.equals("None")) {
                currentPokemon.setItem(null);
            } else {
                for (Item it : availableItems) {
                    if (it.getName().equals(selectedName)) {
                        currentPokemon.setItem(it);
                        break;
                    }
                }
            }

            this.saveClicked = true;
            ((Stage) pokemonNameLabel.getScene().getWindow()).close();
        }
    }

    private void setupMoveComboBoxes(Pokemon pokemon) {
        ComboBox<String>[] boxes = new ComboBox[]{move1, move2, move3, move4};
        
        isUpdating = true;

        List<String> pool = new ArrayList<>();
        if (pokemon.getLearble() != null) {
            for (Attack move : pokemon.getLearble()) {
                if (move != null) {
                    pool.add(move.getName());
                }
            }
        }

        Attack[] current = pokemon.getAttacks();
        boolean hasExistingMoves = false;
        if (current != null) {
            for (Attack a : current) if (a != null) hasExistingMoves = true;
        }

        if (!hasExistingMoves && !pool.isEmpty()) {
            List<String> shuffledPool = new ArrayList<>(pool);
            Collections.shuffle(shuffledPool);
            for (int i = 0; i < boxes.length; i++) {
                if (i < shuffledPool.size()) boxes[i].setValue(shuffledPool.get(i));
            }
        } else if (hasExistingMoves) {
            for (int i = 0; i < boxes.length; i++) {
                if (i < current.length && current[i] != null) boxes[i].setValue(current[i].getName());
            }
        }

        for (ComboBox<String> box : boxes) {
            box.setOnAction(e -> refreshMoveOptions());
        }

        isUpdating = false; 
        refreshMoveOptions();

        int total = pool.size();
        move2.setDisable(total < 2);
        move3.setDisable(total < 3);
        move4.setDisable(total < 4);
    }

    private void refreshMoveOptions() {
        if (isUpdating) return; 
        
        isUpdating = true;
        ComboBox<String>[] boxes = new ComboBox[]{move1, move2, move3, move4};
        
        List<String> allPossibleMoves = new ArrayList<>();
        if (currentPokemon.getLearble() != null) {
            for (Attack a : currentPokemon.getLearble()) {
                if (a != null) allPossibleMoves.add(a.getName());
            }
        }

        for (int i = 0; i < boxes.length; i++) {
            String currentSelection = boxes[i].getValue();
            List<String> availableForThisBox = new ArrayList<>(allPossibleMoves);
            
            for (int j = 0; j < boxes.length; j++) {
                if (i != j) {
                    String otherValue = boxes[j].getValue();
                    if (otherValue != null) availableForThisBox.remove(otherValue);
                }
            }
            
            boxes[i].setItems(FXCollections.observableArrayList(availableForThisBox));
            boxes[i].setValue(currentSelection);
        }
        isUpdating = false;
    }

    private void saveMoves() {
        String[] selected = { move1.getValue(), move2.getValue(), move3.getValue(), move4.getValue() };
        List<Attack> finalMoves = new ArrayList<>();
        for (String name : selected) {
            if (name == null) continue;
            if (currentPokemon.getLearble() != null) {
                for (Attack a : currentPokemon.getLearble()) {
                    if (a != null && a.getName().equals(name)) {
                        finalMoves.add(a);
                        break;
                    }
                }
            }
        }

        Attack[] finalAttacks = new Attack[4];
        for (int i = 0; i < Math.min(4, finalMoves.size()); i++) {
            finalAttacks[i] = finalMoves.get(i);
        }
        currentPokemon.setAttacks(finalAttacks);
    }

    public boolean isSaveClicked() { return saveClicked; }
    public Pokemon getSelectedPokemon() { return currentPokemon; }
}