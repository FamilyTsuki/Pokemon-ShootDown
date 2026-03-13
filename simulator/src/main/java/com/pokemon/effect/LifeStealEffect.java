package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class LifeStealEffect extends Effect {
    public LifeStealEffect() {
        super("Life Steal", "Heals the user by 1/2 of damage dealt", 1);
    }

    @Override
    public void onAfterAttack(Pokemon atk, Pokemon def, int dmg) {
        int heal = dmg / 2;
        if (heal > 0) {
            int newHp = Math.min(atk.getMaxHp(), atk.getHp() + heal);
            atk.setHp(newHp);
        }
    }

    @Override
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        if (log != null) {
            log.appendText(user.getName() + " restored some HP!\n");
        }
    }
}