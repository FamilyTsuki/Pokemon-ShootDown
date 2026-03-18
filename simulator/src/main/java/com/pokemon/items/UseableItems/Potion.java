package com.pokemon.items.UseableItems;

import com.pokemon.models.Pokemon;

import javafx.scene.control.TextArea;
import com.pokemon.models.UseableItem;

public class Potion extends UseableItem {
    public Potion() {
        super("Potion", "Soigne 50 PV");
    }

    @Override
    public void use(Pokemon target, TextArea log) {
        int healAmount = 50;
        int oldHp = target.getHp();
        target.setHp(Math.min(target.getMaxHp(), target.getHp() + healAmount));
        int restored = target.getHp() - oldHp;
        
        if (log != null) {
            log.appendText(target.getName() + " utilise une Potion et regagne " + restored + " PV !\n");
        }
    }
}