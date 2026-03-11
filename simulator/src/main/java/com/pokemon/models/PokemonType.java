package com.pokemon.models;


import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public enum PokemonType {
    
    NORMAL("Normal"), 
    FIRE("Fire"), 
    WATER("Water"), 
    GRASS("Grass"), 
    ELECTRIC("Electric"), 
    ICE("Ice"), 
    FIGHTING("Fighting"), 
    POISON("Poison"), 
    GROUND("Ground"), 
    FLYING("Flying"), 
    PSYCHIC("Psychic"), 
    BUG("Bug"), 
    ROCK("Rock"), 
    GHOST("Ghost"), 
    DRAGON("Dragon"), 
    DARK("Dark"), 
    STEEL("Steel"), 
    FAIRY("Fairy");

    private final String name;
    private final Map<PokemonType, Double> efficiencyMap = new HashMap<>();

    PokemonType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    static {
        for (PokemonType type : PokemonType.values()) {
            type.initEfficiency();
        }
    }

    public double getEfficiencyAgainst(PokemonType defenderType) {
        return efficiencyMap.getOrDefault(defenderType, 1.0);
    }

 
    public double getEfficiencyAgainst(PokemonType[] defenderTypes) {
        double multiplier = 1.0;
        for (PokemonType type : defenderTypes) {
            multiplier *= this.getEfficiencyAgainst(type);
        }
        return multiplier;
    }

    private void addEfficiency(PokemonType target, double multiplicateur) {
        this.efficiencyMap.put(target, multiplicateur);
    }

    private void initEfficiency() {
        switch (this) {
            case NORMAL -> normalEfficiency();
            case FIRE -> fireEfficiency();
            case WATER -> waterEfficiency();
            case GRASS -> grassEfficiency();
            case ELECTRIC -> electricEfficiency();
            case ICE -> iceEfficiency();
            case FIGHTING -> fightingEfficiency();
            case POISON -> poisonEfficiency();
            case GROUND -> groundEfficiency();
            case FLYING -> flyingEfficiency();
            case PSYCHIC -> psychicEfficiency();
            case BUG -> bugEfficiency();
            case ROCK -> rockEfficiency();
            case GHOST -> ghostEfficiency();
            case DRAGON -> dragonEfficiency();
            case DARK -> darkEfficiency();
            case STEEL -> steelEfficiency();
            case FAIRY -> fairyEfficiency();
        }
    }

    private void normalEfficiency() {
        addEfficiency(ROCK, 0.5);
        addEfficiency(STEEL, 0.5);
        addEfficiency(GHOST, 0.0);
    }

    private void fireEfficiency() {
        addEfficiency(GRASS, 2.0);
        addEfficiency(ICE, 2.0);
        addEfficiency(BUG, 2.0);
        addEfficiency(STEEL, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(WATER, 0.5);
        addEfficiency(ROCK, 0.5);
        addEfficiency(DRAGON, 0.5);
    }

    private void waterEfficiency() {
        addEfficiency(FIRE, 2.0);
        addEfficiency(GROUND, 2.0);
        addEfficiency(ROCK, 2.0);
        addEfficiency(WATER, 0.5);
        addEfficiency(GRASS, 0.5);
        addEfficiency(DRAGON, 0.5);
    }

    private void grassEfficiency() {
        addEfficiency(WATER, 2.0);
        addEfficiency(GROUND, 2.0);
        addEfficiency(ROCK, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(GRASS, 0.5);
        addEfficiency(POISON, 0.5);
        addEfficiency(FLYING, 0.5);
        addEfficiency(BUG, 0.5);
        addEfficiency(DRAGON, 0.5);
        addEfficiency(STEEL, 0.5);
    }

    private void electricEfficiency() {
        addEfficiency(WATER, 2.0);
        addEfficiency(FLYING, 2.0);
        addEfficiency(ELECTRIC, 0.5);
        addEfficiency(GRASS, 0.5);
        addEfficiency(DRAGON, 0.5);
        addEfficiency(GROUND, 0.0);
    }

    private void iceEfficiency() {
        addEfficiency(GRASS, 2.0);
        addEfficiency(GROUND, 2.0);
        addEfficiency(FLYING, 2.0);
        addEfficiency(DRAGON, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(WATER, 0.5);
        addEfficiency(ICE, 0.5);
        addEfficiency(STEEL, 0.5);
    }

    private void fightingEfficiency() {
        addEfficiency(NORMAL, 2.0);
        addEfficiency(ICE, 2.0);
        addEfficiency(ROCK, 2.0);
        addEfficiency(DARK, 2.0);
        addEfficiency(STEEL, 2.0);
        addEfficiency(POISON, 0.5);
        addEfficiency(FLYING, 0.5);
        addEfficiency(PSYCHIC, 0.5);
        addEfficiency(BUG, 0.5);
        addEfficiency(FAIRY, 0.5);
        addEfficiency(GHOST, 0.0);
    }

    private void poisonEfficiency() {
        addEfficiency(GRASS, 2.0);
        addEfficiency(FAIRY, 2.0);
        addEfficiency(POISON, 0.5);
        addEfficiency(GROUND, 0.5);
        addEfficiency(ROCK, 0.5);
        addEfficiency(GHOST, 0.5);
        addEfficiency(STEEL, 0.0);
    }

    private void groundEfficiency() {
        addEfficiency(FIRE, 2.0);
        addEfficiency(ELECTRIC, 2.0);
        addEfficiency(POISON, 2.0);
        addEfficiency(ROCK, 2.0);
        addEfficiency(STEEL, 2.0);
        addEfficiency(GRASS, 0.5);
        addEfficiency(BUG, 0.5);
        addEfficiency(FLYING, 0.0);
    }

    private void flyingEfficiency() {
        addEfficiency(GRASS, 2.0);
        addEfficiency(FIGHTING, 2.0);
        addEfficiency(BUG, 2.0);
        addEfficiency(ELECTRIC, 0.5);
        addEfficiency(ROCK, 0.5);
        addEfficiency(STEEL, 0.5);
        addEfficiency(GROUND, 0.0);
    }

    private void psychicEfficiency() {
        addEfficiency(FIGHTING, 2.0);
        addEfficiency(POISON, 2.0);
        addEfficiency(PSYCHIC, 0.5);
        addEfficiency(STEEL, 0.5);
        addEfficiency(DARK, 0.0);
    }

    private void bugEfficiency() {
        addEfficiency(GRASS, 2.0);
        addEfficiency(PSYCHIC, 2.0);
        addEfficiency(DARK, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(FIGHTING, 0.5);
        addEfficiency(POISON, 0.5);
        addEfficiency(FLYING, 0.5);
        addEfficiency(GHOST, 0.5);
        addEfficiency(STEEL, 0.5);
        addEfficiency(FAIRY, 0.5);
    }

    private void rockEfficiency() {
        addEfficiency(FIRE, 2.0);
        addEfficiency(ICE, 2.0);
        addEfficiency(FLYING, 2.0);
        addEfficiency(BUG, 2.0);
        addEfficiency(FIGHTING, 0.5);
        addEfficiency(GROUND, 0.5);
        addEfficiency(STEEL, 0.5);
    }

    private void ghostEfficiency() {
        addEfficiency(PSYCHIC, 2.0);
        addEfficiency(GHOST, 2.0);
        addEfficiency(DARK, 0.5);
        addEfficiency(NORMAL, 0.0);
    }

    private void dragonEfficiency() {
        addEfficiency(DRAGON, 2.0);
        addEfficiency(STEEL, 0.5);
        addEfficiency(FAIRY, 0.0);
    }

    private void darkEfficiency() {
        addEfficiency(PSYCHIC, 2.0);
        addEfficiency(GHOST, 2.0);
        addEfficiency(FIGHTING, 0.5);
        addEfficiency(DARK, 0.5);
        addEfficiency(FAIRY, 0.5);
    }

    private void steelEfficiency() {
        addEfficiency(ICE, 2.0);
        addEfficiency(ROCK, 2.0);
        addEfficiency(FAIRY, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(WATER, 0.5);
        addEfficiency(ELECTRIC, 0.5);
        addEfficiency(STEEL, 0.5);
    }

    private void fairyEfficiency() {
        addEfficiency(FIGHTING, 2.0);
        addEfficiency(DRAGON, 2.0);
        addEfficiency(DARK, 2.0);
        addEfficiency(FIRE, 0.5);
        addEfficiency(POISON, 0.5);
        addEfficiency(STEEL, 0.5);
    }
    public Color getColor() {
    switch (this) {
        case FIRE: return Color.ORANGERED;
        case WATER: return Color.DEEPSKYBLUE;
        case GRASS: return Color.LIMEGREEN;
        case ELECTRIC: return Color.GOLD;
        case ICE: return Color.AQUA;
        case PSYCHIC: return Color.MAGENTA;
        case GHOST: return Color.PURPLE;
        case ROCK: return Color.SADDLEBROWN;
        default: return Color.WHITE; // Couleur par défaut (Normal)
    }
}

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}