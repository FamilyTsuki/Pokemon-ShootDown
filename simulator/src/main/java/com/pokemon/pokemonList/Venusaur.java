package com.pokemon.pokemonList;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.attack.Razor_Leaf;
import com.pokemon.models.attack.Solar_Beam;
import com.pokemon.models.attack.Tackle;
import com.pokemon.models.attack.Vine_Whip;

public class Venusaur extends Pokemon {
    public Venusaur() {
        super(3,
                "Venusaur",
                100,
                25,
                25,
                35,
                new PokemonType[]{PokemonType.WATER},
                null,
                null,
                new Attack[]{ 
                    new Tackle(), 
                    new Vine_Whip(), 
                    new Razor_Leaf(), 
                    new Solar_Beam() 
                },
                100,
                100
        );
    }
}
