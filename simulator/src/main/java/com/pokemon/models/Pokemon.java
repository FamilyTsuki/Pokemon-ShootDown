package com.pokemon.models;

public class Pokemon {
    private String name;
    protected int hp;
    private int maxHp;
    private int strenght;
    private int defense;
    private int speed;
    private Type[] type;

    public Pokemon(String name, int hp, int strenght, int defense, int speed, String type) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.strenght = strenght;
        this.defense = defense;
        this.speed = speed;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return strenght;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public String getType() {
        return type;
    }

    public boolean isFainted() {
        return this.hp <= 0;
    }

    public void takeDamage(int damage) {
        this.setHp(this.hp - damage);
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, Type: %s)", name, hp, maxHp, type);
    }
}
