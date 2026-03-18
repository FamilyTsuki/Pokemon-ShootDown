package com.pokemon.controllers;

import com.pokemon.models.*;
import com.pokemon.models.UseableItems.*;
import com.pokemon.core.BattleEngine;
import com.pokemon.effect.Effecte;

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
import com.pokemon.core.AudioManager;

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
    @FXML private HBox playerEffectContainer;
    @FXML private HBox cpuEffectContainer;
    @FXML private VBox itemMenu;
    @FXML private Button itemBtn0, itemBtn1; 
    private boolean isItemMenuVisible = false;

    private Team playerTeam, cpuTeam;
    private Pokemon activePlayer, activeCpu;
    private final BattleEngine engine = new BattleEngine();
    private boolean isSwitchMenuVisible = false;
    private boolean isLogVisible = false;
    private int turnCount = 1;
    private boolean isForcedSwitch = false;
    private FadeTransition playerFade;
    private FadeTransition cpuFade;
    

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
    setMoveButtons(activePlayer);
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
        AudioManager.playSound("clic.wav");
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
            processEndOfTurn();
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
            processEndOfTurn();
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
    boolean isPlayerAttacking = (attacker == activePlayer);
    ImageView attackerSprite = isPlayerAttacking ? playerSprite : cpuSprite;
    ImageView targetSprite = isPlayerAttacking ? cpuSprite : playerSprite;

    animateAttack(attackerSprite, isPlayerAttacking);
    AudioManager.playSound("attack.wav");

    int damage = engine.applyDamage(attacker, target, move, battleLog); 
    
    if (damage > 0) {
        animateDamage(targetSprite);
        showDamagePopup(targetSprite, damage);
    }

    battleLog.appendText("• " + attacker.getName() + " utilise " + move.getName() + "\n");
    
    if (target.isFainted()) {
        battleLog.appendText("   -> " + target.getName() + " est KO !\n");
        FadeTransition koFade = new FadeTransition(Duration.millis(500), targetSprite);
        koFade.setToValue(0);
        if (targetSprite == playerSprite) playerFade = koFade;
        else cpuFade = koFade;
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
                isForcedSwitch = true;
                handleSwitch(null); 
            } else {
                showEndGameMessage("DÉFAITE...", Color.RED);
            }
        }
    }

    private void showEndGameMessage(String message, Color color) {
        AudioManager.stopMusic();
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

    displayEffects(playerEffectContainer, activePlayer);
    displayEffects(cpuEffectContainer, activeCpu);

    updateCpuIcons();


}

    @FXML
    private void handleSwitch(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        disableUI(false);
        if (isSwitchMenuVisible && event != null) { 
            if (isForcedSwitch) return; 
            closeSwitchMenu(); 
            return; 
        }
        
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
    AudioManager.playSound("clic.wav");
    int index = (int) ((Button)event.getSource()).getUserData();
    activePlayer = playerTeam.getPokemons()[index];
    playerTeam.setActivePokemon(activePlayer);
    
    loadSprites();
    closeSwitchMenu();
    refreshUI();
    setMoveButtons(activePlayer);
    
    if (isForcedSwitch) {
        battleLog.appendText(activePlayer.getName() + " entre en combat !\n");
        isForcedSwitch = false;
        disableUI(false);
    } else {
        battleLog.appendText("\n=== TOUR " + turnCount + " ====\n");
        battleLog.appendText("Switch : Vous envoyez " + activePlayer.getName() + " !\n");
        executeCpuOnlyTurn();
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
            if (playerFade != null) playerFade.stop();
            if (cpuFade != null) cpuFade.stop();
            playerSprite.setImage(new Image(getClass().getResourceAsStream("/com/pokemon/assets/sprites/" + activePlayer.getId() + ".png")));
            cpuSprite.setImage(new Image(getClass().getResourceAsStream("/com/pokemon/assets/sprites/" + activeCpu.getId() + ".png")));
            playerSprite.setOpacity(1.0);
            cpuSprite.setOpacity(1.0);
            AudioManager.playSound(activePlayer.getId() + ".wav");
        
            AudioManager.playSound(activeCpu.getId() + ".wav");
        } catch (Exception e) {
            System.err.println("Erreur de chargement d'image : " + e.getMessage());
        }
    }

    private void openSwitchMenu() {
    if (isItemMenuVisible) closeItemMenu();
    TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
    tt.setToY(0); tt.play();
    isSwitchMenuVisible = true;
    disableUI(true);
}

    @FXML private void closeSwitchMenu() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), switchMenu);
        tt.setToY(170); tt.play();
        isSwitchMenuVisible = false;
    }

    @FXML private void toggleLog() {
        AudioManager.playSound("clic.wav");
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
    AudioManager.playSound("clic.wav");
    if (isItemMenuVisible) { closeItemMenu(); return; }
    
    itemBtn0.setText("POTION");
    itemBtn0.setUserData(new Potion());
    itemBtn0.setDisable(activePlayer.getHp() >= activePlayer.getMaxHp());

    itemBtn1.setText("ANTIDOTE");
    itemBtn1.setUserData(new Antidote());
    itemBtn1.setDisable(activePlayer.getActiveEffects().isEmpty());

    openItemMenu();
}

@FXML
private void handleItemConfirmation(ActionEvent event) {
    AudioManager.playSound("clic.wav");
    UseableItem selectedItem = (UseableItem) ((Button)event.getSource()).getUserData();
    
    closeItemMenu();
    battleLog.appendText("\n=== TOUR " + turnCount + " ====\n");
    
    selectedItem.use(activePlayer, battleLog);
    refreshUI();
    
    executeCpuOnlyTurn();
}


private void openItemMenu() {
    if (isSwitchMenuVisible) closeSwitchMenu();
    TranslateTransition tt = new TranslateTransition(Duration.millis(300), itemMenu);
    tt.setToY(0); tt.play();
    isItemMenuVisible = true;
    disableUI(true);
}

@FXML private void closeItemMenu() {
    TranslateTransition tt = new TranslateTransition(Duration.millis(300), itemMenu);
    tt.setToY(170); tt.play();
    isItemMenuVisible = false;
    disableUI(false);
}

    private void executeCpuOnlyTurn() {
    disableUI(true);
    
    Attack cpuAtk = engine.chooseBestAttack(activeCpu, activePlayer);
    
    PauseTransition cpuTurnDelay = new PauseTransition(Duration.seconds(1.0));
    cpuTurnDelay.setOnFinished(e -> {
        if (cpuAtk != null && !activeCpu.isFainted()) {
            processAttack(activeCpu, activePlayer, cpuAtk);
        }
        
        PauseTransition endDelay = new PauseTransition(Duration.seconds(1.0));
        endDelay.setOnFinished(ev -> {
            turnCount++;
            processEndOfTurn();
            checkBattleStatus();
            if (!activePlayer.isFainted() && !activeCpu.isFainted()) {
                disableUI(false);
            }
        });
        endDelay.play();
    });
    cpuTurnDelay.play();
}

    private void processEndOfTurn() {
        boolean needsRefresh = false;

        if (!activePlayer.isFainted()) {
            int oldHp = activePlayer.getHp();
            if (activePlayer.getCurrentStatus() != null) activePlayer.getCurrentStatus().onTurnEnd(activePlayer);
            if (activePlayer.getItem() != null) activePlayer.getItem().onTurnEnd(activePlayer);
            
            if (activePlayer.getHp() < oldHp) {
                battleLog.appendText(activePlayer.getName() + " souffre à la fin du tour !\n");
                needsRefresh = true;
                animateDamage(playerSprite);
                if (activePlayer.isFainted()) {
                    battleLog.appendText("   -> " + activePlayer.getName() + " est KO !\n");
                    playerFade = new FadeTransition(Duration.millis(500), playerSprite);
                    playerFade.setToValue(0); playerFade.play();
                }
            } else if (activePlayer.getHp() > oldHp) {
                battleLog.appendText(activePlayer.getName() + " restaure des PV à la fin du tour !\n");
                needsRefresh = true;
            }
        }

        if (!activeCpu.isFainted()) {
            int oldHp = activeCpu.getHp();
            if (activeCpu.getCurrentStatus() != null) activeCpu.getCurrentStatus().onTurnEnd(activeCpu);
            if (activeCpu.getItem() != null) activeCpu.getItem().onTurnEnd(activeCpu);
            
            if (activeCpu.getHp() < oldHp) {
                battleLog.appendText(activeCpu.getName() + " souffre à la fin du tour !\n");
                needsRefresh = true;
                animateDamage(cpuSprite);
                if (activeCpu.isFainted()) {
                    battleLog.appendText("   -> " + activeCpu.getName() + " est KO !\n");
                    cpuFade = new FadeTransition(Duration.millis(500), cpuSprite);
                    cpuFade.setToValue(0); cpuFade.play();
                }
            } else if (activeCpu.getHp() > oldHp) {
                battleLog.appendText(activeCpu.getName() + " restaure des PV à la fin du tour !\n");
                needsRefresh = true;
            }
        }

        if (needsRefresh) refreshUI();
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
private void displayEffects(HBox container, Pokemon pokemon) {
    container.getChildren().clear();
    
    if (pokemon == null || pokemon.getActiveEffects().isEmpty()) return;

    for (Effecte eff : pokemon.getActiveEffects()) {
        Label badge = new Label(eff.getName().toUpperCase());
        
        badge.setStyle("-fx-background-radius: 5; -fx-padding: 2 5; -fx-font-weight: bold; -fx-font-size: 10px; -fx-text-fill: white;");

        if (eff.getName().equalsIgnoreCase("Brûlure") || eff.getName().contains("BURN")) {
            badge.setStyle(badge.getStyle() + "-fx-background-color: #e67e22;"); 
        } else if (eff.getName().contains("Boost")) {
            badge.setStyle(badge.getStyle() + "-fx-background-color: #3498db;"); 
        } else {
            badge.setStyle(badge.getStyle() + "-fx-background-color: #7f8c8d;");
        }

        container.getChildren().add(badge);
    }
}



}