package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Ember;
import com.pokemon.models.attack.Fire_Blast;
import com.pokemon.models.attack.Scratch;

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
                null,
                new Attack[]{ 
                    new Scratch(), 
                    new Ember(), 
                    new Fire_Blast() 
                },
                109,
                85
            
            );
    }
}