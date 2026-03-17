package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Burn extends Status {
    private int attack_storage;
    private boolean hasModifiedAttack = false;

    public Burn() {
        super("Burn", "L'attaque est divisée par 2 et le Pokémon perd 1/16 de ses PV max.");
    }

    @Override
    public void onTurnStart(Pokemon pokemon) {
        if (!hasModifiedAttack) {
            this.attack_storage = pokemon.getAttack();
            pokemon.setBaseAttack(this.attack_storage / 2);
            this.hasModifiedAttack = true;
        }
    }

    @Override
    public void onTurnEnd(Pokemon pokemon) {
        int damage = Math.max(1, pokemon.getMaxHp() / 16);
        pokemon.takeDamage(damage);
        if (hasModifiedAttack) {
            pokemon.setBaseAttack(this.attack_storage);
            this.hasModifiedAttack = false;
        }
    }
}