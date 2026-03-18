package com.pokemon.models.items;

import com.pokemon.models.Attack;
import com.pokemon.models.Item;
import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;

public class BerryJuice extends Item {
    public BerryJuice() {
        super("BerruJuice", "restaure 20h quand la vie déssend en dessous de 50%");
    }
    private boolean active = true;

    public boolean useCondition(Pokemon pokemon) {

        if (active && pokemon.getHp() <= (pokemon.getMaxHp() / 2)) {
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
    public void onTurnEnd(Pokemon pokemon) {
        if (useCondition(pokemon)) {
            pokemon.hpmodifier(20);
            active = false;
        }
    }
    @Override
    public void onAttack(Pokemon attacker, Pokemon target, Attack attack) {}

    @Override
    public void onReceiveDamage(Pokemon pokemon, Attack attack, int damage) {}
}
