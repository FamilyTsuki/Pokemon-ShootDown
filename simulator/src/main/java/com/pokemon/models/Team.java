package com.pokemon.models;

public class Team {
    private Pokemon[] pokemons;
    private Pokemon activePokemon;

    public Team(Pokemon[] pokemons) {
        this.pokemons = pokemons;
        this.activePokemon = pokemons[0];
    }

    public Pokemon[] getPokemons() { return pokemons; }
    
    public Pokemon getActivePokemon() { return activePokemon; }

    public void setActivePokemon(Pokemon pokemon) {
        this.activePokemon = pokemon;
    }

    public Pokemon getNextAvailablePokemon() {
        for (Pokemon p : pokemons) {
            if (p != null && !p.isFainted()) {
                return p;
            }
        }
        return null;
    }

    public boolean hasAvailablePokemon() {
        return getNextAvailablePokemon() != null;
    }
}