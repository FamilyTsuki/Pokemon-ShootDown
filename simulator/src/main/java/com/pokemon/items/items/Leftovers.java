package com.pokemon.items.items;

import com.pokemon.models.Attack;
import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Leftovers extends Item {
    private boolean active = true;

    public Leftovers() {
        super("Restes", "restaure 1/16ème des PV à la fin de chaque tour");
    }

    @Override
    public Item use(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " utilise " + getName() + " : " + getDescription());
        return this;
    }

    public int calculateHeal(Pokemon pokemon) {
        return (int) (pokemon.getMaxHp() * 0.08);
    }

    @Override
    public void onTurnStart(Pokemon pokemon) {}

    @Override
    public void onTurnEnd(Pokemon pokemon) {
        int healAmount = calculateHeal(pokemon);
        pokemon.setHp(pokemon.getHp() + healAmount);
        System.out.println(pokemon.getName() + " regagne des PV grâce à ses Restes !");
    }

    @Override
    public void onAttack(Pokemon attacker, Pokemon target, Attack attack) {}

    @Override
    public void onReceiveDamage(Pokemon pokemon, Attack attack, int damage) {}
}