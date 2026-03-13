package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Bite;
import com.pokemon.models.attack.Bubble_Beam;
import com.pokemon.models.attack.Hydro_Pump;

import com.pokemon.models.attack.Tackle;

import com.pokemon.models.attack.Water_Gun;

public class Blastoise extends Pokemon {
    public Blastoise() {

        super(9, 
                "Blastoise",
                79, 
                83,
                100,
                78,
                new PokemonType[]{PokemonType.WATER},
                null,
                null,
                new Attack[]{ 
                    new Tackle(), 
                    new Water_Gun(), 
                    new Bubble_Beam(), 
                    new Bite(), 
                    new Hydro_Pump()
                },
                85,
                105

        );
    }
}