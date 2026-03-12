package com.pokemon.effect;

public class Effecte {
    private String name;
    private String description;
    private int duration;

    public Effecte(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void decrementDuration() {
        if (this.duration > 0) {
            this.duration--;
        }
    }

    public boolean isExpired() {
        return duration == 0;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (Turns remaining: %d)", name, description, duration);
    }

    
}
