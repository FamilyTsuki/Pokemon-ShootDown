package com.pokemon.models;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea; 

@FunctionalInterface
public interface MoveEffect {

    void apply(Pokemon user, Pokemon target, TextArea log);
}