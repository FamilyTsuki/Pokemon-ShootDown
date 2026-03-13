package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Bite;
import com.pokemon.models.attack.Bubble_Beam;
import com.pokemon.models.attack.Tackle;
import com.pokemon.models.attack.Water_Gun;

public class Squirtle extends Pokemon {
    public Squirtle() {
        super(7,
                "Squirtle",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.WATER},
                null,
                null,
                new Attack[]{ 
                    new Tackle(), 
                    new Water_Gun(), 
                    new Bubble_Beam(), 
                    new Bite() 
                },
                50,
                64
        );
    }
}