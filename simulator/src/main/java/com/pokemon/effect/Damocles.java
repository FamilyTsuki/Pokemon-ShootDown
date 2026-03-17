package com.pokemon.effect;

import com.pokemon.models.Pokemon;

public class Damocles extends Effecte {

    public Damocles() {
        // On garde la durée de 3 tours comme tu l'as demandé
        super("Damoclès", "Le lanceur subit 1/3 des dégâts infligés", 3);
    }

    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {
        // On vérifie si l'effet n'est pas expiré
        if (!isExpired()) {
            int recoil = damageDealt / 3;

            if (recoil > 0) {
                System.out.println(attacker.getName() + " subit le contrecoup de Damoclès ! (-" + recoil + " PV)");
                attacker.takeDamage(recoil);
            }
            decrementDuration();
        }
    }
}