package tictactoe;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiplayerWindowController implements Initializable {

    MainController mainController;
    MultiplayerWindow multiplayerWindow;

    public MultiplayerWindowController(MainController controller) {
        mainController = controller;
        multiplayerWindow = new MultiplayerWindow();
    }

    public void show() {
        multiplayerWindow.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
