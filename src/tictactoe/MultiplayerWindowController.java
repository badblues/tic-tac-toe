package tictactoe;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import server.ClientController;

public class MultiplayerWindowController {

    @FXML
    ComboBox<Integer> opponentComboBox;

    MainController mainController;
    MultiplayerWindow multiplayerWindow;

    public void initialize(MainController controller, MultiplayerWindow multiplayerWindow) {
        mainController = controller;
        this.multiplayerWindow = multiplayerWindow;
    }

    public void show() {
        multiplayerWindow.getStage().show();
    }

    public void hide() {
        multiplayerWindow.getStage().hide();
    }

    public void startGame() {
        if (opponentComboBox.getValue() != null)
            mainController.requestOnlineGame(opponentComboBox.getValue());
    }

    public void updateClients() {
        opponentComboBox.getItems().clear();
        for (Integer client : ClientController.getClients())
            if (client != ClientController.getClientId()) {
                opponentComboBox.getItems().add(client);
            }
    }



}
