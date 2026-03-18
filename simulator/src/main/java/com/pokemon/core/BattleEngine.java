package com.pokemon.core;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;
import com.pokemon.models.Team;
import java.util.Random;

public class BattleEngine {
    private final Random random = new Random();

    public enum ActionType {
        SWITCH(2),
        ITEM(2),
        ATTACK(0);

        private final int priority;

        ActionType(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    public boolean isPlayerFirst(ActionType playerAction, Pokemon player, ActionType cpuAction, Pokemon cpu) {
        if (playerAction.getPriority() > cpuAction.getPriority()) return true;
        if (cpuAction.getPriority() > playerAction.getPriority()) return false;

        if (player.getSpeed() > cpu.getSpeed()) return true;
        if (cpu.getSpeed() > player.getSpeed()) return false;
        return random.nextBoolean();
    }

    public boolean isPlayerFirst(Pokemon player, Pokemon cpu) {
        return isPlayerFirst(ActionType.ATTACK, player, ActionType.ATTACK, cpu);
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


    public int applyDamage(Pokemon attacker, Pokemon target, Attack move, javafx.scene.control.TextArea log) {
    double rawDmg = DamageCalculator.calculateDamage(attacker, target, move);
    int finalDmg = (int) rawDmg;
    target.takeDamage(finalDmg);

    if (move.getEffect() != null) {
        move.getEffect().apply(attacker, target, log);
        
        move.getEffect().onAfterAttack(attacker, target, finalDmg);
    }

    return finalDmg;
}
}