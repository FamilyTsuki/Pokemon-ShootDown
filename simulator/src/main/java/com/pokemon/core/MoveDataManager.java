package com.pokemon.core;

import com.pokemon.models.Attack;
import com.pokemon.models.PokemonType;
import com.pokemon.effect.*; // Importation de ton package d'effets
import java.util.HashMap;
import java.util.Map;

public class MoveDataManager {

    // On crée un catalogue d'effets pour éviter les "if/else"
    private static final Map<String, Effecte> effectRegistry = new HashMap<>();

    static {
    
        effectRegistry.put("HEAL_USER", new LifeStealEffect());
        effectRegistry.put("BURN", new BurnEffect());
        effectRegistry.put("DAMOCLES", new Damocles());
        effectRegistry.put("BOOST_DEF", new BoostDefEffect());
       
    }

    public static Attack createAttackFromData(String[] d) {
        try {
            String name = d[0].trim();
            PokemonType type = PokemonType.valueOf(d[1].trim().toUpperCase());
            int power = Integer.parseInt(d[2].trim());
            int acc = Integer.parseInt(d[3].trim());
            String category = d[4].trim();
            String effectId = (d.length > 5) ? d[5].trim().toUpperCase() : "NONE";

            Attack atk = new Attack(name, type, power, acc, category);

            Effecte effect = effectRegistry.get(effectId);
            
            if (effect != null) {
                atk.setEffect(effect);
            }

            return atk;

        } catch (Exception e) {
            System.err.println("Erreur création attaque CSV : " + e.getMessage());
            return null;
        }
    }
}