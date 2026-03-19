package com.pokemon.models;


public abstract class UseableItem {
    private String name;
    private String description;


    public UseableItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void use(Pokemon target, javafx.scene.control.TextArea log);

    public String getName() { return name; }
}