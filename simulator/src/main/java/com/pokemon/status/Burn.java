package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Burn extends Status {
    private int originalAtk;
    private boolean isApplied = false;

    public Burn() {
        super("Burn", "Attack halved and loses 1/16 max HP per turn.");
    }

    @Override
    public void onTurnStart(Pokemon p) {
        if (!isApplied) {
            originalAtk = p.getAttack();
            p.setBaseAttack(originalAtk / 2);
            isApplied = true;
        }
    }

    @Override
    public void onTurnEnd(Pokemon p) {
        p.burn(16, p);
        if (isApplied) {
            p.setBaseAttack(originalAtk);
            isApplied = false;
        }
    }
}