package tictactoe;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

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

}
