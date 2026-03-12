package com.pokemon.core;

import com.pokemon.models.*;
import com.pokemon.effect.*;
import java.util.*;

public class MoveDataManager {
    private static final Map<String, Effect> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("HEAL_USER", new LifeStealEffect());
        REGISTRY.put("BURN", new BurnEffect());
        REGISTRY.put("DAMOCLES", new Damocles());
        REGISTRY.put("BOOST_DEF", new BoostDefEffect());
    }

    public static Attack createAttackFromData(String[] d) {
        try {
            String name = d[0].trim();
            PokemonType type = PokemonType.valueOf(d[1].trim().toUpperCase());
            int pwr = Integer.parseInt(d[2].trim());
            int acc = Integer.parseInt(d[3].trim());
            String cat = d[4].trim();
            
            Attack atk = new Attack(name, type, pwr, acc, cat);
            String eId = (d.length > 5) ? d[5].trim().toUpperCase() : "NONE";
            
            if (REGISTRY.containsKey(eId)) atk.setEffect(REGISTRY.get(eId));
            return atk;
        } catch (Exception e) {
            System.err.println("CSV Attack Error: " + e.getMessage());
            return null;
        }
    }
}