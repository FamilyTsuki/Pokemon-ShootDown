package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public abstract class Effecte {
    private String name;
    private String description;
    private int duration;

    public Effecte(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    // --- Tes méthodes existantes (on ne change rien) ---
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public void decrementDuration() {
        if (this.duration > 0) this.duration--;
    }

    public boolean isExpired() { return duration == 0; }

    // --- TES DEUX MÉTHODES ABSTRAITES ---

    // 1. Pour l'effet immédiat de l'attaque (Soin, Boost, etc.)
    public abstract void apply(Pokemon user, Pokemon target, TextArea log);

    // 2. Pour les effets après les dégâts (Recul, Vol de vie, etc.)
    public abstract void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt);

    @Override
    public String toString() {
        return String.format("%s: %s (Turns remaining: %d)", name, description, duration);
    }
}