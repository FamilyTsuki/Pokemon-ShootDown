package com.pokemon.models;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea; // Import important

@FunctionalInterface
public interface MoveEffect {
    // On remplace BattleLog par TextArea (le type de ton battleLog dans le contrôleur)
    void apply(Pokemon user, Pokemon target, TextArea log);
}