package com.pokemon.effect;

import com.pokemon.models.Pokemon;

import javafx.scene.control.TextArea;

public class Damocles extends Effecte {

    public Damocles() {
        super("Damoclès", "Le lanceur subit 1/3 des dégâts infligés", 3);
    }

    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {

        if (!isExpired()) {
            int recoil = damageDealt / 3;

            if (recoil > 0) {
                System.out.println(attacker.getName() + " subit le contrecoup de Damoclès ! (-" + recoil + " PV)");
                attacker.takeDamage(recoil);
            }
            decrementDuration();
        }
    }
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        if (log != null) log.appendText(user.getName() + " subit 1/3 !\n");
    }
}