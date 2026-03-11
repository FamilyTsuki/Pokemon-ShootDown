package com.pokemon.models;

public class Team {
    
    private Pokemon[] pokemons;
    private int activePokemonIndex;

    public Team(Pokemon[] pokemons) {
        
        this.pokemons = pokemons;
        this.activePokemonIndex = 0;
    }

    public Pokemon[] getPokemons() {
        return pokemons;
    }

    public Pokemon getActivePokemon() {
        if (activePokemonIndex < pokemons.length) {
            return pokemons[activePokemonIndex];
        }
        return null;
    }

    public boolean hasAvailablePokemon() {
        for (Pokemon p : pokemons) {
            if (!p.isFainted()) {
                return true;
            }
        }
        return false;
    }

    public void setActivePokemonIndex(int index) {
        if (index >= 0 && index < pokemons.length && !pokemons[index].isFainted()) {
            this.activePokemonIndex = index;
        }
    }

    @Override
    public String toString() {
        return " (Active: " + getActivePokemon().getName() + ")";
    }

    
}
