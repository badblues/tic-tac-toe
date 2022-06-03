package tictactoe.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import tictactoe.data.ClientData;
import util.Game;
import util.packages.Leaderboard;

import java.util.ArrayList;
import java.util.Optional;

public class OnlineMenuController {

    @FXML
    AnchorPane onlineMenuAnchorPane;
    @FXML
    Label nameLabel;
    @FXML
    TitledPane leaderboardPane;
    @FXML
    TextArea leaderboardText;

    MainController mainController;


    public void initialize(MainController controller) {
        mainController = controller;
    }

    public void showMenu() {
        onlineMenuAnchorPane.setVisible(true);
        nameLabel.setText("PLAYER NAME: " + ClientData.getInstance().getThisClientName());
    }

    public void hideMenu() {
        onlineMenuAnchorPane.setVisible(false);
        leaderboardPane.setVisible(false);
    }

    public void backToMainMenu() {
        mainController.openMainMenu();
    }

    public void startOnlinePlay() {
        openStartGameDialog();
    }

    public void startSpectate() {
        if (!ClientData.getInstance().getGames().isEmpty()) {
            ArrayList<String> choices = new ArrayList<>();
            for (Game game : ClientData.getInstance().getGames())
                choices.add(game.getString());
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("SPECTATE");
            dialog.setHeaderText("WHO TO WATCH??");
            dialog.setContentText("Choose match:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                for (Game game : ClientData.getInstance().getGames())
                    if (game.getString().equals(result.get())) {
                        game.getSpectators().add(ClientData.getInstance().getThisClientId());
                        mainController.startSpectate(game);
                    }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SPECTATE");
            alert.setHeaderText(null);
            alert.setContentText("No available games :(");
            alert.showAndWait();
        }
    }

    public void showLeaderboard() {
        if (!leaderboardPane.isVisible()) {
            ClientController.sendObject("LEADERBOARD");
            leaderboardPane.setVisible(true);
        } else {
            leaderboardPane.setVisible(false);
        }
    }

    public void updateLeaderboard() {
        Leaderboard leaderboard = ClientData.getInstance().getLeaderboard();
        leaderboardText.setText("  PLAYER/GAMES/WINS/LOSES/WINRATE\n");
        for (int i = 0; i < leaderboard.getRowsNumber(); i++) {
            leaderboardText.appendText(leaderboard.getString(i));
            leaderboardText.appendText("\n");
        }
    }

    public void showGameRequestAlert(String senderId) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game request");
            alert.setHeaderText("You've been invited to a game with: " + senderId);
            alert.setContentText("Accept?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                mainController.acceptGame(senderId);
            } else  {
                mainController.declineGame(senderId);
            }
        });
    }

    public void showGameDeclinedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game");
        alert.setHeaderText(null);
        alert.setContentText("Game wasn't accepted :(");
        alert.showAndWait();
    }

    public void openStartGameDialog() {
            ArrayList<String> choices = new ArrayList<>();
            for (Pair<Integer, String> player : ClientData.getInstance().getPlayers())
                if (!player.getValue().equals(ClientData.getInstance().getThisClientName()))
                    choices.add(player.getValue());
        if (!choices.isEmpty()) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("PLAY");
            dialog.setHeaderText("WHO TO PLAY WITH");
            dialog.setContentText("Choose your opponent:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(integer -> ClientController.sendGameRequest(result.get()));

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SPECTATE");
            alert.setHeaderText(null);
            alert.setContentText("No available players :(");
            alert.showAndWait();
        }
    }

}
