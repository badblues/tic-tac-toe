package tictactoe;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import util.packages.ChatMessage;

public class ChatController {

    @FXML
    TextArea chatText;
    @FXML
    TextField chatField;
    @FXML
    AnchorPane chatPane;

    private final int maxLength = 80;

    MainController mainController;

    public void initialize(MainController controller) {
        mainController = controller;
        chatField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (chatField.getText().length() > maxLength) {
                String s = chatField.getText().substring(0, maxLength);
                chatField.setText(s);
            }
        });
    }

    private void sendMessage() {
        ChatMessage message = new ChatMessage();
        mainController.getGameController().getId1();
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
