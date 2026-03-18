package com.pokemon.core;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioManager {
    private static MediaPlayer music;
    private static final String PATH = "/com/pokemon/assets/sounds/";

    public static void playSound(String file) {
        try {
            URL res = AudioManager.class.getResource(PATH + file);
            if (res == null) res = AudioManager.class.getResource(PATH + "1.wav");
            
            if (res != null) {
                new AudioClip(res.toExternalForm()).play();
            }
        } catch (Exception e) {
            System.err.println("Sound error: " + file);
        }
    }

    public static void playMusic(String file) {
        stopMusic();
        try {
            URL res = AudioManager.class.getResource(PATH + file);
            if (res != null) {
                music = new MediaPlayer(new Media(res.toExternalForm()));
                music.setCycleCount(MediaPlayer.INDEFINITE);
                music.setVolume(0.5);
                music.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (music != null) music.stop();
    }
}