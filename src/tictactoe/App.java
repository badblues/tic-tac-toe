package tictactoe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;;
//TODO: view +++
//TODO: local play +++
//TODO: menu and switching scenes +++
//TODO: background play on menu +++
//TODO: server connecting  +++
//TODO: choosing who to play with +++
//TODO: leaving current match +++
//TODO: ability to continue playing after won game +++
//TODO: spectator mode
//TODO: changing name before online games
//TODO: leaderboards
//TODO: redesign
//TODO: game options 3x3 4x4 5x5

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setOnCloseRequest(windowEvent ->  {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
