package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import com.pokemon.status.Burn;

public class BurnEffect extends Effecte {
    public BurnEffect() {
        super("Effet Brûlure", "10% de chance de brûler l'adversaire", 1);
    }

    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {
        // Math.random() < 0.1 correspond à 10%
        if (Math.random() < 0.1 && defender.getCurrentStatus() == null) {
            defender.setCurrentStatus(new Burn());
            System.out.println(defender.getName() + " a été brûlé par l'attaque !");
        }
    }
}