package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class BurnEffect extends Effect {
    public BurnEffect() {
        super("Burn Effect", "10% chance to burn the target", 1);
    }

    @Override
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        if (log != null) {
            log.appendText(target.getName() + " is burned!\n");
        }
    }

    @Override
    public void onAfterAttack(Pokemon atk, Pokemon def, int dmg) {
        // Reduced to 0.1 to match the 10% description
        if (Math.random() < 0.1 && def.getCurrentStatus() == null) {
            Pokemon.burn(16, def);
            def.addEffect(this);
        }
    }
}