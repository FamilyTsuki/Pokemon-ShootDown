package com.pokemon.models;

import java.util.HashMap;
import java.util.Map;


public class PokemonType {
    private final String name;
    private final Map<PokemonType, Double> efficiency ;

    public PokemonType(String name) {
        this.name = name;
        this.efficiency = new HashMap<>();
    }

    public String getName() {
        return name;
    
    }

    public void addEfficiency(PokemonType target, double multiplicateur) {
        efficiency.put(target, multiplicateur);
    }
    
    public double getEfficiencyAgainst(PokemonType[] target) {
        for (PokemonType type : target) {
            if (efficiency.containsKey(type)) {
                return efficiency.get(type);
            }
        }
        return 1.0;
    }

    @Override
    public String toString() {
        return name;
    
    }
}