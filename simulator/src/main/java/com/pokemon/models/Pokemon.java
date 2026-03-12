package com.pokemon.models;

public class Pokemon {
    private int id;
    private String name;
    private int hp;
    private int maxHp;
    private int strenght;
    private int defense;
    private int speed;
    private PokemonType[] type;
    private Attack[] attacks;
    private Object object;


    public Pokemon(int id, String name, int hp, int strenght, int defense, int speed, PokemonType[] type, Attack[] attacks , Object object) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.strenght = strenght;
        this.defense = defense;
        this.speed = speed;
        this.type = type;
        this.attacks = attacks;
        this.object = object;
    }

    public int getId() {
        return id;
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

    public PokemonType[] getType() {
        return type;
    }

    public boolean isFainted() {
        return this.hp <= 0;
    }

    public void takeDamage(int damage) {
        this.setHp(this.hp - damage);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, Type: %s)", name, hp, maxHp, type);
    }
    public void damageSuffered(int damage) {
        this.hp -= damage;
    }
}
