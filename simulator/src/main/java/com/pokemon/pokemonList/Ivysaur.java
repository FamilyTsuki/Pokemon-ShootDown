package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;

public class Ivysaur extends Pokemon {
    public Ivysaur() {
        super("Ivysaur", 100, 25, 25, 35, "f");
    }
    public void damageSuffered(damage) {
        int damage;
        this.hp -= damage;
    }
}