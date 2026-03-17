package com.pokemon.core;

import com.pokemon.models.Attack;
import com.pokemon.models.PokemonType;

public class MoveDataManager {

    public static Attack createAttackFromData(String[] d) {
        try {

            String name = d[0].trim();
            PokemonType type = PokemonType.valueOf(d[1].trim().toUpperCase());
            int power = Integer.parseInt(d[2].trim());
            int acc = Integer.parseInt(d[3].trim());
            String category = d[4].trim();
            String effectId = (d.length > 5) ? d[5].trim() : "NONE";

            Attack atk = new Attack(name, type, power, acc, category);

            if (effectId.equalsIgnoreCase("HEAL_USER")) {
                atk.setEffect((user, target, log) -> {
                    int heal = user.getMaxHp() / 2;
                    user.setHp(Math.min(user.getMaxHp(), user.getHp() + heal));
                    if (log != null) log.appendText(user.getName() + " se soigne !\n");
                });
            } else if (effectId.equalsIgnoreCase("BOOST_ATK")) {
                atk.setEffect((user, target, log) -> {
                    user.setAttack(user.getAttack() + 10);
                    if (log != null) log.appendText("L'attaque de " + user.getName() + " augmente !\n");
                });
            }

            return atk;
        } catch (Exception e) {
            System.err.println("Erreur création attaque CSV : " + e.getMessage());
            return null;
        }
    }
}