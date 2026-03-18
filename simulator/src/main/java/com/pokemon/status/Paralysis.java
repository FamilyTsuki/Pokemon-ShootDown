package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Paralysis extends Status {
    private int originalSpd;
    private boolean isApplied = false;

    public Paralysis() {
        super("Paralysis", "Speed halved and 25% chance to be fully paralyzed.");
    }

    @Override
    public void onTurnStart(Pokemon p) {
        if (!isApplied) {
            originalSpd = p.getSpeed();
            p.setSpeed(originalSpd / 2);
            isApplied = true;
        }
        
        p.setCanAttack(Math.random() >= 0.25);
        if (!p.canAttack()) {
            System.out.println(p.getName() + " is paralyzed! It can't move!");
        }
    }

    @Override
    public void onTurnEnd(Pokemon p) {
        if (isApplied) {
            p.setSpeed(originalSpd);
            isApplied = false;
        }
    }
}