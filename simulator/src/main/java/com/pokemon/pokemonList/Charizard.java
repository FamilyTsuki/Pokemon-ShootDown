package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Charizard extends Pokemon {
    public Charizard() {

        super(6,
                "Charizard",
                100,
                25,
                25,
                35,
                new PokemonType[]{ PokemonType.FIRE, PokemonType.FLYING },
                null,
                null);
    }
}