package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Poison extends Status {

    public Poison() {
        super("Poison", "The Pokemon loses 1/8 of its max HP each turn.");
    }

    @Override
    public void onTurnStart(Pokemon p) {}

    @Override
    public void onTurnEnd(Pokemon p) {
        if (!p.isFainted()) {
            // Using your static method for fractional damage
            Pokemon.burn(8, p);
            System.out.println(p.getName() + " is hurt by poison!");
        }
    }
}