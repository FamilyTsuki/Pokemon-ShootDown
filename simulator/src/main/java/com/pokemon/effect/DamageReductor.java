package com.pokemon.effect;

public class DamageReductor extends Effecte {
    private double reduction;

    public DamageReductor(String name, String description, int duration, double reduction) {
        super(name, description, duration);
        this.reduction = reduction;
    }

    public double getreduction() {
        return reduction * 100 ;
    }

    public void setreduction(double reduction) {
        this.reduction = reduction;
    }

    

    
}
