package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Charmeleon extends Pokemon {
    public Charmeleon() {
        super(5,
                "Charmeleon",
                60,
                10,
                25,
                15,
                new PokemonType[]{PokemonType.FIRE},
                null,
                null
        );
    }
}
