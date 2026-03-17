package com.pokemon.core;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;
import com.pokemon.models.Team;
import java.util.Random;

public class BattleEngine {
    private final Random random = new Random();

    public boolean isPlayerFirst(Pokemon player, Pokemon cpu) {
        if (player.getSpeed() > cpu.getSpeed()) return true;
        if (cpu.getSpeed() > player.getSpeed()) return false;
        return random.nextBoolean();
    }


    public Attack chooseBestAttack(Pokemon attacker, Pokemon target) {
        Attack[] moves = attacker.getAttacks();
        if (moves == null || moves.length == 0) return null;

        Attack bestMove = moves[0];
        double maxDmg = -1;

        for (Attack atk : moves) {
            if (atk == null) continue;
            double theoreticalDmg = DamageCalculator.calculateDamage(attacker, target, atk);
            if (theoreticalDmg > maxDmg) {
                maxDmg = theoreticalDmg;
                bestMove = atk;
            }
        }
        return bestMove;
    }


    public int applyDamage(Pokemon attacker, Pokemon target, Attack move) {
        double rawDmg = DamageCalculator.calculateDamage(attacker, target, move);
        int finalDmg = (int) rawDmg;
        target.takeDamage(finalDmg);
        return finalDmg;
    }
}