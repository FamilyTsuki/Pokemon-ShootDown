package com.pokemon.status;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Status;

public class Paralyse extends Status {
    public Paralyse() {
        super("Paralyse",
                "le pokemon a une chance sur 4 de ne pas pouvoir attaquer " +
                        "et vois sa vitesse etre diviser par 2"
                );
    }

    public void onTurnStart(Pokemon pokemon) {
        int rightattack = (int)(Math.random() * (4 - 1 + 1) + 1);
        if (rightattack == 1) {
            //faire en sorte que cela l'empeche d'attaque durant le tour
        }
        //modif la vitesse
    }

    public void onTurnEnd(Pokemon pokemon) {
        pokemon.setHp((int)(pokemon.getHp() * 0.9375));
    }
}
