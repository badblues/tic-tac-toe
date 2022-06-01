package tictactoe;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import server.ClientController;
import server.packages.GamePackage;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    MainMenuController mainMenuController;
    @FXML
    GameController gameController;
    @FXML
    OnlineMenuController onlineMenuController;

    ClientController clientController;

    MultiplayerWindow multiplayerWindow;
    MultiplayerWindowController multiplayerWindowController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainMenuController.initialize(this);
        gameController.initialize(this);
        onlineMenuController.initialize(this);
        clientController = new ClientController(this);
        multiplayerWindow = new MultiplayerWindow();
        multiplayerWindowController = multiplayerWindow.getMultiplayerWindowController();
        multiplayerWindowController.initialize(this, multiplayerWindow);
    }

    public void openMainMenu() {
        mainMenuController.showMenu();
        clientController.disconnectFromServer();
    }

    public void openOnlineMenu() {
        onlineMenuController.showMenu();
        clientController.connectToServer();
        clientController.start();
    }

    public void openMultiplayerWindow() {
        multiplayerWindowController.show();
    }

    public void startLocalGame() {
        gameController.endAutoplay();
        gameController.resetGame();
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
        gameController.setOnlineGame(true);
    }

    public void declineGame(int requestSenderId) {
        clientController.sendGameResponse(requestSenderId, "GAME_DECLINE");
    }

    public void startOnlineGame(int id1, int id2) {
        onlineMenuController.hideMenu();
        multiplayerWindowController.hide();
        gameController.startOnlineGame(id1, id2);
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

    public void clientsUpdate() {
        multiplayerWindowController.updateClients();
    }

}
