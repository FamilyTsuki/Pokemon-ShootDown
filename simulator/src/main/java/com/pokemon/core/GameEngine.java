package com.pokemon.core;

public class GameEngine {
    private boolean running;
    private final int FPS = 60;
    private final long TARGET_TIME = 1000 / FPS;

    public GameEngine() {
        this.running = false;
    }

    public void start() {
        running = true;
        gameLoop();
    }

    public void stop() {
        running = false;
    }

    private void gameLoop() {
        while (running) {
            long startTime = System.currentTimeMillis();

            update();
            render();

            long timeTaken = System.currentTimeMillis() - startTime;
            long sleepTime = TARGET_TIME - timeTaken;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void update() {
        //TODO Logic for updating game state
    }

    private void render() {
        //TODO Logic for rendering the game
    }
    

}
