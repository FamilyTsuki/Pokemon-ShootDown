package com.pokemon.models;
import com.pokemon.models.Move;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class Attack extends Move{
    public Attack(String name, int power, int accuracy, PokemonType type, String category) {
        super(name, power, accuracy, type, category);
    }

    public void execute(Pokemon attacker, Pokemon defender) {
        System.out.println(attacker.getName() + " uses " + this.getName() + "!");
        
        if (Math.random() * 100 > this.getAccuracy()) {
            System.out.println("The attack missed!");
            return;
        }

        double damage = com.pokemon.core.DamageCalculator.calculateDamage(attacker, defender, this);
        
        int finalDamage = (int) Math.round(damage);
        attacker.getItem().onAttack(attacker, defender, this);
        defander.getItem().onReceiveDamage(defender, this, finalDamage);

        defender.takeDamage(finalDamage);
        System.out.println(defender.getName() + " took " + finalDamage + " damage!");
    }




    
}
