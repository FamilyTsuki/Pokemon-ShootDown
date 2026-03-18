package com.pokemon.items.items;

import com.pokemon.models.Attack;
import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;

public class FocusSash extends  Item{
    public FocusSash() {
        super("FocusSash", "l'objet empeche le pokemon de mourir en le laissant a 1 HP");
    }
    private boolean active = true;

    public boolean useCondition(Pokemon pokemon) {

        if (active && pokemon.getHp() < 0) {
            return true;
        }

        return false;
    }

    @Override
    public Item use(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " récupere 20 HP grace au berryjuice");
        return null;
    }


    @Override
    public void onTurnStart(Pokemon pokemon) {}
    @Override
    public void onTurnEnd(Pokemon pokemon) {}
    @Override
    public void onAttack(Pokemon attacker, Pokemon target, Attack attack) {}
    @Override
    public void onReceiveDamage(Pokemon pokemon, Attack attack, int damage) {}
    public void onPokemondie(Pokemon pokemon) {
        if (useCondition(pokemon)) {
            pokemon.hpmodifier(1);
            active = false;
        }
    }
}
