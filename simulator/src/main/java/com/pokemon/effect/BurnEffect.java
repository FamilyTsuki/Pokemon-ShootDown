package com.pokemon.effect;

import com.pokemon.models.Pokemon;
import com.pokemon.status.Burn;

import javafx.scene.control.TextArea;

public class BurnEffect extends Effecte {
    public BurnEffect() {
        super("Effet Brûlure", "10% de chance de brûler l'adversaire", 1);
    }

    public void apply(Pokemon user, Pokemon target, TextArea log) {
        if (log != null) log.appendText(target.getName() + " est brûlé !\n");
    }
    @Override
    public void onAfterAttack(Pokemon attacker, Pokemon defender, int damageDealt) {
        if (Math.random() < 0.5 && defender.getCurrentStatus() == null) {
            defender.setCurrentStatus(new Burn());
            System.out.println(defender.getName() + " a été brûlé par l'attaque !");
            defender.addEffect(this);
        }
    }
}