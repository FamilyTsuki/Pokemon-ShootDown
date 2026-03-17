package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Poison extends Status {

    public Poison() {
        super("poison", "diminue la vie du pokemon de 1/8 a chaque tour");
    }

    @Override
    public void onTurnStart(Pokemon pokemon) {}

    @Override
    public void onTurnEnd(Pokemon pokemon) {
        if (!pokemon.isFainted()) {
            int damage = pokemon.getMaxHp() / 8;
            if (damage < 1) damage = 1;
            pokemon.takeDamage(damage);
        }
    }
}
