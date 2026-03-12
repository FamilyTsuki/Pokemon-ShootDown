package com.pokemon.core;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;

public class GameEngine {
    private boolean running;
    private final int FPS = 60;
    private final long TARGET_TIME = 1000 / FPS;

    private BattleEngine battleEngine; 

    public GameEngine(BattleEngine logic) {
        this.battleEngine = logic;
        this.running = false;
    }

    public void start() {
        running = true;
        gameLoop();
    }

    private void gameLoop() {
        while (running) {
            long startTime = System.currentTimeMillis();

            update();
            render();

            long timeTaken = System.currentTimeMillis() - startTime;
            long sleepTime = TARGET_TIME - timeTaken;

            if (sleepTime > 0) {
                try { Thread.sleep(sleepTime); } 
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    private void update() {
  
        if (battleEngine.hasPendingAction()) {
            battleEngine.executeNextStep();
        }
    }

    private void render() {
        // C'est ici que tu demandes à JavaFX de rafraîchir 
        // les barres de vie (HP) ou l'historique.
        // ex: uiController.updateUI();
    }
}