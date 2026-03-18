package com.pokemon.models.items;

import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;

public class VesteCombat extends Item {

    public VesteCombat() {
        super("Veste Combat", "Augmente la Défense Spéciale de 50%");
    }

    @Override
    public void onReceiveDamage(Pokemon pokemon, Attack attack, int damage) {}

    @Override
    public void onTurnStart(Pokemon pokemon){}

    @Override
    public void onTurnEnd(Pokemon pokemon) {}

    @Override
    public void onAttack(Pokemon attacker, Pokemon target, Attack attack) {}

    @Override
    public Object use(Pokemon pokemon) {
        return null;
    }
    public boolean active = true;

    public void startFight (Pokemon pokemon) {
        if (active == true) {
            int defadd = pokemon.getDefense() / 5;
            pokemon.setDefmodif(defadd);
        }
    }
}