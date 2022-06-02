package tictactoe;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import server.ClientController;
import util.packages.GamePackage;
import util.Game;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    MainMenuController mainMenuController;
    @FXML
    GameController gameController;
    @FXML
    OnlineMenuController onlineMenuController;
    @FXML
    ChatController chatController;

    ClientController clientController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainMenuController.initialize(this);
        gameController.initialize(this);
        onlineMenuController.initialize(this);
        chatController.initialize(this);
        clientController = new ClientController(this);
        clientController.start();
    }

    public void openMainMenu() {
        mainMenuController.showMenu();
        gameController.hideMenuButton();
        gameController.hideResetButton();
        clientController.disconnectFromServer();
    }

    public void openOnlineMenu() {
        onlineMenuController.showMenu();
        gameController.hideMenuButton();
        gameController.hideResetButton();
        clientController.connectToServer();
    }

    public void openMultiplayerWindow() {
        onlineMenuController.openPlayDialog();
    }

    public void startLocalGame() {
        gameController.endAutoplay();
        gameController.resetGame();
        gameController.showMenuButton();
        gameController.showResetButton();
    }

    public void requestOnlineGame(int receiver) {
        clientController.sendGameRequest(receiver);
    }

    public void showGameRequest(int senderId) {
        onlineMenuController.showGameAlert(senderId);
    }

    public void acceptGame(int requestSenderId) {
        clientController.sendGameResponse(requestSenderId, "GAME_ACCEPT");
        onlineMenuController.hideMenu();
        gameController.endAutoplay();
        gameController.disableAllButtons();
        gameController.showMenuButton();
        gameController.hideResetButton();
        gameController.setOnlineGame(true);
        chatController.showChat();
    }

    public void declineGame(int requestSenderId) {
        clientController.sendGameResponse(requestSenderId, "GAME_DECLINE");
    }

    public void startOnlineGame(int id1, int id2) {
        onlineMenuController.hideMenu();
        chatController.showChat();
        gameController.startOnlineGame(id1, id2);
    }

    public void gameRematch() {
        gameController.resetGame();
        gameController.disableAllButtons();
        gameController.hideRematchButton();
    }

    public void sendGamePackage(GamePackage gamePackage) {
        clientController.sendGamePackage(gamePackage);
    }

    public void getGamePackage(GamePackage gamePackage) {
        Platform.runLater(() -> {
            gameController.nextTurnOnlineGame(gamePackage);
        });
    }

    public void showGameDeclined() {
        onlineMenuController.showGameDeclined();
    }

    public void startSpectate(Game game) {
        clientController.sendGameSpectator(game);
        onlineMenuController.hideMenu();
        gameController.endAutoplay();
        gameController.resetGame();
        gameController.hideResetButton();
        gameController.showMenuButton();
        gameController.disableAllButtons();
        chatController.showChat();
    }

    public void spectatePackage(GamePackage gamePackage) {
        System.out.println("spectating package");
        gameController.readBoard(gamePackage);
    }

    public void closeOnlineGame() {
        Platform.runLater(() -> {
            gameController.closeOnlineGame();
            chatController.hideChat();
        });
    }

    public GameController getGameController() {
        return gameController;
    }

}
