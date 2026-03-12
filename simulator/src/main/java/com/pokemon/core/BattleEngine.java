package com.pokemon.core;

import com.pokemon.models.*;
import java.util.Random;

public class BattleEngine {
    private final Random random = new Random();

    public enum Action {
        SWITCH(2), ITEM(2), ATTACK(0);
        private final int p;
        Action(int p) { this.p = p; }
        public int getP() { return p; }
    }

    public boolean isPlayerFirst(Action pA, Pokemon p, Action cA, Pokemon c) {
        if (pA.getP() != cA.getP()) return pA.getP() > cA.getP();
        if (p.getSpeed() != c.getSpeed()) return p.getSpeed() > c.getSpeed();
        return random.nextBoolean();
    }

    public boolean isPlayerFirst(Pokemon p, Pokemon c) {
        return isPlayerFirst(Action.ATTACK, p, Action.ATTACK, c);
    }

    public Attack chooseBestAttack(Pokemon atk, Pokemon tar) {
        Attack[] moves = atk.getAttacks();
        if (moves == null || moves.length == 0) return null;
        Attack best = moves[0];
        double max = -1;
        for (Attack m : moves) {
            if (m == null) continue;
            double d = DamageCalculator.calculateDamage(atk, tar, m);
            if (d > max) { max = d; best = m; }
        }
        return best;
    }

    public int applyDamage(Pokemon a, Pokemon t, Attack m, 
                           javafx.scene.control.TextArea log) {
        int dmg = (int) DamageCalculator.calculateDamage(a, t, m);
        t.takeDamage(dmg);
        if (m.getEffect() != null) {
            m.getEffect().apply(a, t, log);
            m.getEffect().onAfterAttack(a, t, dmg);
        }
        return dmg;
    }
}