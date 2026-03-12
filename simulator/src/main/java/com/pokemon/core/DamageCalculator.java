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
    // 1. Calcul des dégâts de base (Formule officielle)
    // Note : On utilise 50.0 pour le niveau car c'est le standard Showdown
    double baseDamage = ((((2.0 * 50.0 / 5.0) + 2.0) 
                        * attack.getPower() 
                        * ((double) attacker.getAttack() / defender.getDefense())) 
                        / 50.0) + 2.0;

    // 2. Calcul du modificateur de base (STAB, Type, Random)
    double modifier = calculateModifier(attacker, defender, attack.getType());

    // 3. Application des effets d'objets (Items)
    // On vérifie si l'attaquant a un objet qui booste l'attaque (ex: Choice Band, Life Orb)
    if (attacker.getItem() != null) {
        modifier *= attacker.getItem().getDamageModifier(attacker, defender, attack);
    }

    // On vérifie si le défenseur a un objet qui réduit les dégâts (ex: Ballon, Baies)
    if (defender.getItem() != null) {
        modifier *= defender.getItem().getDamageModifier(defender, attacker, attack);
    }

    // 4. Résultat final
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


        // Random factor between 0.85 and 1.0
        double random = 0.85 + (Math.random() * (1.0 - 0.85));

        return stab * typeEffectiveness * random;
    }
    
}

