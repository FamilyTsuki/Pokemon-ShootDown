package com.pokemon.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class StartController {

    @FXML
    private void handleStart(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/team_builder.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        
        stage.setScene(new Scene(root));
        stage.setTitle("Pokémon ShootDown - Préparation de l'équipe");
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        System.exit(0);
    }
}