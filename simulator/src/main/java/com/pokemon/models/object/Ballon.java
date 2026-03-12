package com.pokemon.models.object;

import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.Attack;

public class Ballon extends Item {

    private boolean active = true;

    public Ballon() {
        super("Ballon", "Immunité aux attaques Sol jusqu'à ce que le Pokémon soit touché.");
    }

    public boolean useCondition(Pokemon pokemon, PokemonType moveType) {

        if (active && moveType.getName().equalsIgnoreCase("ground")) {
            return true;
        }

        return false;
    }

    @Override
    public Item use(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " est immunisé grâce au Ballon !");
        //TODO fair que l'object ranvoi son efect
        return null;
    }

    public double getDamageModifier(Pokemon defender,Pokemon attacker,Attack attack){
        return 0.0;
    }

    @Override
    public void onReceiveDamage(Pokemon pokemon, Attack attack, int damage) {

        if (!active) return;

        if (useCondition(pokemon, attack.getType())) {
            use(pokemon);
            return;
        }

        active = false;
        System.out.println("Le Ballon de " + pokemon.getName() + " éclate !");
    }

    @Override
    public void onTurnStart(Pokemon pokemon) {}

    @Override
    public void onTurnEnd(Pokemon pokemon) {}

    @Override
    public void onAttack(Pokemon attacker, Pokemon target, Attack attack) {}
}