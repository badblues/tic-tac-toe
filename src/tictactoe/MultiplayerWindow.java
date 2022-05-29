package tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MultiplayerWindow {

    Stage stage;
    public MultiplayerWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app.fxml"));
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
