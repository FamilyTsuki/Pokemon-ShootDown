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

public class PokemonSelectorController {

    @FXML private TextField searchField;
    @FXML private TableView<Pokemon> pokemonTable;
    @FXML private TableColumn<Pokemon, Void> img;
    @FXML private TableColumn<Pokemon, String> nameCol;
    @FXML private TableColumn<Pokemon, PokemonType[]> typeCol;
    @FXML private TableColumn<Pokemon, Integer> hpCol, atkCol, defCol;
    @FXML private TableColumn<Pokemon, Integer> spaCol, spdCol, speCol;

    private Pokemon selectedPokemon;
    private final ObservableList<Pokemon> masterData = 
        FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadData();
        setupImageColumn();
        setupStatsColumns();
        setupTypeColumn();
        setupSearchAndEvents();
    }

    private void loadData() {
        List<Pokemon> pokedex = PokemonDataManager.loadPokemonsFromCSV(
            "/com/pokemon/data/pokemons.csv");
        masterData.addAll(pokedex);
    }

    private void setupImageColumn() {
        img.setCellFactory(param -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            { iv.setFitHeight(30); iv.setFitWidth(30); iv.setPreserveRatio(true); }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Pokemon p = getTableView().getItems().get(getIndex());
                setGraphic(loadSprite(p, iv));
            }
        });
    }

    private ImageView loadSprite(Pokemon p, ImageView iv) {
        try {
            String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
            var stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                stream = getClass().getResourceAsStream(
                    "/com/pokemon/assets/sprites/missingno.png");
            }
            iv.setImage(new Image(stream));
            return iv;
        } catch (Exception e) { return null; }
    }

    private void setupStatsColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkCol.setCellValueFactory(new PropertyValueFactory<>("attack"));
        defCol.setCellValueFactory(new PropertyValueFactory<>("defense"));
        spaCol.setCellValueFactory(new PropertyValueFactory<>("spAttack"));
        spdCol.setCellValueFactory(new PropertyValueFactory<>("spDefense"));
        speCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
    }

    private void setupTypeColumn() {
        typeCol.setCellValueFactory(new PropertyValueFactory<>("types"));
        typeCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(PokemonType[] types, boolean empty) {
                super.updateItem(types, empty);
                if (empty || types == null) { setGraphic(null); return; }
                HBox container = new HBox(5);
                container.setAlignment(Pos.CENTER);
                for (PokemonType t : types) {
                    container.getChildren().add(createTypeBadge(t));
                }
                setGraphic(container);
            }
        });
    }

    private Label createTypeBadge(PokemonType t) {
        Label badge = new Label(t.toString().toUpperCase());
        badge.getStyleClass().addAll("type-badge", 
            "type-" + t.toString().toLowerCase());
        badge.setStyle("-fx-text-fill: white;");
        return badge;
    }

    private void setupSearchAndEvents() {
        FilteredList<Pokemon> filteredData = new FilteredList<>(masterData);
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(p -> {
                if (newV == null || newV.isEmpty()) return true;
                return p.getName().toLowerCase().contains(newV.toLowerCase());
            });
        });
        pokemonTable.setItems(filteredData);
        setupTableClick();
    }

    private void setupTableClick() {
        pokemonTable.setOnMouseClicked(event -> {
            Pokemon p = pokemonTable.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && p != null) {
                AudioManager.playSound("clic.wav");
                this.selectedPokemon = p;
                closeWindow();
            }
        });
    }

    public Pokemon getSelectedPokemon() { return selectedPokemon; }

    private void closeWindow() {
        if (pokemonTable.getScene() != null) {
            Stage stage = (Stage) pokemonTable.getScene().getWindow();
            stage.close();
        }
    }
}