package com.pokemon.controllers;

import com.pokemon.models.*;
import com.pokemon.core.BattleEngine;
import com.pokemon.effect.Effect;
import com.pokemon.items.UseableItems.*;
import com.pokemon.core.AudioManager;

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
import javafx.scene.shape.Circle;

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
    

    /**
     * Initializes the battle state with player and CPU teams.
     */
    public void setupBattle(Team playerTeam, Team cpuTeam) {
        this.playerTeam = playerTeam;
        this.cpuTeam = cpuTeam;
        
        updateActivePokemons();
        
        if (this.activePlayer == null) {
            System.err.println("ERROR: No active Pokemon found for player!");
            return;
        }

        loadSprites();
        refreshUI();
        setMoveButtons(activePlayer);
        disableUI(false); 
        
        battleLog.appendText("A battle begins!\n");
    }

    private void updateActivePokemons() {
        this.activePlayer = playerTeam.getActivePokemon();
        this.activeCpu = cpuTeam.getActivePokemon();

        if (this.activePlayer == null) {
            this.activePlayer = findFirstAvailable(playerTeam);
        }

        if (this.activeCpu == null) {
            this.activeCpu = findFirstAvailable(cpuTeam);
        }
    }

    private Pokemon findFirstAvailable(Team team) {
        for (Pokemon p : team.getPokemons()) {
            if (p != null) {
                team.setActivePokemon(p);
                return p;
            }
        }
        return null;
    }

    @FXML
    private void handleMove(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        Button btn = (Button) event.getSource();
        int moveIdx = getButtonIndex(btn);
        Attack pAtk = activePlayer.getAttacks()[moveIdx];

        if (pAtk == null) return;

        disableUI(true);
        Attack cAtk = engine.chooseBestAttack(activeCpu, activePlayer);

        if (engine.isPlayerFirst(activePlayer, activeCpu)) {
            executeTurnSequence(activePlayer, activeCpu, pAtk, cAtk);
        } else {
            executeTurnSequence(activeCpu, activePlayer, cAtk, pAtk);
        }
    }

    private void executeTurnSequence(Pokemon f, Pokemon s, Attack a1, Attack a2) {
        battleLog.appendText("\n=== TURN " + turnCount + " ====\n");
        battleLog.appendText("[INFO] " + f.getName() + " is faster (" + 
            f.getSpeed() + ")\n");

        processAttack(f, s, a1);
        if (s.isFainted()) {
            handleQuickFinish();
            return;
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(e -> {
            processAttack(s, f, a2);
            PauseTransition endDelay = new PauseTransition(Duration.seconds(1.0));
            endDelay.setOnFinished(ev -> wrapUpTurn());
            endDelay.play();
        });
        delay.play();
    }

    private void handleQuickFinish() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
        pause.setOnFinished(e -> wrapUpTurn());
        pause.play();
    }

    private void wrapUpTurn() {
        turnCount++;
        processEndOfTurn();
        if (activePlayer.isFainted()) disableUI(false);
        checkBattleStatus();
        if (!activePlayer.isFainted() && !activeCpu.isFainted()) {
            disableUI(false);
        }
    }
    
    private void processAttack(Pokemon atk, Pokemon tar, Attack move) {
        boolean isPlayer = (atk == activePlayer);
        ImageView aSprite = isPlayer ? playerSprite : cpuSprite;
        ImageView tSprite = isPlayer ? cpuSprite : playerSprite;

        animateAttack(aSprite, isPlayer, move);
        AudioManager.playSound("attack.wav");

        int dmg = engine.applyDamage(atk, tar, move, battleLog);
        
        battleLog.appendText("• " + atk.getName() + " uses " + move.getName() + "\n");
        battleLog.appendText("  -> Damage: " + dmg + " | " + tar.getName() + 
            ": " + tar.getHp() + "/" + tar.getMaxHp() + " HP\n");

        if (dmg > 0) {
            animateDamage(tSprite);
            showDamagePopup(tSprite, dmg);
        }

        if (tar.isFainted()) handleFainted(tar, tSprite);
        refreshUI();
    }

    private void handleFainted(Pokemon target, ImageView sprite) {
        battleLog.appendText("   -> " + target.getName() + " fainted!\n");
        FadeTransition koFade = new FadeTransition(Duration.millis(500), sprite);
        koFade.setToValue(0);
        if (sprite == playerSprite) playerFade = koFade;
        else cpuFade = koFade;
        koFade.play();
    }

    private void animateAttack(ImageView sprite, boolean isPlayer, Attack move) {
        applyRecoil(sprite, isPlayer);
        
        Circle proj = new Circle(10, move.getType().getColor());
        proj.setEffect(new GaussianBlur(5));
        proj.setManaged(false);

        double startX = sprite.localToScene(sprite.getBoundsInLocal()).getCenterX();
        double startY = sprite.localToScene(sprite.getBoundsInLocal()).getCenterY();
        
        battleStackPane.getChildren().add(proj);
        proj.setLayoutX(startX);
        proj.setLayoutY(startY);

        TranslateTransition shoot = new TranslateTransition(Duration.millis(400), proj);
        shoot.setByX(isPlayer ? 400 : -400);
        shoot.setOnFinished(e -> battleStackPane.getChildren().remove(proj));
        shoot.play();
}

    private void applyRecoil(ImageView sprite, boolean isPlayer) {
        TranslateTransition rt = new TranslateTransition(Duration.millis(100), sprite);
        rt.setByX(isPlayer ? -15 : 15);
        rt.setCycleCount(2);
        rt.setAutoReverse(true);
        rt.play();
    }

    /**
     * Plays a flashing animation when a Pokemon takes damage.
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
            handleCpuFainted();
        } else if (activePlayer.isFainted()) {
            handlePlayerFainted();
        }
    }

    private void handleCpuFainted() {
        Pokemon next = cpuTeam.getNextAvailablePokemon();
        if (next != null) {
            activeCpu = next;
            cpuTeam.setActivePokemon(next);
            battleLog.appendText("Opponent sends out " + next.getName() + "!\n");
            loadSprites();
            refreshUI();
            disableUI(false);
        } else {
            showEndGameMessage("VICTORY!", Color.GREEN);
            playVictorySounds();
        }
    }

    private void handlePlayerFainted() {
        if (playerTeam.hasAvailablePokemon()) {
            battleLog.appendText("Your Pokemon fainted! Forced switch.\n");
            isForcedSwitch = true;
            handleSwitch(null);
        } else {
            showEndGameMessage("DEFEAT...", Color.RED);
            AudioManager.stopMusic();
            AudioManager.playSound("game_over.wav");
        }
    }

    private void playVictorySounds() {
        AudioManager.stopMusic();
        AudioManager.playSound("victory.wav");
        AudioManager.playSound("yeah-boy.wav");
    }

    private void showEndGameMessage(String msg, Color color) {
        AudioManager.stopMusic();
        battleLog.appendText(msg + "\n");
        disableUI(true);
        mainBattleArea.setVisible(false);

        ColorAdjust gray = new ColorAdjust();
        gray.setSaturation(-1.0); 
        mainBattleArea.setEffect(new GaussianBlur(10));

        Text endText = createEndGameText(msg, color);
        battleStackPane.getChildren().add(endText);
    }

    private Text createEndGameText(String msg, Color color) {
        Text text = new Text(msg);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
        text.setFill(color);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(3);
        return text;
    }
    private void refreshUI() {
        updatePokemonUI(activePlayer, playerHPBar, playerHPText, playerNameLabel);
        updatePokemonUI(activeCpu, cpuHPBar, cpuHPText, cpuNameLabel);
        
        displayTypes(playerTypeContainer, activePlayer);
        displayTypes(cpuTypeContainer, activeCpu);

        displayEffects(playerEffectContainer, activePlayer);
        displayEffects(cpuEffectContainer, activeCpu);

        updateCpuIcons();
    }

    private void updatePokemonUI(Pokemon p, ProgressBar bar, Label hp, Label name) {
        double progress = (double) p.getHp() / p.getMaxHp();
        bar.setProgress(progress);
        hp.setText(p.getHp() + " / " + p.getMaxHp());
        name.setText(p.getName().toUpperCase());
    }

    @FXML
    private void handleSwitch(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        disableUI(false);
        
        if (isSwitchMenuVisible && event != null) { 
            if (!isForcedSwitch) closeSwitchMenu(); 
            return; 
        }
        
        updateSwitchButtons();
        openSwitchMenu();
    }

    private void updateSwitchButtons() {
        Button[] btns = {switchBtn0, switchBtn1, switchBtn2, 
                        switchBtn3, switchBtn4, switchBtn5};
        Pokemon[] pks = playerTeam.getPokemons();

        for (int i = 0; i < 6; i++) {
            if (i >= pks.length || pks[i] == null) {
                btns[i].setText("❌");
                btns[i].setDisable(true);
            } else {
                btns[i].setText(pks[i].getName());
                btns[i].setDisable(pks[i].isFainted() || pks[i] == activePlayer);
                btns[i].setUserData(i);
            }
        }
    }

    @FXML
    private void handleSwitchConfirmation(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        Button btn = (Button) event.getSource();
        int idx = (int) btn.getUserData();
        
        updateActivePlayer(idx);
        
        boolean forced = isForcedSwitch;
        isForcedSwitch = false;
        closeSwitchMenu();
        refreshUI();
        setMoveButtons(activePlayer);
        
        if (forced) {
            logSwitchIn();
            disableUI(false);
        } else {
            logManualSwitch();
            executeCpuOnlyTurn();
        }
    }

    private void updateActivePlayer(int index) {
        activePlayer = playerTeam.getPokemons()[index];
        playerTeam.setActivePokemon(activePlayer);
        loadSprites();
    }

    private void logSwitchIn() {
        battleLog.appendText(activePlayer.getName() + " enters the battle!\n");
    }

    private void logManualSwitch() {
        battleLog.appendText("\n=== TURN " + turnCount + " ====\n");
        battleLog.appendText("Switch: You sent out " + activePlayer.getName() + "!\n");
    }

    private void updateCpuIcons() {
        cpuTeamStatus.getChildren().clear();
        for (Pokemon p : cpuTeam.getPokemons()) {
            if (p != null) {
                cpuTeamStatus.getChildren().add(createCpuIcon(p));
            }
        }
    }

    private ImageView createCpuIcon(Pokemon p) {
        String path = "/com/pokemon/assets/sprites/" + p.getId() + ".png";
        var res = getClass().getResourceAsStream(path);
        if (res == null) {
            res = getClass().getResourceAsStream(
                "/com/pokemon/assets/sprites/missingno.png");
        }
        
        ImageView icon = new ImageView(new Image(res));
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        
        if (p.isFainted()) {
            ColorAdjust bw = new ColorAdjust();
            bw.setSaturation(-1.0);
            icon.setEffect(bw);
        }
        return icon;
    }

    /**
     * Enables or disables the move buttons based on the given state.
     */
    private void disableUI(boolean state) {
        Button[] buttons = {move1, move2, move3, move4};
        for (Button btn : buttons) {
            if (btn != null) {
                btn.setDisable(state);
            }
        }
    }

    private void loadSprites() {
        try {
            stopFadeTransitions();
            
            playerSprite.setImage(loadIndividualSprite(activePlayer.getId()));
            cpuSprite.setImage(loadIndividualSprite(activeCpu.getId()));
            
            playerSprite.setOpacity(1.0);
            cpuSprite.setOpacity(1.0);
            playerSprite.setScaleX(-1); 
            
            AudioManager.playSound(activePlayer.getId() + ".wav");
            AudioManager.playSound(activeCpu.getId() + ".wav");
        } catch (Exception e) {
            System.err.println("Sprite loading error: " + e.getMessage());
        }
    }

    private Image loadIndividualSprite(int id) {
        String path = "/com/pokemon/assets/sprites/" + id + ".png";
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            stream = getClass().getResourceAsStream(
                "/com/pokemon/assets/sprites/missingno.png");
        }
        return new Image(stream);
    }

    private void stopFadeTransitions() {
        if (playerFade != null) playerFade.stop();
        if (cpuFade != null) cpuFade.stop();
    }

    private void openSwitchMenu() {
        if (isItemMenuVisible) closeItemMenu();
        TranslateTransition tt = new TranslateTransition(
            Duration.millis(300), switchMenu);
        tt.setToY(0);
        tt.play();
        isSwitchMenuVisible = true;
        disableUI(true);
    }

    @FXML 
    private void closeSwitchMenu() {
        if (isForcedSwitch) return; // Prevent closing if switch is mandatory
        TranslateTransition tt = new TranslateTransition(
            Duration.millis(300), switchMenu);
        tt.setToY(170);
        tt.play();
        isSwitchMenuVisible = false;
    }

    @FXML 
    private void toggleLog() {
        AudioManager.playSound("clic.wav");
        TranslateTransition tt = new TranslateTransition(
            Duration.millis(300), logPane);
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
        if (isForcedSwitch) return;
        
        if (isItemMenuVisible) {
            closeItemMenu();
            return;
        }
        
        setupItemButton(itemBtn0, (com.pokemon.models.UseableItem) new Potion(), 
            activePlayer.getHp() >= activePlayer.getMaxHp());
            
        setupItemButton(itemBtn1, (com.pokemon.models.UseableItem) new Antidote(), 
            activePlayer.getActiveEffects().isEmpty());

        openItemMenu();
    }

    private void setupItemButton(Button btn, UseableItem item, boolean disabled) {
        btn.setText(item.getName().toUpperCase());
        btn.setUserData(item);
        btn.setDisable(disabled);
    }

    @FXML
    private void handleItemConfirmation(ActionEvent event) {
        AudioManager.playSound("clic.wav");
        Button btn = (Button) event.getSource();
        UseableItem item = (UseableItem) btn.getUserData();
        
        closeItemMenu();
        battleLog.appendText("\n=== TURN " + turnCount + " ====\n");
        
        if (item != null) {
            item.use(activePlayer, battleLog);
        }
        
        refreshUI();
        executeCpuOnlyTurn();
    }

    private void openItemMenu() {
        if (isSwitchMenuVisible) closeSwitchMenu();
        TranslateTransition tt = new TranslateTransition(
            Duration.millis(300), itemMenu);
        tt.setToY(0);
        tt.play();
        isItemMenuVisible = true;
        disableUI(true);
    }

    @FXML 
    private void closeItemMenu() {
        TranslateTransition tt = new TranslateTransition(
            Duration.millis(300), itemMenu);
        tt.setToY(170);
        tt.play();
        isItemMenuVisible = false;
        disableUI(false);
    }

    private void executeCpuOnlyTurn() {
        disableUI(true);
        Attack cAtk = engine.chooseBestAttack(activeCpu, activePlayer);
        
        PauseTransition cpuDelay = new PauseTransition(Duration.seconds(1.0));
        cpuDelay.setOnFinished(e -> {
            if (cAtk != null && !activeCpu.isFainted()) {
                processAttack(activeCpu, activePlayer, cAtk);
            }
            
            PauseTransition endDelay = new PauseTransition(Duration.seconds(1.0));
            endDelay.setOnFinished(ev -> wrapUpTurn());
            endDelay.play();
        });
        cpuDelay.play();
    }

    private void processEndOfTurn() {
        boolean pChanged = applyEndOfTurnEffects(activePlayer, playerSprite);
        boolean cChanged = applyEndOfTurnEffects(activeCpu, cpuSprite);

        if (pChanged || cChanged) refreshUI();
    }

    private boolean applyEndOfTurnEffects(Pokemon p, ImageView sprite) {
        if (p.isFainted()) return false;
        int oldHp = p.getHp();

        if (p.getCurrentStatus() != null) p.getCurrentStatus().onTurnEnd(p);
        if (p.getItem() != null) p.getItem().onTurnEnd(p);

        if (p.getHp() < oldHp) {
            logEndTurnDamage(p, sprite);
            return true;
        } else if (p.getHp() > oldHp) {
            battleLog.appendText(p.getName() + " restored HP!\n");
            return true;
        }
        return false;
    }

    private void logEndTurnDamage(Pokemon p, ImageView sprite) {
        battleLog.appendText(p.getName() + " suffers end-turn damage!\n");
        animateDamage(sprite);
        if (p.isFainted()) handleFainted(p, sprite);
    }    
    
    private void displayTypes(HBox container, Pokemon pokemon) {
        container.getChildren().clear(); 
        if (pokemon == null || pokemon.getTypes() == null) return;

        for (PokemonType type : pokemon.getTypes()) {
            String typeStr = type.toString().toLowerCase();
            Label badge = new Label(typeStr.toUpperCase());
            
            badge.getStyleClass().addAll("type-badge", "type-" + typeStr);
            badge.setStyle("-fx-text-fill: white; -fx-font-size: 9px; " +
                        "-fx-padding: 1 5;");
            
            container.getChildren().add(badge);
        }
    }

    private void showDamagePopup(ImageView sprite, int dmg) {
        Label label = createDamageLabel(dmg);
        
        double startX = sprite.getParent().getLayoutX() + (sprite.getFitWidth() / 2);
        double startY = sprite.getParent().getLayoutY() - 20;

        label.setLayoutX(startX);
        label.setLayoutY(startY);
        battleStackPane.getChildren().add(label);

        TranslateTransition move = new TranslateTransition(Duration.millis(800), label);
        move.setByY(-60);
        
        FadeTransition fade = new FadeTransition(Duration.millis(800), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        
        ParallelTransition pt = new ParallelTransition(label, move, fade);
        pt.setOnFinished(e -> battleStackPane.getChildren().remove(label));
        pt.play();
    }

    private Label createDamageLabel(int dmg) {
        Label label = new Label("-" + dmg);
        label.getStyleClass().add("damage-popup");
        return label;
    }

    private void setMoveButtons(Pokemon p) {
        Button[] btns = {move1, move2, move3, move4};
        Attack[] attacks = p.getAttacks();

        for (int i = 0; i < btns.length; i++) {
            clearButtonStyle(btns[i]);
            if (i < attacks.length && attacks[i] != null) {
                Attack atk = attacks[i];
                btns[i].setGraphic(createMoveContent(atk));
                btns[i].getStyleClass().add("atk-" + 
                    atk.getType().toString().toLowerCase());
                btns[i].setVisible(true);
                btns[i].setDisable(false);
            } else {
                btns[i].setVisible(false);
            }
        }
    }

    private void clearButtonStyle(Button btn) {
        btn.setGraphic(null);
        btn.setText("");
        btn.getStyleClass().removeIf(s -> s.startsWith("atk-"));
    }

    private VBox createMoveContent(Attack atk) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        Label name = new Label(atk.getName().toUpperCase());
        name.setStyle("-fx-text-fill: inherit; -fx-font-weight: bold;");
        Label type = new Label(atk.getType().toString());
        type.setStyle("-fx-text-fill: inherit; -fx-font-size: 10px;");
        box.getChildren().addAll(name, type);
        return box;
    }

    private void displayEffects(HBox container, Pokemon pokemon) {
        container.getChildren().clear();
        if (pokemon == null || pokemon.getActiveEffects().isEmpty()) return;

        for (Effect eff : pokemon.getActiveEffects()) {
            Label badge = new Label(eff.getName().toUpperCase());
            String base = "-fx-background-radius: 5; -fx-padding: 2 5; " +
                        "-fx-font-weight: bold; -fx-font-size: 10px; " +
                        "-fx-text-fill: white;";
            
            badge.setStyle(base + "-fx-background-color: " + getEffectColor(eff) + ";");
            container.getChildren().add(badge);
        }
    }

    private String getEffectColor(Effect eff) {
        String name = eff.getName().toLowerCase();
        if (name.contains("burn") || name.contains("brûlure")) return "#e67e22";
        if (name.contains("boost")) return "#3498db";
        return "#7f8c8d";
    }
}