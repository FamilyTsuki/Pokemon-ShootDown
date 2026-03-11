package com.pokemon.core;

import com.pokemon.models.PokemonType;
import java.util.HashMap;
import java.util.Map;

public class TypeRepository {
    private static final Map<String, PokemonType> types = new HashMap<>();

    public static void init() {
       
        PokemonType normal = new PokemonType("Normal");
        PokemonType fire = new PokemonType("Fire");
        PokemonType water = new PokemonType("Water");
        PokemonType grass = new PokemonType("Grass");
        PokemonType electric = new PokemonType("Electric");
        PokemonType ice = new PokemonType("Ice");
        PokemonType fighting = new PokemonType("Fighting");
        PokemonType poison = new PokemonType("Poison");
        PokemonType ground = new PokemonType("Ground");
        PokemonType flying = new PokemonType("Flying");
        PokemonType psychic = new PokemonType("Psychic");
        PokemonType bug = new PokemonType("Bug");
        PokemonType rock = new PokemonType("Rock");
        PokemonType ghost = new PokemonType("Ghost");
        PokemonType dragon = new PokemonType("Dragon");
        PokemonType dark = new PokemonType("Dark");
        PokemonType steel = new PokemonType("Steel");
        PokemonType fairy = new PokemonType("Fairy");


        normal.addEfficiency(rock, 0.5);
        normal.addEfficiency(steel, 0.5);
        normal.addEfficiency(ghost, 0.0);

        fire.addEfficiency(grass, 2.0);
        fire.addEfficiency(ice, 2.0);
        fire.addEfficiency(bug, 2.0);
        fire.addEfficiency(steel, 2.0);
        fire.addEfficiency(fire, 0.5);
        fire.addEfficiency(water, 0.5);
        fire.addEfficiency(rock, 0.5);
        fire.addEfficiency(dragon, 0.5);

        water.addEfficiency(fire, 2.0);
        water.addEfficiency(ground, 2.0);
        water.addEfficiency(rock, 2.0);
        water.addEfficiency(water, 0.5);
        water.addEfficiency(grass, 0.5);
        water.addEfficiency(dragon, 0.5);

        grass.addEfficiency(water, 2.0);
        grass.addEfficiency(ground, 2.0);
        grass.addEfficiency(rock, 2.0);
        grass.addEfficiency(fire, 0.5);
        grass.addEfficiency(grass, 0.5);
        grass.addEfficiency(poison, 0.5);
        grass.addEfficiency(flying, 0.5);
        grass.addEfficiency(bug, 0.5);
        grass.addEfficiency(dragon, 0.5);
        grass.addEfficiency(steel, 0.5);

        electric.addEfficiency(water, 2.0);
        electric.addEfficiency(flying, 2.0);
        electric.addEfficiency(electric, 0.5);
        electric.addEfficiency(grass, 0.5);
        electric.addEfficiency(dragon, 0.5);
        electric.addEfficiency(ground, 0.0);

        ice.addEfficiency(grass, 2.0);
        ice.addEfficiency(ground, 2.0);
        ice.addEfficiency(flying, 2.0);
        ice.addEfficiency(dragon, 2.0);
        ice.addEfficiency(fire, 0.5);
        ice.addEfficiency(water, 0.5);
        ice.addEfficiency(ice, 0.5);
        ice.addEfficiency(steel, 0.5);

        fighting.addEfficiency(normal, 2.0);
        fighting.addEfficiency(ice, 2.0);
        fighting.addEfficiency(rock, 2.0);
        fighting.addEfficiency(dark, 2.0);
        fighting.addEfficiency(steel, 2.0);
        fighting.addEfficiency(poison, 0.5);
        fighting.addEfficiency(flying, 0.5);
        fighting.addEfficiency(psychic, 0.5);
        fighting.addEfficiency(bug, 0.5);
        fighting.addEfficiency(fairy, 0.5);
        fighting.addEfficiency(ghost, 0.0);

        poison.addEfficiency(grass, 2.0);
        poison.addEfficiency(fairy, 2.0);
        poison.addEfficiency(poison, 0.5);
        poison.addEfficiency(ground, 0.5);
        poison.addEfficiency(rock, 0.5);
        poison.addEfficiency(ghost, 0.5);
        poison.addEfficiency(steel, 0.0);

        ground.addEfficiency(fire, 2.0);
        ground.addEfficiency(electric, 2.0);
        ground.addEfficiency(poison, 2.0);
        ground.addEfficiency(rock, 2.0);
        ground.addEfficiency(steel, 2.0);
        ground.addEfficiency(grass, 0.5);
        ground.addEfficiency(bug, 0.5);
        ground.addEfficiency(flying, 0.0);

        flying.addEfficiency(grass, 2.0);
        flying.addEfficiency(fighting, 2.0);
        flying.addEfficiency(bug, 2.0);
        flying.addEfficiency(electric, 0.5);
        flying.addEfficiency(rock, 0.5);
        flying.addEfficiency(steel, 0.5);

        psychic.addEfficiency(fighting, 2.0);
        psychic.addEfficiency(poison, 2.0);
        psychic.addEfficiency(psychic, 0.5);
        psychic.addEfficiency(steel, 0.5);
        psychic.addEfficiency(dark, 0.0);

        bug.addEfficiency(grass, 2.0);
        bug.addEfficiency(psychic, 2.0);
        bug.addEfficiency(dark, 2.0);
        bug.addEfficiency(fire, 0.5);
        bug.addEfficiency(fighting, 0.5);
        bug.addEfficiency(poison, 0.5);
        bug.addEfficiency(flying, 0.5);
        bug.addEfficiency(ghost, 0.5);
        bug.addEfficiency(steel, 0.5);
        bug.addEfficiency(fairy, 0.5);

        rock.addEfficiency(fire, 2.0);
        rock.addEfficiency(ice, 2.0);
        rock.addEfficiency(flying, 2.0);
        rock.addEfficiency(bug, 2.0);
        rock.addEfficiency(fighting, 0.5);
        rock.addEfficiency(ground, 0.5);
        rock.addEfficiency(steel, 0.5);

        ghost.addEfficiency(psychic, 2.0);
        ghost.addEfficiency(ghost, 2.0);
        ghost.addEfficiency(dark, 0.5);
        ghost.addEfficiency(normal, 0.0);

        dragon.addEfficiency(dragon, 2.0);
        dragon.addEfficiency(steel, 0.5);
        dragon.addEfficiency(fairy, 0.0);

        dark.addEfficiency(psychic, 2.0);
        dark.addEfficiency(ghost, 2.0);
        dark.addEfficiency(fighting, 0.5);
        dark.addEfficiency(dark, 0.5);
        dark.addEfficiency(fairy, 0.5);

        steel.addEfficiency(ice, 2.0);
        steel.addEfficiency(rock, 2.0);
        steel.addEfficiency(fairy, 2.0);
        steel.addEfficiency(fire, 0.5);
        steel.addEfficiency(water, 0.5);
        steel.addEfficiency(electric, 0.5);
        steel.addEfficiency(steel, 0.5);

        fairy.addEfficiency(fighting, 2.0);
        fairy.addEfficiency(dragon, 2.0);
        fairy.addEfficiency(dark, 2.0);
        fairy.addEfficiency(fire, 0.5);
        fairy.addEfficiency(poison, 0.5);
        fairy.addEfficiency(steel, 0.5);

        register(normal); register(fire); register(water); register(grass);
        register(electric); register(ice); register(fighting); register(poison);
        register(ground); register(flying); register(psychic); register(bug);
        register(rock); register(ghost); register(dragon); register(dark);
        register(steel); register(fairy);
    }

    private static void register(PokemonType type) {
        types.put(type.toString().toLowerCase(), type);
    }

    public static PokemonType get(String nom) {
        return types.get(nom.toLowerCase());
    }
}