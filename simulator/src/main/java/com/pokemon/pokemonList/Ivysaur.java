package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Razor_Leaf;
import com.pokemon.models.attack.Solar_Beam;
import com.pokemon.models.attack.Tackle;
import com.pokemon.models.attack.Vine_Whip;

public class Ivysaur extends Pokemon {
    public Ivysaur() {
        super(2,
                "Ivysaur",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.GRASS, PokemonType.POISON},
                null,
                null,
                new Attack[]{ 
                    new Tackle(), 
                    new Vine_Whip(), 
                    new Razor_Leaf(), 
                    new Solar_Beam() 
                },
                80,
                80
        );
    }
}