package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class Damocles extends Effect {
    public Damocles() {
        super("Damocles", "User takes 1/3 of damage dealt as recoil", 3);
    }

    @Override
    public void onAfterAttack(Pokemon atk, Pokemon def, int dmg) {
        if (!isExpired()) {
            int recoil = dmg / 3;
            if (recoil > 0) {
                atk.takeDamage(recoil);
            }
            decrementDuration();
        }
    }

    @Override
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        if (log != null) {
            log.appendText(user.getName() + " takes recoil damage!\n");
        }
    }
}