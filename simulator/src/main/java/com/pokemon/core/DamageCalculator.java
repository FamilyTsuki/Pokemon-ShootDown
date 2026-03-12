package com.pokemon.core;
import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;


public class DamageCalculator {
    /**
     * 
     * @param attacker
     * @param defender
     * @param movePower
     * @param moveType
     * @return
     */
    public static double calculateDamage(Pokemon attacker, Pokemon defender, Attack attack) {

    double baseDamage = ((((2.0 * 50.0 / 5.0) + 2.0) 
                        * attack.getPower() 
                        * ((double) attacker.getStrenght() / defender.getDefense())) 
                        / 50.0) + 2.0;

    double modifier = calculateModifier(attacker, defender, attack.getType());

    if (attacker.getItem() != null) {
        modifier *= attacker.getItem().getDamageModifier(attacker, defender, attack);
    }

    if (defender.getItem() != null) {
        modifier *= defender.getItem().getDamageModifier(defender, attacker, attack);
    }

    return baseDamage * modifier;
}

    private static double calculateModifier(Pokemon attacker, Pokemon defender, PokemonType moveType) {
        double stab = 1.0;
        for (PokemonType type : attacker.getType()){
            if (type == moveType) {
                stab = 1.5;
            }
        }
        double typeEffectiveness = moveType.getEfficiencyAgainst(defender.getType());


        double random = 0.85 + (Math.random() * (1.0 - 0.85));

        return stab * typeEffectiveness * random;
    }
    
}

