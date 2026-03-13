package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Bulbasaur extends Pokemon {
    public Bulbasaur() {
        super(1,
                "Bulbasaur",
                60, 10,
                25,
                15,
                new PokemonType[]{PokemonType.GRASS, PokemonType.POISON},
                null,
                null
        );
    }
}
