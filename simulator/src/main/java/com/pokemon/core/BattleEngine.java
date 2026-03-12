package com.pokemon.core;

import com.pokemon.models.Attack;
import com.pokemon.models.Pokemon;
import com.pokemon.models.Team;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BattleEngine {
    private Team playerTeam;
    private Team cpuTeam;
    private int turnNumber = 1;
    private Random random = new Random();

    // Cette file d'attente stocke les actions du tour pour les jouer une par une
    private Queue<Runnable> eventQueue = new LinkedList<>();

    public BattleEngine(Team playerTeam, Team cpuTeam) {
        this.playerTeam = playerTeam;
        this.cpuTeam = cpuTeam;
    }

    /**
     * Prépare la logique du tour et remplit la file d'attente d'événements.
     */
    public void prepareTurn(Attack playerAction, Pokemon nextPokemon) {
        log("--- TOUR " + turnNumber + " ---");
        
        Pokemon pActive = playerTeam.getActivePokemon();
        Pokemon cActive = cpuTeam.getActivePokemon();
        Attack cpuAction = chooseCpuAttack(cActive);

        // Cas 1 : Le joueur change de Pokemon (Prioritaire)
        if (playerAction == null && nextPokemon != null) {
            eventQueue.add(() -> handleSwitch(playerTeam, nextPokemon));
            eventQueue.add(() -> cpuAction.execute(cActive, playerTeam.getActivePokemon()));
        } 
        // Cas 2 : Combat classique (Vitesse)
        else {
            eventQueue.add(() -> determineOrderAndAttack(pActive, playerAction, cActive, cpuAction));
        }

        // Cas 3 : Fin de tour et vérification
        eventQueue.add(() -> applyEndTurnEffects(playerTeam.getActivePokemon()));
        eventQueue.add(() -> applyEndTurnEffects(cpuTeam.getActivePokemon()));
        eventQueue.add(() -> {
            checkBattleStatus();
            turnNumber++;
        });
    }


    public boolean hasPendingAction() {
        return !eventQueue.isEmpty();
    }

    public void executeNextStep() {
        Runnable action = eventQueue.poll();
        if (action != null) {
            action.run();
        }
    }

    private void determineOrderAndAttack(Pokemon p, Attack pAtk, Pokemon c, Attack cAtk) {
        boolean playerFirst = p.getSpeed() > c.getSpeed();
        if (p.getSpeed() == c.getSpeed()) playerFirst = random.nextBoolean();

        if (playerFirst) {
            pAtk.execute(p, c);
            if (!c.isFainted()) cAtk.execute(c, p);
        } else {
            cAtk.execute(c, p);
            if (!p.isFainted()) pAtk.execute(p, c);
        }
    }

    private void handleSwitch(Team team, Pokemon next) {
        log("Changement : " + next.getName() + " entre au combat !");
        for (int i = 0; i < team.getPokemons().length; i++) {
            if (team.getPokemons()[i] == next) {
                team.setActivePokemonIndex(i);
                break;
            }
        }
    }

    private Attack chooseCpuAttack(Pokemon cpuPkmn) {
        Attack[] moves = cpuPkmn.getAttacks();
        return moves[random.nextInt(moves.length)];
    }

    private void applyEndTurnEffects(Pokemon p) {
        if (p != null && !p.isFainted() && p.getItem() != null) {
            p.getItem().onTurnEnd(p);
        }
    }

    private void checkBattleStatus() {
        if (!playerTeam.hasAvailablePokemon()) {
            log("GAME OVER - L'ordinateur a gagné !");
        } else if (!cpuTeam.hasAvailablePokemon()) {
            log("VICTOIRE - Vous avez battu l'ordinateur !");
        }
    }

    private void log(String msg) {
        System.out.println(msg);
        
    }
}