package com.pokemon.models;

public class Status {
    String name;
    int duration;
    String description;
    public Status(String name, int duration, String description){
        this.name = name;
        this.duration = duration;
        this.description = description;
    }
    public int getDuration() {
        return this.duration;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
}
