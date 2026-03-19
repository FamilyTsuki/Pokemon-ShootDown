package com.pokemon.models;

public abstract class Status {
    String name;
    String description;
    public Status(String name, String description){
        this.name = name;
        this.description = description;
    }
    public static String[] statusInit(){
        String[] statusList = new String[3];
        statusList[0] = "Paralyse";
        statusList[1] = "Burn";
        statusList[2] = "poison";
        return statusList;
    }
    public boolean getStatusActif(){
        return true;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public abstract void onTurnStart(Pokemon pokemon);

    public abstract void onTurnEnd(Pokemon pokemon);

}
