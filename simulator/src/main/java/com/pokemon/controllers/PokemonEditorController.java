package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;
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

    
    public void setPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        this.pokemonNameLabel.setText(pokemon.getName().toUpperCase());

        try {
            String imagePath = "/com/pokemon/assets/sprites/" + pokemon.getId() + ".png";
            var inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream != null) {
                pokemonSprite.setImage(new Image(inputStream));
            } else {
                System.out.println("Image manquante : " + imagePath);

            }
        } catch (Exception e) {
            System.err.println("Erreur chargement image");
        }

        ObservableList<String> attackNames = FXCollections.observableArrayList();
        if (pokemon.getLearble() != null) {
            for (Attack move : pokemon.getLearble()) {
                if (move != null) attackNames.add(move.getName());
            }
        }

        move1.setItems(attackNames);
        move2.setItems(attackNames);
        move3.setItems(attackNames);
        move4.setItems(attackNames);

        if (attackNames.size() >= 1) move1.getSelectionModel().select(0);
        if (attackNames.size() >= 2) move2.getSelectionModel().select(1);
        if (attackNames.size() >= 3) move3.getSelectionModel().select(2);
        if (attackNames.size() >= 4) move4.getSelectionModel().select(3);

        ObservableList<String> items = FXCollections.observableArrayList(
            "Life Orb", "Leftovers", "Choice Band", "Choice Specs", "Focus Sash", "None"
        );
        itemComboBox.setItems(items);
        itemComboBox.getSelectionModel().select("None");
    }



    @FXML
    private void handleSave() {
        if (currentPokemon != null) {

            String[] selectedNames = { move1.getValue(), move2.getValue(), move3.getValue(), move4.getValue() };
            List<Attack> finalMoves = new ArrayList<>();
            
            Attack[] learnable = currentPokemon.getLearble();
            if (learnable != null) {
                for (String name : selectedNames) {
                    if (name == null) continue;
                    for (Attack a : learnable) {
                        if (a.getName().equals(name)) {
                            finalMoves.add(a);
                            break;
                        }
                    }
                }
            }
            
            currentPokemon.setAttacks(finalMoves.toArray(new Attack[0]));
            this.saveClicked = true;

            Stage stage = (Stage) pokemonNameLabel.getScene().getWindow();
            stage.close();
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Pokemon getSelectedPokemon() {
        return currentPokemon;
    }
    
    public String getSelectedItem() {
        return itemComboBox.getValue();
    }
}