package com.pokemon.items.UseableItems;

import com.pokemon.models.UseableItem;
import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class Antidote extends UseableItem {

    public Antidote() {
        super("Antidote", "Cures all status conditions (Burn, etc.)");
    }

    @Override
    public void use(Pokemon target, TextArea log) {
        if (target.getActiveEffects().isEmpty()) {
            if (log != null) {
                log.appendText("❌ " + target.getName() + " has no status.\n");
            }
            return;
        }

        target.getActiveEffects().clear();

        if (log != null) {
            log.appendText("✨ " + target.getName() + " is fully cured!\n");
        }
    }
}