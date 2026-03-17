package com.pokemon.models;

public class Attack extends Move {
    private MoveEffect effect;

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

    public void setEffect(MoveEffect effect) {
        this.effect = effect;
    }

    public MoveEffect getEffect() {
        return effect;
    }
}