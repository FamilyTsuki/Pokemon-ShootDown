package com.pokemon.core;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioManager {
    private static MediaPlayer backgroundMusic;
    private static final String SOUND_PATH = "/com/pokemon/assets/sounds/";

    // 1. Jouer un cri ou un bruitage (Action immédiate)
    public static void playSound(String fileName) {
    try {
        // On construit le chemin complet
        String path = "/com/pokemon/assets/sounds/" + fileName;
        URL resource = AudioManager.class.getResource(path);
        
        if (resource == null) {
            System.err.println("⚠️ Impossible de trouver le son : " + path);
            return; // On arrête la méthode proprement sans crasher
        }

        // Utilise toExternalForm() au lieu de getPath()
        AudioClip clip = new AudioClip(resource.toExternalForm());
        clip.play();
        
    } catch (Exception e) {
        System.err.println("❌ Erreur lors de la lecture du son " + fileName + " : " + e.getMessage());
    }
}

    // 2. Jouer la musique de combat (En boucle)
    public static void playMusic(String fileName) {
        if (backgroundMusic != null) backgroundMusic.stop();

        try {
            URL resource = AudioManager.class.getResource(SOUND_PATH + fileName);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Boucle infinie
                backgroundMusic.setVolume(0.5); // 50% du volume
                backgroundMusic.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }
}