package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import tictactoe.data.ClientData;
import util.Game;
import util.transferObjects.ChatMessage;

public class ChatController {

    @FXML
    TextArea chatText;
    @FXML
    TextField inputField;
    @FXML
    AnchorPane chatPane;

    private final int maxMessageLength = 80;

    MainController mainController;

    public void initialize(MainController controller) {
        mainController = controller;
        inputField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (inputField.getText().length() > maxMessageLength) {
                String s = inputField.getText().substring(0, maxMessageLength);
                inputField.setText(s);
            }
        });
    }

    private void sendMessage() {
        Game game = mainController.getGameController().getGame();
        ChatMessage message = new ChatMessage(ClientData.getInstance().getThisClientName() + "", game, inputField.getText());
        ClientController.sendObject(message);
        inputField.setText("");
    }

    public void readMessage(ChatMessage message) {
        chatText.appendText("\n" + message.getSender() + ": " + message.getMessage());
    }

    public void resetChat() {
        chatText.setText("");
    }

    public void keyEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            sendMessage();
    }

    public void showChat() {
        chatPane.setVisible(true);
    }

    public void hideChat() {
        chatPane.setVisible(false);
    }

}
