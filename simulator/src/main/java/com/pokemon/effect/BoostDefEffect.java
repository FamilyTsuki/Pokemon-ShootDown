package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import javafx.scene.control.TextArea;

public class BoostDefEffect extends Effecte {

    public BoostDefEffect() {
        super("Boost Défense", "Augmente la défense du lanceur", 0);
    }

    @Override
    public void apply(Pokemon user, Pokemon target, TextArea log) {
        int bonus = 10;
        user.setDefense(user.getDefense() + bonus);
        
        if (log != null) {
            log.appendText("🛡️ La défense de " + user.getName() + " augmente de " + bonus + " !\n");
        }
    }

    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {

    }
}