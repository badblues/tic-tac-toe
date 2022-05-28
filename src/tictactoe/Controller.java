package tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private GameState gameState = GameState.getInstance();

    @FXML
    Button button0;
    @FXML
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    @FXML
    Button button4;
    @FXML
    Button button5;
    @FXML
    Button button6;
    @FXML
    Button button7;
    @FXML
    Button button8;

    @FXML
    ImageView image0;
    @FXML
    ImageView image1;
    @FXML
    ImageView image2;
    @FXML
    ImageView image3;
    @FXML
    ImageView image4;
    @FXML
    ImageView image5;
    @FXML
    ImageView image6;
    @FXML
    ImageView image7;
    @FXML
    ImageView image8;
    @FXML
    Label label;


    ArrayList<Button> buttons;
    ArrayList<ImageView> images;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button0, button1, button2, button3, button4, button5, button6, button7, button8));
        images = new ArrayList<>(Arrays.asList(image0, image1, image2, image3, image4, image5, image6, image7, image8));
    }

    public void buttonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();  //getting pressed button
        int id = Integer.parseInt(button.getId().substring(6));
        cellActivated(id);
    }

    public void cellActivated(int id) {
        buttons.get(id).setDisable(true);
        if (gameState.getTurn() % 2 == 0) {
            images.get(id).setImage(new Image("/cross.png"));
            gameState.getBoard().set(id, 1);
        }
        else {
            images.get(id).setImage(new Image("/nought.png"));
            gameState.getBoard().set(id, 2);
        }
        gameState.nextTurn();
        checkGameOver();
    }

    private void checkGameOver() {
        int winner = gameState.gameWinner();
        if (winner == 0)
            return;
        if (winner == 1) {
            label.setText("CROSSES WON!");
        } else if (winner == 2) {
            label.setText("NULLS WON!");
        }
        buttons.forEach(button -> {button.setDisable(true);});
    }

    public void resetGame() {
        gameState.restart();
        images.forEach(imageView -> {imageView.setImage(new Image("/empty.png"));});
        buttons.forEach(button -> {button.setDisable(false);});
        label.setText("TIC-TAC-TOE");
    }
}
