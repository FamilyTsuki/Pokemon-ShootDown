package com.pokemon.status;

import com.pokemon.models.Status;

public class Paralyse extends Status {
    public Paralyse() {
        super("Paralyse",
                "le pokemon a une chance sur 4 de ne pas pouvoir attaquer " +
                        "et vois sa vitesse etre diviser par 2"
                );
    }
    public int effect(int speed){
        speed = (int)(speed / 2);
        int rightattack = (int)(Math.random() * (4 - 1 + 1) + 1);
        return rightattack + speed;
    }
}
