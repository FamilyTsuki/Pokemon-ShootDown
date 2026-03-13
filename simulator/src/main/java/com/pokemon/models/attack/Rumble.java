package com.pokemon.models.attack;

import com.pokemon.models.Attack;
import com.pokemon.models.PokemonType;

public class Rumble extends Attack {

    public Rumble() {

        super("Rumble", 10, 100, PokemonType.NORMAL, "Physical"); 
    }
}