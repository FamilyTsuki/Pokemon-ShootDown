package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Ivysaur extends Pokemon {
    public Ivysaur() {
        super(2,
                "Ivysaur",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.GRASS, PokemonType.POISON},
                null,
                null
        );
    }
}