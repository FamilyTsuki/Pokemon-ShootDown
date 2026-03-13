package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public abstract class Effect {
    private String name;
    private String description;
    private int duration;

    public Effect(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public void decrementDuration() {
        if (this.duration > 0) this.duration--;
    }

    public boolean isExpired() { return duration == 0; }

    public abstract void apply(Pokemon user, Pokemon target, TextArea log);

    public abstract void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt);

    @Override
    public String toString() {
        return String.format("%s: %s (Turns remaining: %d)", name, description, duration);
    }
}