package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;
import com.pokemon.core.DamageCalculator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BattleController {

    @FXML private Label playerNameLabel, cpuNameLabel, playerHPText;
    @FXML private ProgressBar playerHPBar, cpuHPBar;
    @FXML private TextArea battleLog;
    @FXML private Button move1, move2, move3, move4;

    private Pokemon activePlayerPokemon;
    private Pokemon activeCpuPokemon;

    public void setupBattle(Pokemon player, Pokemon cpu) {
        this.activePlayerPokemon = player;
        this.activeCpuPokemon = cpu;
        updateUI();
        battleLog.appendText("Un combat commence entre " + player.getName() + " et " + cpu.getName() + " !\n");
    }

    @FXML
    private void handleMove(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();

        executeTurn();
    }

    private void executeTurn() {

        double damage = DamageCalculator.calculateDamage(activePlayerPokemon, activeCpuPokemon, activePlayerPokemon.getAttacks()[0]);
        activeCpuPokemon.takeDamage((int)damage); 

        battleLog.appendText(activePlayerPokemon.getName() + " inflige " + (int)damage + " dégâts.\n");

        if (activeCpuPokemon.isFainted()) {
            battleLog.appendText(activeCpuPokemon.getName() + " est KO !\n");
        }

        updateUI();
    }

    private void updateUI() {

        playerHPBar.setProgress((double) activePlayerPokemon.getHp() / activePlayerPokemon.getMaxHp());
        cpuHPBar.setProgress((double) activeCpuPokemon.getHp() / activeCpuPokemon.getMaxHp());
        playerHPText.setText(activePlayerPokemon.getHp() + " / " + activePlayerPokemon.getMaxHp());
    }
}