package com.pokemon.models;

public abstract class Item {
    private String name;
    private String description;

    // merci dany de nous avoir demander de faire un les item
    // alors qu'ils ne sont meme pas présent dans le jeu de base

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public double getDamageModifier(Pokemon defender,Pokemon attacker,Attack attack){
        return 1;
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
