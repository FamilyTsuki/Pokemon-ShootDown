package com.pokemon.items.UseableItems;

import com.pokemon.models.UseableItem;
import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class Antidote extends UseableItem {

    public Antidote() {
        super("Antidote", "Soigne tous les problèmes de statut (Brûlure, etc.)");
    }

    @Override
    public void use(Pokemon target, TextArea log) {
        if (target.getActiveEffects().isEmpty()) {
            if (log != null) log.appendText("❌ " + target.getName() + " n'a aucun problème de statut.\n");
            return;
        }

        target.getActiveEffects().clear();

        if (log != null) {
            log.appendText("✨ " + target.getName() + " est guéri de tous ses maux !\n");
        }
    }
}