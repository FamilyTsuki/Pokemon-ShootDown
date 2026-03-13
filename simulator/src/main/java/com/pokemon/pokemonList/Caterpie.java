package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Caterpie extends Pokemon {
    public Caterpie() {
        super(10,
                "Caterpie",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.BUG},
                null,
                null
                );
    }
}