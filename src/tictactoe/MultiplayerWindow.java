package tictactoe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MultiplayerWindow{

    MultiplayerWindowController multiplayerWindowController;
    Stage stage;
    public MultiplayerWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/multiplayerWindow.fxml"));
            Parent root = loader.load();
            multiplayerWindowController = loader.getController();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public MultiplayerWindowController getMultiplayerWindowController() {
        return multiplayerWindowController;
    }

}
