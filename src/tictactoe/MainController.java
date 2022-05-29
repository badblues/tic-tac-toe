package tictactoe;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import server.ClientController;
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
    //MultiplayerWindow multiplayerWindow = new MultiplayerWindow();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainMenuController.initialize(this);
        gameController.initialize(this);
        onlineMenuController.initialize(this);
    }

    public void openMainMenu() {
        mainMenuController.showMenu();
    }

    public void openOnlineMenu() {
        onlineMenuController.showMenu();
    }

    public void startLocalGame() {
        gameController.endAutoplay();
        gameController.resetGame();
    }

}
