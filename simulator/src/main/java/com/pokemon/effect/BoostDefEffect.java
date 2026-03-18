package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class BoostDefEffect extends Effect {

    public BoostDefEffect() {
        super("Defense Boost", "Increases the user's defense", 0);
    }

    @Override
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        int bonus = 10;
        user.setDefense(user.getDefense() + bonus);
        
        if (log != null) {
            log.appendText("🛡️ " + user.getName() + "'s defense rose by " + 
                bonus + "!\n");
        }
    }

    @Override
    public void onAfterAttack(Pokemon atk, Pokemon def, int dmg) {
        // No post-attack effect needed
    }
}