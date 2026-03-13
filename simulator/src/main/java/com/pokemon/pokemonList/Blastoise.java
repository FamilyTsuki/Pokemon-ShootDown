package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.object.Ballon;

public class Blastoise extends Pokemon {
    public Blastoise() {
        super(9,
                "Blastoise",
                100,
                25,
                25,
                35,
                new PokemonType[]{new PokemonType("water")},
                null,
                null
        );
    }
}