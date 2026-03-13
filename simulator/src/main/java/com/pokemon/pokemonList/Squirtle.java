package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Squirtle extends Pokemon {
    public Squirtle() {
        super(7,
                "Squirtle",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.WATER},
                null,
                null
        );
    }
}