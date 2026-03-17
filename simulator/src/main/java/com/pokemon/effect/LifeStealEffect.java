package com.pokemon.effect;

import com.pokemon.models.Pokemon;

public class LifeStealEffect extends Effecte {
    public LifeStealEffect() {
        super("Vol de vie", "Soigne le lanceur de 1/2 des dégâts infligés", 1);
    }

    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {
        int healAmount = damageDealt / 2;
        if (healAmount > 0) {
            // On utilise setHp pour soigner (ton setHp limite déjà au maxHp)
            attacker.setHp(attacker.getHp() + healAmount);
            System.out.println(attacker.getName() + " récupère " + healAmount + " PV !");
        }
    }
}