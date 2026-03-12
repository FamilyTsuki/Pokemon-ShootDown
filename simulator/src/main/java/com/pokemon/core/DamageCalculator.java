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
        // Basic damage formula: ((2 * Level / 5 + 2) * Power * A/D / 50 + 2) * Modifier
        
        double baseDamage = ((((2.0 * 50.0 / 5.0) + 2.0) * attack.getPower() * ((double) attacker.getAttack() / defender.getDefense())) / 50.0) + 2.0;

        double modifier = calculateModifier(attacker, defender, attack.getType());

        return baseDamage * modifier;
    }

    private static double calculateModifier(Pokemon attacker, Pokemon defender, PokemonType moveType) {
        double stab = 1.0;
        if (attacker.getType().equalsIgnoreCase(moveType.getName())) {
            stab = 1.5;
        }

        double typeEffectiveness = 1.0;
        PokemonType defenderType = TypeRepository.get(defender.getType());
        
        if (defenderType != null) {
            typeEffectiveness = moveType.getEfficiencyAgainst(defenderType);
        }

        // Random factor between 0.85 and 1.0
        double random = 0.85 + (Math.random() * (1.0 - 0.85));

        return stab * typeEffectiveness * random;
    }
    
}
