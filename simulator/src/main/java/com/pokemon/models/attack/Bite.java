package com.pokemon.models.attack;

import com.pokemon.models.Attack;
import com.pokemon.models.PokemonType;

public class Bite extends Attack {
    public Bite() {
        super("Bite", 60, 100, PokemonType.DARK, "Physical");
    }

    
}
