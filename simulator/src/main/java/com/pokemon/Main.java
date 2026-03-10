package com.pokemon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Simulateur Pokemon - Prêt pour le combat !");
        Scene scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Pokemon ShootDown");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}