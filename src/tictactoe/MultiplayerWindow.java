package tictactoe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MultiplayerWindowController {

    MainController mainController;
    Stage stage;
    public MultiplayerWindowController(MainController controller) {
        try {
            mainController = controller;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/multiplayerWindow.fxml"));
            Parent root = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.show();
    }

}
