package tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Game;
import util.packages.GamePackage;
import util.packages.GameResult;

import java.util.*;

public class GameController {
    private final GameState gameState = GameState.getInstance();

    @FXML
    Button button0;
    @FXML
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    @FXML
    Button button4;
    @FXML
    Button button5;
    @FXML
    Button button6;
    @FXML
    Button button7;
    @FXML
    Button button8;

    @FXML
    ImageView image0;
    @FXML
    ImageView image1;
    @FXML
    ImageView image2;
    @FXML
    ImageView image3;
    @FXML
    ImageView image4;
    @FXML
    ImageView image5;
    @FXML
    ImageView image6;
    @FXML
    ImageView image7;
    @FXML
    ImageView image8;

    @FXML
    Label label;
    @FXML
    Button resetButton;
    @FXML
    Button menuButton;
    @FXML
    Button rematchButton;

    ArrayList<Button> buttons;
    ArrayList<ImageView> images;
    Thread autoplayThread;
    long AUTOPLAY_DELAY = 500;
    MainController mainController;
    Game game;
    boolean onlineGame = false;


    public void initialize(MainController controller) {
        mainController = controller;
        buttons = new ArrayList<>(Arrays.asList(button0, button1, button2, button3, button4, button5, button6, button7, button8));
        images = new ArrayList<>(Arrays.asList(image0, image1, image2, image3, image4, image5, image6, image7, image8));
        startAutoplay();
    }

    public void buttonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        int id = Integer.parseInt(button.getId().substring(6));
        activateCell(id);
        checkGameOver();
        if (onlineGame) {
            turnMade();
            checkGameResult();
        }
    }
    public void activateCell(int id) {
        buttons.get(id).setDisable(true);
        if (gameState.getTurn() % 2 == 0) {
            images.get(id).setImage(new Image("/cross.png"));
            gameState.getBoard().set(id, 1);
        }
        else {
            images.get(id).setImage(new Image("/nought.png"));
            gameState.getBoard().set(id, 2);
        }
        gameState.setLastTurnCellId(id);
        gameState.nextTurn();
    }

    public void nextTurnOnlineGame(GamePackage gamePackage) {
        label.setText("YOUR TURN");
        if (gamePackage != null) {
            if (gamePackage.getLastTurnCell() != -1)
                activateCell(gamePackage.getLastTurnCell());
            game = new Game(gamePackage.getReceiver(), gamePackage.getSender());
            gameState.readGamePackage(gamePackage);
        }
        if (!checkGameOver()) {
            enableSomeButtons();
        }
    }

    private void turnMade() {
        disableAllButtons();
        passTurn();
        if (!checkGameOver()) {
            label.setText("OPPONENT'S TURN");
        }
    }

    public void passTurn() {
        GamePackage gamePackage = gameState.writeGamePackage(game);
        mainController.sendObject(gamePackage);
    }

    public void startOnlineGame(int id1, int id2) {
        game = new Game(id1, id2);
        onlineGame = true;
        endAutoplay();
        hideResetButton();
        disableAllButtons();
        if (coinflipWin()) {
            nextTurnOnlineGame(null);
        } else {
            passTurn();
        }
    }

    public void endOnlineGame() {
        showResetButton();
        System.out.println("calling ending online game");
        GamePackage gamePackage = new GamePackage(game.getSender(), game.getReceiver(), "GAME_END");
        mainController.sendObject(gamePackage);
    }

    public void rematch() {
        rematchButton.setVisible(false);
        GamePackage gamePackage = gameState.writeGamePackage(game);
        gamePackage.setMessage("GAME_REMATCH");
        resetGame();
        mainController.sendObject(gamePackage);
        disableAllButtons();
        if (coinflipWin()) {
            nextTurnOnlineGame(null);
        } else {
            passTurn();
        }
    }

    public void readBoard(GamePackage gamePackage) {
        label.setText("TIC-TAC-TOE");
        game = new Game(gamePackage.getSender(), gamePackage.getReceiver());
        gameState.readGamePackage(gamePackage);
        for (int i = 0; i < 9; i++) {
            System.out.println(gameState.getBoard());
            switch(gameState.getBoard().get(i)) {
                case 0:
                    images.get(i).setImage(new Image("/empty.png"));
                    break;
                case 1:
                    images.get(i).setImage(new Image("/cross.png"));
                    break;
                case 2:
                    images.get(i).setImage(new Image("/nought.png"));
                    break;
            }
        }
        checkGameOver();
    }

    private boolean checkGameOver() {
        int winner = gameState.gameWinner();
        if (winner == 0)
            return false;
        if (winner == 1) {
            label.setText("CROSSES WON!");
        } else if (winner == 2) {
            label.setText("NULLS WON!");
        }
        buttons.forEach(button -> button.setDisable(true));
        if (onlineGame) {
            rematchButton.setVisible(true);
        }
        return true;
    }

    private boolean coinflipWin() {
        Random random = new Random();
        boolean heads = random.nextBoolean();
        boolean usersChoiseIsHeads = true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("COINFLIP");
        alert.setHeaderText(null);
        alert.setContentText("Heads or tails?");

        ButtonType headsButton = new ButtonType("HEADS");
        ButtonType tailsButton = new ButtonType("TAILS");
        alert.getButtonTypes().setAll(headsButton, tailsButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == headsButton){
            usersChoiseIsHeads = true;
        } else if (result.get() == tailsButton) {
            usersChoiseIsHeads = false;
        }

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("COINFLIP");
        alert.setHeaderText(null);
        alert.setContentText("COIN: " + (heads? "HEADS" : "TAILS"));

        alert.showAndWait();
        return heads == usersChoiseIsHeads;
    }

    public void resetGame() {
        gameState.restart();
        images.forEach(imageView -> {imageView.setImage(new Image("/empty.png"));});
        buttons.forEach(button -> {button.setDisable(false);});
        label.setText("TIC-TAC-TOE");
    }

    public void openMenu() {
        if (onlineGame) {
            setOnlineGame(false);
            endOnlineGame();
            game = null;
        }
        hideRematchButton();
        mainController.openMainMenu();
        mainController.getChatController().hideChat();
        resetGame();
        startAutoplay();
    }

    public void closeOnlineGame() {
        onlineGame = false;
        mainController.openMainMenu();
        showResetButton();
        resetGame();
        startAutoplay();
    }

    public void checkGameResult() {
        int winner = gameState.gameWinner();
        if (winner == 0)
            return;
        Game thisGame = ClientController.getGameFromId(ClientController.getClientId());
        String name1 = ClientController.getClientName();
        String name2 = Objects.equals(thisGame.getReceiverName(), name1) ? thisGame.getSenderName() : thisGame.getReceiverName();
        GameResult gameResult = new GameResult(name1, name2);
        if (winner == 1 && gameState.getTurn() % 2 == 1) {
            gameResult.setResults("WIN", "LOSE");
            mainController.sendObject(gameResult);
        } else if (winner == 2 && gameState.getTurn() % 2 == 0) {
            gameResult.setResults("WIN", "LOSE");
            mainController.sendObject(gameResult);
        } else if (winner == 3) {
            gameResult.setResults("DRAW", "DRAW");
            mainController.sendObject(gameResult);
        }
    }

    public void startAutoplay() {
        autoplayThread = new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            Random rand = new Random();
            ArrayList<Integer> filledCells = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
            while(true) {
                long thisTime = System.currentTimeMillis();
                if ((thisTime - lastTime) >= AUTOPLAY_DELAY) {
                    Platform.runLater(() -> {
                        if (checkGameOver()) {
                            resetGame();
                            for (int i = 0; i < 9; i++)  filledCells.set(i, 0);
                        }
                    });
                    lastTime = thisTime;
                    int i;
                    do {
                        i = Math.abs(rand.nextInt(9));
                    } while(filledCells.get(i) == 1);
                    filledCells.set(i, 1);
                    activateCell(i);
                }
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
            }
        });
        autoplayThread.start();
    }

    public void endAutoplay() {
        if (autoplayThread != null) {
            autoplayThread.interrupt();
            autoplayThread = null;
        }
        resetGame();
    }

    private void enableSomeButtons() {
        for (int i = 0; i < 9; i++) {
            if (gameState.getBoard().get(i) == 0) {
                buttons.get(i).setDisable(false);
            }
        }
    }

    public void disableAllButtons() {
        buttons.forEach(button -> button.setDisable(true));
    }

    public void hideResetButton() {
        resetButton.setVisible(false);
    }

    public void showResetButton() {
        resetButton.setVisible(true);
    }

    public void hideMenuButton() {
        menuButton.setVisible(false);
    }

    public void showMenuButton() {
        menuButton.setVisible(true);
    }

    public void hideRematchButton() {
        rematchButton.setVisible(false);
    }

    public void setOnlineGame(boolean boo) {
        onlineGame = boo;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
