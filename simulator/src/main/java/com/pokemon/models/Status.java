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
    public void updateDuration(){
        if (this.duration > 0) {
            this.duration--;
        }
    }
    public void getStatusAply(){
        if (duration <= 0) {
            System.out.println("status");
        }
    }
    public boolean isExpired() {
        return duration == 0;
    }
    public void Setduration(int duration){
        this.duration += duration;
    }
}
