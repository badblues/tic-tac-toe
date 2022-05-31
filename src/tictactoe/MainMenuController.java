package tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {

    @FXML
    AnchorPane mainMenuAnchorPane;

    MainController mainController;

    public void initialize(MainController controller) {
        mainController = controller;
    }

    public void showMenu() {
        mainMenuAnchorPane.setVisible(true);
    }

    public void toLocalGame() {
        mainMenuAnchorPane.setVisible(false);
        mainController.startLocalGame();
    }

    public void toOnlineMenu() {
        mainMenuAnchorPane.setVisible(false);
        mainController.openOnlineMenu();
    }

    public void closeGame() {
        Platform.exit();
        System.exit(0);
    }

}
