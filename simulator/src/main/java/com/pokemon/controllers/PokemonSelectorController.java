package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.pokemonList.*; 
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PokemonSelectorController {

    @FXML private TextField searchField;
    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, Void> img; 
    @FXML private TableColumn<Pokemon, String> nameCol;
    @FXML private TableColumn<Pokemon, String> typeCol; 
    @FXML private TableColumn<Pokemon, Integer> hpCol, atkCol, defCol, spaCol, spdCol, speCol;

    private Pokemon selectedPokemon;
    private final ObservableList<Pokemon> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        masterData.addAll(
            new Bulbasaur(), new Ivysaur(), new Venusaur(),
            new Charmander(), new Charmeleon(), new Charizard(),
            new Squirtle(), new Wartortle(), new Blastoise()
        );

        img.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Pokemon, Void> call(TableColumn<Pokemon, Void> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();
                    {
                        imageView.setFitHeight(30);
                        imageView.setFitWidth(30);
                        imageView.setPreserveRatio(true);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Pokemon pokemon = getTableView().getItems().get(getIndex());
                            try {
                                
                                String path = "/com/pokemon/assets/sprites/" + pokemon.getId() + ".png";
                                var stream = getClass().getResourceAsStream(path);
                                if (stream != null) {
                                    imageView.setImage(new Image(stream));
                                    setGraphic(imageView);
                                } else {
                                    setGraphic(null);
                                }
                            } catch (Exception e) {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkCol.setCellValueFactory(new PropertyValueFactory<>("attack"));
        defCol.setCellValueFactory(new PropertyValueFactory<>("defense"));
        spaCol.setCellValueFactory(new PropertyValueFactory<>("spAttack"));
        spdCol.setCellValueFactory(new PropertyValueFactory<>("spDefense"));
        speCol.setCellValueFactory(new PropertyValueFactory<>("speed"));

        typeCol.setCellValueFactory(cellData -> {
            PokemonType[] types = cellData.getValue().getTypes();
            if (types == null || types.length == 0) {
                return new SimpleStringProperty("-");
            }
            String typesString = Arrays.stream(types)
                                       .map(Object::toString)
                                       .collect(Collectors.joining(" / "));
            return new SimpleStringProperty(typesString);
        });

        FilteredList<Pokemon> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pokemon -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return pokemon.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        pokemonTable.setItems(filteredData);

        pokemonTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && pokemonTable.getSelectionModel().getSelectedItem() != null) {
                this.selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
                System.out.println("Sélecteur : Pokémon choisi = " + selectedPokemon.getName());
                closeWindow();
            }
        });
    }

    public Pokemon getSelectedPokemon() {
        return selectedPokemon;
    }

    private void closeWindow() {
        Stage stage = (Stage) pokemonTable.getScene().getWindow();
        stage.close();
    }
}