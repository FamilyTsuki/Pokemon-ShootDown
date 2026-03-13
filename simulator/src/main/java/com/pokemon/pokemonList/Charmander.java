package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Ember;
import com.pokemon.models.attack.Fire_Blast;
import com.pokemon.models.attack.Scratch;

public class Charmander extends Pokemon {
    public Charmander() {
        super(4,
                "Charmander",
                100, 25,
                25,
                35,
                new PokemonType[]{PokemonType.FIRE},
                null,
                null,
                new Attack[]{ 
                    new Scratch(), 
                    new Ember(), 
                    new Fire_Blast() 
                },
                60,
                50
        );
    }
}