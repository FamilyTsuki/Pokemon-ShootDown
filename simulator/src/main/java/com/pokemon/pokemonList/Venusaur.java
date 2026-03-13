package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Venusaur extends Pokemon {
    public Venusaur() {
        super(3,
                "Venusaur",
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
