package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Ember;
import com.pokemon.models.attack.Fire_Blast;
import com.pokemon.models.attack.Scratch;

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
                null,
                new Attack[]{ 
                    new Scratch(), 
                    new Ember(), 
                    new Fire_Blast() 
                },
                80,
                65
        );
    }
}
