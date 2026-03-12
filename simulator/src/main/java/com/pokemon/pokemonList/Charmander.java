package com.pokemon.pokemonList;

import com.pokemon.models.Pokemon;

public class Charmander extends Pokemon {
    public Charmander() {
        super("Charmander", 100, 25, 25, 35, "f");
    }
    public void damageSuffered(damage) {
        int damage;
        this.hp -= damage;
    }
}