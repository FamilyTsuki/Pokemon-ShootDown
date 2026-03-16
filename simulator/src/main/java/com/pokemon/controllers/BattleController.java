package com.pokemon.controllers;

import com.pokemon.models.*;
import com.pokemon.core.BattleEngine;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BattleController {

    @FXML private Label playerNameLabel, cpuNameLabel, playerHPText, cpuHPText;
    @FXML private ProgressBar playerHPBar, cpuHPBar;
    @FXML private ImageView playerSprite, cpuSprite;
    @FXML private TextArea battleLog;
    @FXML private Button move1, move2, move3, move4;
    @FXML private VBox switchMenu;
    @FXML private HBox cpuTeamStatus;
    @FXML private Button switchBtn0, switchBtn1, switchBtn2, switchBtn3, switchBtn4, switchBtn5;
    @FXML private Button toggleLogBtn;
    @FXML private HBox logPane;
    @FXML private StackPane battleStackPane; 
    @FXML private VBox mainBattleArea; 

    private Team playerTeam, cpuTeam;
    private Pokemon activePlayer, activeCpu;
    private final BattleEngine engine = new BattleEngine();
    private boolean isSwitchMenuVisible = false;
    private boolean isLogVisible = false;
    private int turnCount = 1;
    

    public void setupBattle(Team playerTeam, Team cpuTeam) {
        this.playerTeam = playerTeam;
        this.cpuTeam = cpuTeam;
        updateActivePokemons();
        
        loadSprites();
        refreshUI();
        disableUI(false); 
        
        battleLog.appendText("Un combat commence !\n");
    }

    private void updateActivePokemons() {
        this.activePlayer = playerTeam.getActivePokemon();
        this.activeCpu = cpuTeam.getActivePokemon();
    }

    @FXML
    private void handleMove(ActionEvent event) {
        int moveIdx = getButtonIndex((Button) event.getSource());
        Attack playerAtk = activePlayer.getAttacks()[moveIdx];

        if (playerAtk != null) {
            disableUI(true); 
            Attack cpuAtk = engine.chooseBestAttack(activeCpu, activePlayer);

            if (engine.isPlayerFirst(activePlayer, activeCpu)) {
                executeTurnSequence(activePlayer, activeCpu, playerAtk, cpuAtk);
            } else {
                executeTurnSequence(activeCpu, activePlayer, cpuAtk, playerAtk);
            }
        }
    }

    private void executeTurnSequence(Pokemon first, Pokemon second, Attack atk1, Attack atk2) {
    battleLog.appendText("\n=== TOUR " + turnCount + " ====\n");
    
    battleLog.appendText("[INFO] " + first.getName() + " est plus rapide (Vitesse: " + first.getSpeed() + ")\n");

    processAttack(first, second, atk1);
    
    if (second.isFainted()) {
        turnCount++;
        PauseTransition shortPause = new PauseTransition(Duration.seconds(1.0));
        shortPause.setOnFinished(e -> {
            if (activePlayer.isFainted()) disableUI(false); 
            checkBattleStatus();
        });
        shortPause.play();
        return;
    }

    PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
    delay.setOnFinished(e -> {
        processAttack(second, first, atk2);
        
        PauseTransition endDelay = new PauseTransition(Duration.seconds(1.0));
        endDelay.setOnFinished(ev -> {
            turnCount++;
            if (activePlayer.isFainted()) disableUI(false); 
            checkBattleStatus(); 
            if (!activePlayer.isFainted() && !activeCpu.isFainted()) {
                disableUI(false);
            }
        });
        endDelay.play();
    });
    delay.play();
}

    private void processAttack(Pokemon attacker, Pokemon target, Attack move) {
    
    int damage = engine.applyDamage(attacker, target, move);
    
    String efficacite = ""; 
    if (damage > 20) efficacite = " (C'est efficace !)";
    else if (damage < 10 && damage > 0) efficacite = " (Ce n'est pas très efficace...)";

    battleLog.appendText("• " + attacker.getName() + " utilise " + move.getName() + efficacite + "\n");
    battleLog.appendText("   -> " + target.getName() + " perd " + damage + " PV.\n");

    if (target.isFainted()) {
        battleLog.appendText("   -> " + target.getName() + " est KO !\n");
    }

    refreshUI();
}

    private void checkBattleStatus() {
        if (activeCpu.isFainted()) {
            Pokemon next = cpuTeam.getNextAvailablePokemon();
            if (next != null) {
                activeCpu = next;
                cpuTeam.setActivePokemon(next);
                battleLog.appendText("L'adversaire envoie " + next.getName() + " !\n");
                loadSprites();
                refreshUI();
                disableUI(false);
            } else {
                showEndGameMessage("VICTOIRE !", Color.GREEN);
            }
        } 
        else if (activePlayer.isFainted()) {
            if (playerTeam.hasAvailablePokemon()) {
                battleLog.appendText("Votre Pokémon est KO ! Switch obligatoire.\n");
                handleSwitch(null); 
            } else {
                showEndGameMessage("DÉFAITE...", Color.RED);
            }
        }
    }

    private void showEndGameMessage(String message, Color color) {
        battleLog.appendText(message + "\n");
        disableUI(true);
        mainBattleArea.setVisible(false);

        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1.0); 
        mainBattleArea.setEffect(new GaussianBlur(10));

        Text endText = new Text(message);
        endText.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
        endText.setFill(color);
        endText.setStroke(Color.BLACK);
        endText.setStrokeWidth(3);

        battleStackPane.getChildren().add(endText);
    }

    private void refreshUI() {
    
    playerHPBar.setProgress((double) activePlayer.getHp() / activePlayer.getMaxHp());
    playerHPText.setText(activePlayer.getHp() + " / " + activePlayer.getMaxHp());
    cpuHPBar.setProgress((double) activeCpu.getHp() / activeCpu.getMaxHp());
    cpuHPText.setText(activeCpu.getHp() + " / " + activeCpu.getMaxHp());
    
    playerNameLabel.setText(activePlayer.getName().toUpperCase());
    cpuNameLabel.setText(activeCpu.getName().toUpperCase());

    updateCpuIcons();

    Button[] btns = {move1, move2, move3, move4};
    Attack[] atks = activePlayer.getAttacks();

    boolean alive = !activePlayer.isFainted();

    for (int i = 0; i < 4; i++) {
        if (alive && atks != null && i < atks.length && atks[i] != null) {
            btns[i].setText(atks[i].getName());
            btns[i].setVisible(true);
            btns[i].setManaged(true);
        } else {
            btns[i].setVisible(false);
            btns[i].setManaged(false);
        }
    }
}

    @FXML
    private void handleSwitch(ActionEvent event) {
        disableUI(false);
        if (isSwitchMenuVisible && event != null) { closeSwitchMenu(); return; }
        
        Button[] btns = {switchBtn0, switchBtn1, switchBtn2, switchBtn3, switchBtn4, switchBtn5};
        Pokemon[] pokemons = playerTeam.getPokemons();

        for (int i = 0; i < 6; i++) {
            if (i >= pokemons.length || pokemons[i] == null) {
                btns[i].setText("❌"); btns[i].setDisable(true);
            } else {
                btns[i].setText(pokemons[i].getName());
                btns[i].setDisable(pokemons[i].isFainted() || pokemons[i] == activePlayer);
                btns[i].setUserData(i);
            }
        }
        openSwitchMenu();
    }

    @FXML
    private void handleSwitchConfirmation(ActionEvent event) {
        int index = (int) ((Button)event.getSource()).getUserData();
        activePlayer = playerTeam.getPokemons()[index];
        playerTeam.setActivePokemon(activePlayer);
        
        battleLog.appendText("Switch : " + activePlayer.getName() + " entre en combat !\n");
        loadSprites();
        closeSwitchMenu();
        refreshUI();
        disableUI(false); 
    }

    private void updateCpuIcons() {
        cpuTeamStatus.getChildren().clear();
        for (Pokemon p : cpuTeam.getPokemons()) {
            if (p == null) continue;
            try {
                String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
                var res = getClass().getResourceAsStream(path);
                if (res != null) {
                    ImageView icon = new ImageView(new Image(res));
                    icon.setFitWidth(30); icon.setFitHeight(30);
                    if (p.isFainted()) {
                        ColorAdjust bw = new ColorAdjust(); bw.setSaturation(-1.0);
                        icon.setEffect(bw);
                    }
                    cpuTeamStatus.getChildren().add(icon);
                }
            } catch (Exception e) {}
        }
    }

    private void disableUI(boolean state) {
        move1.setDisable(state); move2.setDisable(state);
        move3.setDisable(state); move4.setDisable(state);
    }

    private void loadSprites() {
        try {
            playerSprite.setImage(new Image(getClass().getResourceAsStream("/com/pokemon/assets/sprites/" + activePlayer.getId() + ".png")));
            cpuSprite.setImage(new Image(getClass().getResourceAsStream("/com/pokemon/assets/sprites/" + activeCpu.getId() + ".png")));
        } catch (Exception e) {}
    }

    private void openSwitchMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
        tt.setToY(0); tt.play();
        isSwitchMenuVisible = true;
    }

    @FXML private void closeSwitchMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
        tt.setToY(170); tt.play();
        isSwitchMenuVisible = false;
    }

    @FXML private void toggleLog() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), logPane);
        tt.setToX(isLogVisible ? 300 : 0);
        toggleLogBtn.setText(isLogVisible ? "◀" : "▶");
        isLogVisible = !isLogVisible;
        tt.play();
    }

    private int getButtonIndex(Button btn) {
        if (btn == move1) return 0;
        if (btn == move2) return 1;
        if (btn == move3) return 2;
        return 3;
    }
    
    @FXML private void handleItems(ActionEvent event) { battleLog.appendText("Sac vide !\n"); }
}