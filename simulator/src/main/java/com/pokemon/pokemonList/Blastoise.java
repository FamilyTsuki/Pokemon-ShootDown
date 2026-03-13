package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Rumble;

public class Blastoise extends Pokemon {
    public Blastoise() {

        super(9, 
                "Blastoise",
                79, 
                83,
                100,
                78,
                new PokemonType[]{PokemonType.WATER},
                new Attack[]{ new Rumble() },
                null
        );
    }
}