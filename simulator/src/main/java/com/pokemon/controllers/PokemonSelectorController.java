package com.pokemon.controllers;

import java.util.List;

import com.pokemon.core.AudioManager;
import com.pokemon.core.PokemonDataManager;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.pokemon.core.AudioManager;


public class PokemonSelectorController {

    @FXML private TextField searchField;
    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, Void> img; 
    @FXML private TableColumn<Pokemon, String> nameCol;
    
    @FXML private TableColumn<Pokemon, PokemonType[]> typeCol; 
    
    @FXML private TableColumn<Pokemon, Integer> hpCol, atkCol, defCol, spaCol, spdCol, speCol;

    private Pokemon selectedPokemon;
    private final ObservableList<Pokemon> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        List<Pokemon> pokedex = PokemonDataManager.loadPokemonsFromCSV("/com/pokemon/data/pokemons.csv");
        masterData.addAll(pokedex);

        img.setCellFactory(param -> new TableCell<>() {
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
                        if (stream == null) stream = getClass().getResourceAsStream("/com/pokemon/assets/sprites/missingno.png");
                        if (stream != null) {
                            imageView.setImage(new Image(stream));
                            setGraphic(imageView);
                        }
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkCol.setCellValueFactory(new PropertyValueFactory<>("attack"));
        defCol.setCellValueFactory(new PropertyValueFactory<>("defense"));
        spaCol.setCellValueFactory(new PropertyValueFactory<>("spAttack"));
        spdCol.setCellValueFactory(new PropertyValueFactory<>("spDefense"));
        speCol.setCellValueFactory(new PropertyValueFactory<>("speed"));

        typeCol.setCellValueFactory(new PropertyValueFactory<>("types"));
        typeCol.setCellFactory(column -> new TableCell<Pokemon, PokemonType[]>() {
            @Override
            protected void updateItem(PokemonType[] types, boolean empty) {
                super.updateItem(types, empty);

                if (empty || types == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox container = new HBox(5);
                    container.setAlignment(Pos.CENTER);

                    for (PokemonType type : types) {
                        Label badge = new Label(type.toString().toUpperCase());
                        badge.getStyleClass().add("type-badge"); 
                        
                        String typeClass = "type-" + type.toString().toLowerCase();
                        badge.getStyleClass().add(typeClass);
                        
                        badge.setStyle("-fx-text-fill: white;");
                        
                        container.getChildren().add(badge);
                    }
                    setGraphic(container);
                }
            }
        });

        FilteredList<Pokemon> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pokemon -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return pokemon.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        pokemonTable.setItems(filteredData);

        pokemonTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && pokemonTable.getSelectionModel().getSelectedItem() != null) {
                AudioManager.playSound("clic.wav");
                this.selectedPokemon = pokemonTable.getSelectionModel().getSelectedItem();
                closeWindow();
            }
        });
    }

    public Pokemon getSelectedPokemon() {
        return selectedPokemon;
    }

    private void closeWindow() {
        if (pokemonTable.getScene() != null) {
            Stage stage = (Stage) pokemonTable.getScene().getWindow();
            stage.close();
        }
    }
}