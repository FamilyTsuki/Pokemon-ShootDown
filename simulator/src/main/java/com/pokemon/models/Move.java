package com.pokemon.models;

public class Move {
    private String name;
    private int power;
    private int accuracy;
    private PokemonType type;
    private String category; //TODO category

    public Move(String name, int power, int accuracy, PokemonType type, String category) {
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.type = type;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public PokemonType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s (Type: %s, Power: %d, Acc: %d)", name, type.getName(), power, accuracy);
    }

    
}
