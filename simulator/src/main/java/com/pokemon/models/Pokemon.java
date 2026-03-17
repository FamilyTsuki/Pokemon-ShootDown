package com.pokemon.models;

public class Pokemon {
    private int id;
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int speed;
    private PokemonType[] type;
    private Attack[] attacks;
    private Attack[] learble;
    private Item item;
    private int spAttack;
    private int spDefense;
    private Status currentStatus;


    public Pokemon(int id, String name, int hp, int attack, int defense, int speed, PokemonType[] type, Attack[] attacks , Item item, Attack[] learble, int spAttack, int spDefense) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.type = type;
        this.attacks = attacks;
        this.item = item;
        this.learble = learble;
        this.spAttack = spAttack;
        this.spDefense = spDefense;


    }

    public Attack[] getLearble() {
        return learble;
    }
    public void setAttack(Attack attack) {
        if (this.attacks.length < 4) {
            this.attacks = new Attack[this.attacks.length +1];
            this.attacks[this.attacks.length -1] = attack;
        }
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

    public void hpmodifier(int hp) {
        this.hp += hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speedadd) {this.speed += speedadd;}

    public PokemonType[] getTypes() {
        return type;
    }

    public boolean isFainted() {
        return this.hp <= 0;
    }

    public void takeDamage(int damage) {
        this.setHp(this.hp - damage);
    }

    public Item getItem() {
        if (item == null) return null;
        return item;
    }

    public int getSpAttack() {
        return spAttack;
    }

    public int getSpDefense() {
        return spDefense;
    }
    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setSpAttack(int spAttack) {
        this.spAttack = spAttack;
    }

    public void setSpDefense(int spDefense) {
        this.spDefense = spDefense;
    }
    
    public Status getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(Status status) { this.currentStatus = status; }

    public void setBaseAttack(int newAttack) {
        this.attack = newAttack;
    }

    private boolean canAttack = true; 

    public boolean canAttack() { return canAttack; }
    public void setCanAttack(boolean canAttack) { this.canAttack = canAttack; }

    public void setBaseSpeed(int newSpeed) { this.speed = newSpeed; }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, Type: %s)", name, hp, maxHp, type);
    }
}
