package com.pokemon.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import com.pokemon.core.AudioManager;

public class StartController {

    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        AudioManager.playMusic("home.mp3");
        AudioManager.playSound("clic.wav");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pokemon/views/team_builder.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
    
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("Pokémon ShootDown - Préparation de l'équipe");
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        AudioManager.stopMusic();
        System.exit(0);
        
    }
}