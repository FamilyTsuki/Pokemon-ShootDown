package com.pokemon.items.UseableItems;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;
import com.pokemon.models.UseableItem;

public class Potion extends UseableItem {
    public Potion() {
        super("Potion", "Heals 50 HP");
    }

    @Override
    public void use(Pokemon target, TextArea log) {
        int heal = 50;
        int oldHp = target.getHp();
        int newHp = Math.min(target.getMaxHp(), oldHp + heal);
        target.setHp(newHp);
        
        int restored = newHp - oldHp;
        if (log != null) {
            log.appendText(target.getName() + " uses Potion: +" + 
                restored + " HP!\n");
        }
    }
}