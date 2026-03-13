package com.pokemon.controllers;

import com.pokemon.models.Pokemon;
import com.pokemon.models.Attack;
import com.pokemon.core.DamageCalculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class BattleController {

    @FXML private Label playerNameLabel, cpuNameLabel, playerHPText, cpuHPText;
    @FXML private ProgressBar playerHPBar, cpuHPBar;
    @FXML private TextArea battleLog;
    @FXML private Button move1, move2, move3, move4;
    @FXML private HBox logPane;
    @FXML private Button toggleLogBtn;
    private boolean isLogVisible = false;

    private Pokemon activePlayerPokemon;
    private Pokemon activeCpuPokemon;

    public void setupBattle(Pokemon player, Pokemon cpu) {
    this.activePlayerPokemon = player;
    this.activeCpuPokemon = cpu;
    this.playerNameLabel.setText(player.getName());
    this.cpuNameLabel.setText(cpu.getName());
    this.playerHPText.setText(player.getHp() + " / " + player.getMaxHp());
    this.playerHPBar.setProgress((double) player.getHp() / player.getMaxHp());
    
    this.cpuHPBar.setProgress((double) cpu.getHp() / cpu.getMaxHp());
    this.cpuHPText.setText(cpu.getHp() + " / " + cpu.getMaxHp());

    Button[] moveButtons = {move1, move2, move3, move4};
    Attack[] attacks = player.getAttacks();

    for (int i = 0; i < moveButtons.length; i++) {
        if (i < attacks.length && attacks[i] != null) {
            moveButtons[i].setText(attacks[i].getName());
            moveButtons[i].setVisible(true);
            moveButtons[i].setDisable(false);
        } else {
            moveButtons[i].setVisible(false);
            moveButtons[i].setDisable(true);
        }
    }

    updateUI();
    battleLog.appendText("Un combat commence entre " + player.getName() + " et " + cpu.getName() + " !\n");
}

    @FXML
private void handleMove(ActionEvent event) {
    Button clickedButton = (Button) event.getSource();
    Attack selectedAttack = null;

    if (clickedButton == move1) selectedAttack = activePlayerPokemon.getAttacks()[0];
    else if (clickedButton == move2) selectedAttack = activePlayerPokemon.getAttacks()[1];
    else if (clickedButton == move3) selectedAttack = activePlayerPokemon.getAttacks()[2];
    else if (clickedButton == move4) selectedAttack = activePlayerPokemon.getAttacks()[3];

    if (selectedAttack != null) {
        executeTurn(selectedAttack);
    }
}



    private void executeTurn(Attack attack) {

        double damage = DamageCalculator.calculateDamage(activePlayerPokemon, activeCpuPokemon, attack);
        activeCpuPokemon.takeDamage((int)damage); 

        battleLog.appendText(activePlayerPokemon.getName() + " utilise " + attack.getName() + " !\n");
        battleLog.appendText("Il inflige " + (int)damage + " dégâts.\n");
        if (activeCpuPokemon.isFainted()) {
            battleLog.appendText(activeCpuPokemon.getName() + " est KO !\n");
        }

        updateUI();
    }

    private void updateUI() {

        playerHPBar.setProgress((double) activePlayerPokemon.getHp() / activePlayerPokemon.getMaxHp());
        cpuHPBar.setProgress((double) activeCpuPokemon.getHp() / activeCpuPokemon.getMaxHp());
        playerHPText.setText(activePlayerPokemon.getHp() + " / " + activePlayerPokemon.getMaxHp());
        cpuHPText.setText(activeCpuPokemon.getHp() + " / " + activeCpuPokemon.getMaxHp());
    }
    @FXML
private void handleSwitch(ActionEvent event) {

    battleLog.appendText("Changement de Pokémon demandé...\n");
}

@FXML
private void handleMega(ActionEvent event) {

    battleLog.appendText("Tentative de Méga-Évolution !\n");
}

@FXML
private void handleItems(ActionEvent event) {

    battleLog.appendText("Ouverture du sac...\n");
}
@FXML
private void toggleLog() {
    TranslateTransition transition = new TranslateTransition(Duration.millis(300), logPane);
    
    if (isLogVisible) {
        transition.setToX(300); 
        toggleLogBtn.setText("◀");
    } else {
        transition.setToX(0);
        toggleLogBtn.setText("▶");
    }
    
    isLogVisible = !isLogVisible;
    transition.play();
}
}