package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Burn extends Status {
    public Burn(){
        super("Burn",
                "a chaque tour le pokemon perd 1/16 de sa vie");
    }
    public int damage(int vie){
        vie = (int) (vie * 0.9375);
        return vie;
    }
    @Override
    public void onTurnStart(Pokemon pokemon) {
        // a modif afin d'ajouter la réduction d'attaque
    }

    @Override
    public void onTurnEnd(Pokemon pokemon) {
        pokemon.setHp((int)(pokemon.getHp() * 0.9375));
    }
}
