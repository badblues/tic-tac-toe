package tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class OnlineMenuController {

    @FXML
    AnchorPane onlineMenuAnchorPane;
    MainController mainController;

    public void initialize(MainController controller) {
        mainController = controller;
    }

    public void showMenu() {
        onlineMenuAnchorPane.setVisible(true);
    }

    public void hideMenu() {
        onlineMenuAnchorPane.setVisible(false);
    }

    public void backToMainMenu() {
        onlineMenuAnchorPane.setVisible(false);
        mainController.openMainMenu();
    }

    public void startOnlinePlay() {
        mainController.openMultiplayerWindow();
    }

    public void startSpectate() {

    }

    public void changeName() {

    }

    public void toLeaderboard() {

    }

    public void showGameAlert(int senderId) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game request");
            alert.setHeaderText("You've been invited to a game with Player " + senderId);
            alert.setContentText("Accept?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                mainController.acceptGame(senderId);
            } else {
                mainController.declineGame(senderId);
            }
        });
    }

    public void showGameDeclined() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game");
            alert.setHeaderText(null);
            alert.setContentText("Game wasn't accepted :(");
            alert.showAndWait();
        });
    }

}
