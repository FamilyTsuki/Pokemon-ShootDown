package com.pokemon.models;

public abstract class Objet {
    private String name;
    private String description;

    public Objet(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public abstract void onTurnStart(Pokemon pokemon);

    public abstract void onTurnEnd(Pokemon pokemon);

    public abstract void onAttack(Pokemon attacker, Pokemon target, Attack attack);

    public abstract void onReceiveDamage(Pokemon pokemon, Attack attack, int damage);

    public abstract Object use(Pokemon pokemon);

    @Override
    public String toString() {
        return name + ": " + description;
    }


    
}
