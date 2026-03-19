package com.pokemon.core;

import com.pokemon.models.*;

public class DamageCalculator {
    public static double calculateDamage(Pokemon atk, Pokemon def, Attack mov) {
        // Pokemon damage formula: ((2*Lvl/5 + 2) * Power * A/D / 50) + 2
        double base = (22.0 * mov.getPower() * atk.getAttack() / def.getDefense() 
                      / 50.0) + 2.0;

        double mod = calculateModifier(atk, def, mov.getType());

        if (atk.getItem() != null) {
            mod *= atk.getItem().getDamageModifier(atk, def, mov);
        }
        if (def.getItem() != null) {
            mod *= def.getItem().getDamageModifier(def, atk, mov);
        }

        return base * mod;
    }

    private static double calculateModifier(Pokemon atk, Pokemon def, 
                                            PokemonType mType) {
        double stab = 1.0;
        for (PokemonType t : atk.getTypes()) if (t == mType) stab = 1.5;

        double eff = mType.getEfficiencyAgainst(def.getTypes());
        double rand = 0.85 + (Math.random() * 0.15);

        return stab * eff * rand;
    }
}