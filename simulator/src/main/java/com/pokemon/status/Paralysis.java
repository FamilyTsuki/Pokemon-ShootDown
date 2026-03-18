package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Paralysis extends Status {
    private int speed_storage;
    private boolean hasModifiedSpeed = false;

    public Paralysis() {
        super("Paralysis", "25% de chance de ne pas pouvoir attaquer et vitesse divisée par 2.");
    }

    @Override
    public void onTurnStart(Pokemon pokemon) {
        if (!hasModifiedSpeed) {
            this.speed_storage = pokemon.getSpeed();
            pokemon.setSpeed(- (this.speed_storage / 2));
            this.hasModifiedSpeed = true;
        }

        int chance = (int)(Math.random() * 4);
        if (chance == 0) {
            System.out.println(pokemon.getName() + " est paralysé ! Il ne peut pas attaquer !");
            pokemon.setCanAttack(false);
        }
    }

    @Override
    public void onTurnEnd(Pokemon pokemon) {
        if (hasModifiedSpeed) {
            pokemon.setSpeed(speed_storage);
            this.hasModifiedSpeed = false;
        }
    }
}