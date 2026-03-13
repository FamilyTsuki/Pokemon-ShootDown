package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Blastoise extends Pokemon {
    public Blastoise() {
        super(1,
                "Blastoise",
                60, 10,
                25,
                15,
                new PokemonType[]{PokemonType.GRASS, PokemonType.POISON},
                null,
                null
        );
    }
}
