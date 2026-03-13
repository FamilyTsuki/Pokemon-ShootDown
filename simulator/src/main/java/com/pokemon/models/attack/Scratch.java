package com.pokemon.models.attack;

import com.pokemon.models.Attack;
import com.pokemon.models.PokemonType;

public class Scratch extends Attack {
    public Scratch() {
        super("Scratch", 40, 100, PokemonType.NORMAL, "Physical");
    }

    
}
