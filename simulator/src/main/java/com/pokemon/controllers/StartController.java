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

    private static final String TEAM_BUILDER_VIEW = 
        "/com/pokemon/views/team_builder.fxml";

    /**
     * Handles the transition to the team builder view.
     */
    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        AudioManager.playMusic("home.mp3");
        AudioManager.playSound("clic.wav");

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(TEAM_BUILDER_VIEW));
        Parent root = loader.load();

        Stage stage = (Stage) ((Button) event.getSource())
            .getScene().getWindow();
    
        setupStage(stage, root);
    }

    /**
     * Configures the stage with the new scene and title.
     */
    private void setupStage(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Pokémon ShootDown - Team Preparation");
        
        stage.setFullScreen(true);
        
        stage.setFullScreenExitHint("");
        stage.setAlwaysOnTop(false);
    }

    /**
     * Terminates the application.
     */
    @FXML
    private void handleQuit(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        AudioManager.stopMusic();
        System.exit(0);
    }
}