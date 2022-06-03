package tictactoe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

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
        Pair<String, String> pair = showLoginDialog();
        mainController.openOnlineMenu(pair.getKey(), pair.getValue());
        mainMenuAnchorPane.setVisible(false);
    }

    public Pair<String, String> showLoginDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("CONNECT");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setText("Player");
        TextField ip = new TextField();
        ip.setText("localhost");

        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(name, 1, 0);
        gridPane.add(new Label("IP:"), 0, 1);
        gridPane.add(ip, 1, 1);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(name.getText(), ip.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public void showNameTaken() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CONNECT");
        alert.setHeaderText(null);
        alert.setContentText("NAME ALREADY TAKEN");
        alert.showAndWait();
    }

    public void closeGame() {
        Platform.exit();
        System.exit(0);
    }

}
