package com.pokemon.models;

public class Attack extends Move {
    private MoveEffect effect;

    // Constructeur harmonisé : Nom, Type, Puissance, Précision, Catégorie
    public Attack(String name, PokemonType type, int power, int accuracy, String category) {
        super(name, power, accuracy, type, category);
    }

    public void execute(Pokemon attacker, Pokemon defender) {
        System.out.println(attacker.getName() + " utilise " + this.getName() + "!");
        
        // Vérification de la précision
        if (Math.random() * 100 > this.getAccuracy()) {
            System.out.println("L'attaque a échoué !");
            return;
        }

        // Calcul des dégâts
        double damage = com.pokemon.core.DamageCalculator.calculateDamage(attacker, defender, this);
        int finalDamage = (int) Math.round(damage);
        
        // Application des dégâts au défenseur (et non à l'attaquant !)
        defender.takeDamage(finalDamage);
        System.out.println(defender.getName() + " a reçu " + finalDamage + " dégâts !");

        // Note : Pense à appeler l'effet ici dans ton BattleEngine si besoin
    }

    public void setEffect(MoveEffect effect) {
        this.effect = effect;
    }

    public MoveEffect getEffect() {
        return effect;
    }
}