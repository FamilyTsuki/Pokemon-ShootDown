package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Charmander extends Pokemon {
    public Charmander() {
        super(4,
                "Charmander",
                100, 25,
                25,
                35,
                new PokemonType[]{PokemonType.FIRE},
                null,
                null
        );
    }
}