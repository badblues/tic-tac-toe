package tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import server.ClientController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

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
    @FXML
    Pane menuPane;


    ArrayList<Button> buttons;
    ArrayList<ImageView> images;
    Thread autoplayThread;
    long AUTOPLAY_DELAY = 500;

    ClientController clientController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button0, button1, button2, button3, button4, button5, button6, button7, button8));
        images = new ArrayList<>(Arrays.asList(image0, image1, image2, image3, image4, image5, image6, image7, image8));
        startAutoplay();
    }

    public void buttonPress(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();  //getting pressed button
        int id = Integer.parseInt(button.getId().substring(6));
        activateCell(id);
        checkGameOver();
    }

    public void activateCell(int id) {
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
    }

    private boolean checkGameOver() {
        int winner = gameState.gameWinner();
        if (winner == 0)
            return false;
        if (winner == 1) {
            label.setText("CROSSES WON!");
        } else if (winner == 2) {
            label.setText("NULLS WON!");
        }
        buttons.forEach(button -> {button.setDisable(true);});
        return true;
    }

    public void resetGame() {
        gameState.restart();
        images.forEach(imageView -> {imageView.setImage(new Image("/empty.png"));});
        buttons.forEach(button -> {button.setDisable(false);});
        label.setText("TIC-TAC-TOE");
    }

    public void startLocalGame() {
        menuPane.setVisible(false);
        resetGame();
        endAutoplay();
    }

    public void startOnline() {
        if (clientController == null)
            clientController = new ClientController(this);
    }

    public void openMenu() {
        menuPane.setVisible(true);
        resetGame();
        startAutoplay();
    }

    public void closeGame() {
        Platform.exit();
        System.exit(0);
    }

    private void startAutoplay() {
        autoplayThread = new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            Random rand = new Random();
            ArrayList<Integer> filledCells = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
            while(true) {
                long thisTime = System.currentTimeMillis();
                if ((thisTime - lastTime) >= AUTOPLAY_DELAY) {
                    Platform.runLater(() -> {
                        if (checkGameOver()) {
                            resetGame();
                            for (int i = 0; i < 9; i++)  filledCells.set(i, 0);
                        }
                    });
                    lastTime = thisTime;
                    int i;
                    do {
                        i = Math.abs(rand.nextInt(9));
                    } while(filledCells.get(i) == 1);
                    filledCells.set(i, 1);
                    activateCell(i);
                }
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
            }
        });
        autoplayThread.start();
    }

    private void endAutoplay() {
        if (autoplayThread != null) {
            autoplayThread.interrupt();
            autoplayThread = null;
        }
    }

}
