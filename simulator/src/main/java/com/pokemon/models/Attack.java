package com.pokemon.models;

import com.pokemon.effect.Effect;

public class Attack extends Move {
    private Effect effect;

    public Attack(String name, PokemonType type, int power, int accuracy, String category) {
        super(name, power, accuracy, type, category);
    }

    public void execute(Pokemon attacker, Pokemon defender) {
        System.out.println(attacker.getName() + " utilise " + this.getName() + "!");
        
        if (Math.random() * 100 > this.getAccuracy()) {
            System.out.println("L'attaque a échoué !");
            return;
        }

        double damage = com.pokemon.core.DamageCalculator.calculateDamage(attacker, defender, this);
        int finalDamage = (int) Math.round(damage);
        
        defender.takeDamage(finalDamage);
        System.out.println(defender.getName() + " a reçu " + finalDamage + " dégâts !");

    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }
    
}