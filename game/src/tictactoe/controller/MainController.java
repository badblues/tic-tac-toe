package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import tictactoe.data.ClientData;
import util.Game;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    GameController gameController;
    @FXML
    MainMenuController mainMenuController;
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
        onlineMenuController.hideMenu();
        clientController.disconnectFromServer();
    }

    public boolean openOnlineMenu(String name, String ip) {
        if (clientController.connectToServer(name, ip)) {
            onlineMenuController.showMenu();
            return true;
        }
        return false;
    }

    public void startLocalGame() {
        gameController.endAutoplay();
        gameController.resetGame();
        gameController.showMenuButton();
        gameController.showResetButton();
    }

    public void startOnlineGame(int id1, int id2) {
        onlineMenuController.hideMenu();
        chatController.showChat();
        gameController.showMenuButton();
        gameController.startOnlineGame(id1, id2);
    }

    public void acceptGame(String requestSenderName) {
        int myId = ClientData.getInstance().getThisClientId();
        int requestSenderId = ClientData.getInstance().getClientId(requestSenderName);
        gameController.setGame(new Game(myId, requestSenderId));
        clientController.sendGameResponse(requestSenderName, "GAME_ACCEPT");
        onlineMenuController.hideMenu();
        gameController.endAutoplay();
        gameController.disableAllButtons();
        gameController.showMenuButton();
        gameController.hideResetButton();
        gameController.setOnlineGame(true);
        chatController.showChat();
    }

    public void declineGame(String requestSenderName) {
        clientController.sendGameResponse(requestSenderName, "GAME_DECLINE");
    }


    public void gameRematch() {
        gameController.resetGame();
        gameController.disableAllButtons();
        gameController.hideRematchButton();
    }

    public void closeOnlineGame() {
        gameController.closeOnlineGame();
        chatController.hideChat();
    }

    public void startSpectate(Game game) {
        ClientController.sendObject(game);
        onlineMenuController.hideMenu();
        gameController.endAutoplay();
        gameController.resetGame();
        gameController.hideResetButton();
        gameController.showMenuButton();
        gameController.disableAllButtons();
        gameController.setGame(game);
        chatController.showChat();
    }

    public void nameTaken() {
        openMainMenu();
        mainMenuController.showNameTakeAlert();
    }

    public void showGameDeclined() {
        onlineMenuController.showGameDeclinedAlert();
    }

    public GameController getGameController() {
        return gameController;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public OnlineMenuController getOnlineMenuController()  {
        return onlineMenuController;
    }

}
