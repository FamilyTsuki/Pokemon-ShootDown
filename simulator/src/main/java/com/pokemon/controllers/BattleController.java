package com.pokemon.controllers;

import com.pokemon.models.*;
import com.pokemon.core.BattleEngine;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    @FXML private HBox playerTypeContainer;
    @FXML private HBox cpuTypeContainer;

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
    
    if (this.activePlayer == null) {
        System.err.println("ERREUR : Aucun Pokémon actif trouvé pour le joueur !");
        return;
    }

    loadSprites();
    refreshUI();
    
    disableUI(false); 
    
    battleLog.appendText("Un combat commence !\n");
}

   private void updateActivePokemons() {
    this.activePlayer = playerTeam.getActivePokemon();
    this.activeCpu = cpuTeam.getActivePokemon();

    if (this.activePlayer == null) {
        for (Pokemon p : playerTeam.getPokemons()) {
            if (p != null) {
                this.activePlayer = p;
                playerTeam.setActivePokemon(p);
                break;
            }
        }
    }

    if (this.activeCpu == null) {
        for (Pokemon p : cpuTeam.getPokemons()) {
            if (p != null) {
                this.activeCpu = p;
                cpuTeam.setActivePokemon(p);
                break;
            }
        }
    }
}


    @FXML
private void handleMove(ActionEvent event) {
    // 1. BLOQUER L'INTERFACE IMMÉDIATEMENT
    disableUI(true); 

    int moveIdx = getButtonIndex((Button) event.getSource());
    Attack playerAtk = activePlayer.getAttacks()[moveIdx];

    if (playerAtk != null) {
        Attack cpuAtk = engine.chooseBestAttack(activeCpu, activePlayer);

        if (engine.isPlayerFirst(activePlayer, activeCpu)) {
            executeTurnSequence(activePlayer, activeCpu, playerAtk, cpuAtk);
        } else {
            executeTurnSequence(activeCpu, activePlayer, cpuAtk, playerAtk);
        }
    } else {
        // Si l'attaque est nulle pour une raison X, on débloque
        disableUI(false);
    }
}

private void executeTurnSequence(Pokemon first, Pokemon second, Attack atk1, Attack atk2) {
    battleLog.appendText("\n=== TOUR " + turnCount + " ====\n");
    
    // Premier attaquant
    processAttack(first, second, atk1);
    
    if (second.isFainted()) {
        turnCount++;
        PauseTransition shortPause = new PauseTransition(Duration.seconds(1.0));
        shortPause.setOnFinished(e -> {
            checkBattleStatus(); // Ici, checkBattleStatus gérera le déblocage si besoin
        });
        shortPause.play();
        return;
    }

    // Deuxième attaquant (après un délai)
    PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
    delay.setOnFinished(e -> {
        processAttack(second, first, atk2);
        
        PauseTransition endDelay = new PauseTransition(Duration.seconds(1.0));
        endDelay.setOnFinished(ev -> {
            turnCount++;
            checkBattleStatus(); 
            
            // ON NE DÉBLOQUE QUE SI LE COMBAT CONTINUE ET QUE LE JOUEUR N'EST PAS KO
            if (!activePlayer.isFainted() && !activeCpu.isFainted()) {
                disableUI(false);
            }
        });
        endDelay.play();
    });
    delay.play();
}

    private void processAttack(Pokemon attacker, Pokemon target, Attack move) {

    boolean isPlayerAttacking = (attacker == activePlayer);
    ImageView attackerSprite = isPlayerAttacking ? playerSprite : cpuSprite;
    ImageView targetSprite = isPlayerAttacking ? cpuSprite : playerSprite;

    animateAttack(attackerSprite, isPlayerAttacking);

    int damage = engine.applyDamage(attacker, target, move);
    
    if (damage > 0) {
        animateDamage(targetSprite);
        showDamagePopup(targetSprite, damage);
    }

    String efficacite = ""; 
    if (damage > 30) efficacite = " (C'est super efficace !)";
    else if (damage < 10 && damage > 0) efficacite = " (Ce n'est pas très efficace...)";

    battleLog.appendText("• " + attacker.getName() + " utilise " + move.getName() + efficacite + "\n");
    battleLog.appendText("   -> " + target.getName() + " perd " + damage + " PV.\n");

    if (target.isFainted()) {
        battleLog.appendText("   -> " + target.getName() + " est KO !\n");

        FadeTransition koFade = new FadeTransition(Duration.millis(500), targetSprite);
        koFade.setToValue(0);
        koFade.play();
    }

    refreshUI();
}

private void animateAttack(ImageView sprite, boolean toRight) {
    double distance = toRight ? 60 : -60;
    TranslateTransition tt = new TranslateTransition(Duration.millis(100), sprite);
    tt.setByX(distance);
    tt.setCycleCount(2);
    tt.setAutoReverse(true);
    tt.play();
}

/**
 * Animation de clignotement quand on reçoit des dégâts
 */
private void animateDamage(ImageView sprite) {
    FadeTransition ft = new FadeTransition(Duration.millis(50), sprite);
    ft.setFromValue(1.0);
    ft.setToValue(0.2);
    ft.setCycleCount(6);
    ft.setAutoReverse(true);
    ft.play();
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
    displayTypes(playerTypeContainer, activePlayer);
    displayTypes(cpuTypeContainer, activeCpu);

    updateCpuIcons();
    setMoveButtons(activePlayer);



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
    // 1. Récupérer l'ancien et le nouveau Pokémon
    Pokemon oldPokemon = activePlayer; 
    int index = (int) ((Button)event.getSource()).getUserData();
    activePlayer = playerTeam.getPokemons()[index];
    playerTeam.setActivePokemon(activePlayer);
    
    // 2. Mise à jour visuelle immédiate
    battleLog.appendText("\nSwitch : " + activePlayer.getName() + " entre en combat !\n");
    loadSprites();
    closeSwitchMenu();
    refreshUI();
    
    // 3. Logique de tour
    if (oldPokemon != null && !oldPokemon.isFainted()) {
        // Switch volontaire : l'adversaire profite du changement pour attaquer
        processCpuOnlyTurn(); 
    } else {
        // Switch forcé (après un KO) : on redonne la main au joueur pour son tour
        disableUI(false); 
    }
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
            playerSprite.setOpacity(1.0);
            cpuSprite.setOpacity(1.0);
        } catch (Exception e) {
            System.err.println("Erreur de chargement d'image : " + e.getMessage());
        }
    }

    private void openSwitchMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
        tt.setToY(0); tt.play();
        isSwitchMenuVisible = true;
        disableUI(true);
    }

    @FXML private void closeSwitchMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
        tt.setToY(170); tt.play();
        isSwitchMenuVisible = false;
        disableUI(false);
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
    
    @FXML 
private void handleItems(ActionEvent event) { 
    // --- Simulation de soin (Exemple : Potion) ---
    if (activePlayer.getHp() < activePlayer.getMaxHp()) {
        int healAmount = 20;
        activePlayer.setHp(Math.min(activePlayer.getMaxHp(), activePlayer.getHp() + healAmount));
        battleLog.appendText("Objet utilisé sur " + activePlayer.getName() + " (+"+healAmount+" PV) !\n");
        battleLog.appendText("Votre tour se termine.\n");
        
        refreshUI();
        // L'action est faite, le CPU attaque maintenant
        processCpuOnlyTurn(); 
    } else {
        battleLog.appendText(activePlayer.getName() + " a déjà tous ses PV !\n");
    }
}
    private void displayTypes(HBox container, Pokemon pokemon) {
    container.getChildren().clear(); 
    if (pokemon == null || pokemon.getTypes() == null) return;

    for (PokemonType type : pokemon.getTypes()) {
        Label badge = new Label(type.toString().toUpperCase());
        badge.getStyleClass().add("type-badge");
        badge.getStyleClass().add("type-" + type.toString().toLowerCase());
        badge.setStyle("-fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 1 5;");
        
        container.getChildren().add(badge);
    }
}
private void showDamagePopup(ImageView targetSprite, int damage) {

    Label damageLabel = new Label("-" + damage);
    damageLabel.getStyleClass().add("damage-popup");
    
    double startX = targetSprite.getParent().getLayoutX() + (targetSprite.getFitWidth() / 2);
    double startY = targetSprite.getParent().getLayoutY() - 20;

    damageLabel.setLayoutX(startX);
    damageLabel.setLayoutY(startY);
    
    battleStackPane.getChildren().add(damageLabel);

    TranslateTransition moveUp = new TranslateTransition(Duration.millis(800), damageLabel);
    moveUp.setByY(-60); 

    FadeTransition fadeOut = new FadeTransition(Duration.millis(800), damageLabel);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    ParallelTransition pt = new ParallelTransition(damageLabel, moveUp, fadeOut);
    pt.setOnFinished(e -> battleStackPane.getChildren().remove(damageLabel));
    
    pt.play();
}
private void setMoveButtons(Pokemon p) {
    Button[] moveButtons = {move1, move2, move3, move4};
    Attack[] attacks = p.getAttacks();

    for (int i = 0; i < moveButtons.length; i++) {

        moveButtons[i].setGraphic(null); 
        moveButtons[i].setText("");      
        
        moveButtons[i].getStyleClass().removeAll(
            "atk-fire", "atk-water", "atk-grass", "atk-electric", "atk-ice", 
            "atk-fighting", "atk-poison", "atk-ground", "atk-flying", "atk-psychic", 
            "atk-bug", "atk-rock", "atk-ghost", "atk-dragon", "atk-dark", "atk-steel", 
            "atk-fairy", "atk-normal"
        );

        if (i < attacks.length && attacks[i] != null) {
            Attack atk = attacks[i];

            VBox content = new VBox(2);
            content.setAlignment(Pos.CENTER);
            
            Label nameLabel = new Label(atk.getName().toUpperCase());
            nameLabel.setStyle("-fx-text-fill: inherit; -fx-font-weight: bold; -fx-font-size: 13px;");
            
            Label typeLabel = new Label(atk.getType().toString());
            typeLabel.setStyle("-fx-text-fill: inherit; -fx-font-size: 10px; -fx-opacity: 0.8;");

            content.getChildren().addAll(nameLabel, typeLabel);
            
            moveButtons[i].setGraphic(content);
            
            String typeClass = "atk-" + atk.getType().toString().toLowerCase();
            moveButtons[i].getStyleClass().add(typeClass);
            
            moveButtons[i].setVisible(true);
            moveButtons[i].setDisable(false);
        } else {
            moveButtons[i].setVisible(false);
        }
    }
}
private void processCpuOnlyTurn() {
    disableUI(true); 
    
    PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
    pause.setOnFinished(e -> {
        // L'IA choisit et attaque
        Attack cpuAtk = engine.chooseBestAttack(activeCpu, activePlayer);
        processAttack(activeCpu, activePlayer, cpuAtk);
        
        // On attend la fin de l'animation pour rendre la main au joueur
        PauseTransition endPause = new PauseTransition(Duration.seconds(1.0));
        endPause.setOnFinished(ev -> {
            checkBattleStatus(); // Vérifie si le joueur est KO
            if (!activePlayer.isFainted()) disableUI(false); 
        });
        endPause.play();
    });
    pause.play();
}
}