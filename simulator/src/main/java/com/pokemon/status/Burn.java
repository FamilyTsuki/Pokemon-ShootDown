package com.pokemon.status;

import com.pokemon.models.Status;

public class Burn extends Status {
    public Burn(){
        super("Burn", 3,
                "a chaque tour le pokemon perd 1/16 de sa vie");
    }
    public int damage(int vie){
        vie = (int) (vie * 0.9375);
        return vie;
    }
}
