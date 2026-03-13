package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Wartortle extends Pokemon {
    public Wartortle() {
        super(8,
                "Wartortle",
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