package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
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
import com.pokemon.items.items.AssaultVest;
import com.pokemon.items.items.Ballon;
import com.pokemon.items.items.BerryJuice;
import com.pokemon.items.items.FocusSash;
import com.pokemon.items.items.Leftovers;

public class PokemonEditorController {

    @FXML private Label pokemonNameLabel, itemDescriptionLabel;
    @FXML private ImageView pokemonSprite;
    @FXML private ComboBox<String> move1, move2, move3, move4, itemComboBox;

    private Pokemon currentPokemon;
    private boolean saveClicked = false;
    private boolean isUpdating = false;
    private final List<Item> availableItems = new ArrayList<>();

    public void setPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        this.pokemonNameLabel.setText(pokemon.getName().toUpperCase());
        loadSprite(pokemon);
        setupMoveComboBoxes(pokemon);
        setupItemComboBox();
    }

    private void loadSprite(Pokemon p) {
        try {
            String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                stream = getClass().getResourceAsStream(
                    "/com/pokemon/assets/sprites/missingno.png");
            }
            pokemonSprite.setImage(new Image(stream));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void setupItemComboBox() {
        availableItems.clear();
        availableItems.add(new Ballon());
        availableItems.add(new Leftovers());
        availableItems.add(new AssaultVest());
        availableItems.add(new BerryJuice());
        availableItems.add(new FocusSash());

        ObservableList<String> names = FXCollections.observableArrayList("None");
        for (Item it : availableItems) names.add(it.getName());
        itemComboBox.setItems(names);
        itemComboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, newVal) -> updateItemDescription(newVal));
        initSelectedItem();
    }

    private void initSelectedItem() {
        if (currentPokemon.getItem() != null) {
            String name = currentPokemon.getItem().getName();
            itemComboBox.getSelectionModel().select(name);
        } else {
            itemComboBox.getSelectionModel().select("None");
        }
    }

    private void updateItemDescription(String itemName) {
        if (itemName == null || itemName.equals("None")) {
            itemDescriptionLabel.setText("No held item.");
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
        saveMoves();
        saveItem();
        this.saveClicked = true;
        ((Stage) pokemonNameLabel.getScene().getWindow()).close();
    }

    private void saveItem() {
        String selected = itemComboBox.getValue();
        currentPokemon.setItem(null);
        if (selected != null && !selected.equals("None")) {
            for (Item it : availableItems) {
                if (it.getName().equals(selected)) {
                    currentPokemon.setItem(it);
                    break;
                }
            }
        }
    }

    private void setupMoveComboBoxes(Pokemon p) {
        ComboBox<String>[] boxes = new ComboBox[]{move1, move2, move3, move4};
        isUpdating = true;
        List<String> pool = getLearnableNames(p);
        assignInitialMoves(boxes, p, pool);
        for (ComboBox<String> box : boxes) {
            box.setOnAction(e -> refreshMoveOptions());
        }
        isUpdating = false;
        refreshMoveOptions();
        updateBoxesAvailability(boxes, pool.size());
    }

    private List<String> getLearnableNames(Pokemon p) {
        List<String> pool = new ArrayList<>();
        if (p.getLearble() != null) {
            for (Attack a : p.getLearble()) if (a != null) pool.add(a.getName());
        }
        return pool;
    }

    private void assignInitialMoves(ComboBox<String>[] boxes, 
                                   Pokemon p, List<String> pool) {
        Attack[] current = p.getAttacks();
        boolean empty = true;
        if (current != null) {
            for (Attack a : current) if (a != null) empty = false;
        }
        if (empty && !pool.isEmpty()) {
            List<String> shuffled = new ArrayList<>(pool);
            Collections.shuffle(shuffled);
            for (int i=0; i<boxes.length; i++) {
                if (i < shuffled.size()) boxes[i].setValue(shuffled.get(i));
            }
        } else {
            for (int i=0; i<boxes.length; i++) {
                if (i < current.length && current[i] != null) 
                    boxes[i].setValue(current[i].getName());
            }
        }
    }

    private void updateBoxesAvailability(ComboBox<String>[] boxes, int total) {
        for (int i = 1; i < boxes.length; i++) {
            boxes[i].setDisable(total < (i + 1));
        }
    }

    private void refreshMoveOptions() {
        if (isUpdating) return;
        isUpdating = true;
        ComboBox<String>[] boxes = new ComboBox[]{move1, move2, move3, move4};
        List<String> allMoves = getLearnableNames(currentPokemon);
        for (int i = 0; i < boxes.length; i++) {
            updateSingleBox(i, boxes, allMoves);
        }
        isUpdating = false;
    }

    private void updateSingleBox(int idx, ComboBox<String>[] boxes, 
                                List<String> all) {
        String currentSel = boxes[idx].getValue();
        List<String> available = new ArrayList<>(all);
        for (int j = 0; j < boxes.length; j++) {
            if (idx != j && boxes[j].getValue() != null) {
                available.remove(boxes[j].getValue());
            }
        }
        boxes[idx].setItems(FXCollections.observableArrayList(available));
        boxes[idx].setValue(currentSel);
    }

    private void saveMoves() {
        String[] selected = {move1.getValue(), move2.getValue(), 
                            move3.getValue(), move4.getValue()};
        Attack[] finalAttacks = new Attack[4];
        int count = 0;
        for (String name : selected) {
            if (name == null) continue;
            for (Attack a : currentPokemon.getLearble()) {
                if (a != null && a.getName().equals(name)) {
                    finalAttacks[count++] = a;
                    break;
                }
            }
        }
        currentPokemon.setAttacks(finalAttacks);
    }

    public boolean isSaveClicked() { return saveClicked; }
    public Pokemon getSelectedPokemon() { return currentPokemon; }
}