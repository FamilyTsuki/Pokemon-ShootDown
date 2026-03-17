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

public class PokemonEditorController {

    @FXML private Label pokemonNameLabel;
    @FXML private ImageView pokemonSprite;
    @FXML private ComboBox<String> move1, move2, move3, move4;
    @FXML private ComboBox<String> itemComboBox;

    private Pokemon currentPokemon;
    private boolean saveClicked = false;
    
    private final List<Item> availableItems = new ArrayList<>();

    public void setPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        this.pokemonNameLabel.setText(pokemon.getName().toUpperCase());

        try {
            String imagePath = "/com/pokemon/assets/sprites/" + pokemon.getId() + ".png";
            var stream = getClass().getResourceAsStream(imagePath);
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

        if (currentPokemon.getItem() != null) {
            itemComboBox.getSelectionModel().select(currentPokemon.getItem().getName());
        } else {
            itemComboBox.getSelectionModel().select("None");
        }
    }

    @FXML
    private void handleSave() {
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
        ObservableList<String> attackNames = FXCollections.observableArrayList();

        if (pokemon.getLearble() != null) {
            for (Attack move : pokemon.getLearble()) {
                if (move != null) {
                    attackNames.add(move.getName());
                }
            }
        }
        move1.setItems(attackNames);
        move2.setItems(attackNames);
        move3.setItems(attackNames);
        move4.setItems(attackNames);
        int count = attackNames.size();
        if (count > 0) move1.getSelectionModel().select(0);
        if (count > 1) move2.getSelectionModel().select(1);
        if (count > 2) move3.getSelectionModel().select(2);
        if (count > 3) move4.getSelectionModel().select(3);

        move2.setDisable(count < 2);
        move3.setDisable(count < 3);
        move4.setDisable(count < 4);
    }

    private void saveMoves() {
        String[] selected = { move1.getValue(), move2.getValue(), move3.getValue(), move4.getValue() };
        List<Attack> finalMoves = new ArrayList<>();
        for (String name : selected) {
            if (name == null) continue;
            for (Attack a : currentPokemon.getLearble()) {
                if (a.getName().equals(name)) {
                    finalMoves.add(a);
                    break;
                }
            }
        }
        currentPokemon.setAttacks(finalMoves.toArray(new Attack[0]));
    }

    public boolean isSaveClicked() { return saveClicked; }
    public Pokemon getSelectedPokemon() { return currentPokemon; }
}