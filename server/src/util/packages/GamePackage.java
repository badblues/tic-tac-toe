package util.packages;

import util.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class GamePackage implements Serializable {

    Game game;
    String message;
    ArrayList<Integer> board = new ArrayList<>();
    int lastTurnCell = -1;
    int turn = 0;

    public GamePackage(int sender, int reciever, String message) {
        game = new Game(sender, reciever);
        this.message = message;
    }

    public void setBoard(ArrayList<Integer> array) {
        board = array;
    }

    public ArrayList<Integer> getBoard() {
        return board;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public void setLastTurnCell(int cell) {
        lastTurnCell = cell;
    }

    public int getLastTurnCell() {
        return lastTurnCell;
    }

    public int getReceiver() {
        return game.getReceiver();
    }

    public int getSender() {
        return game.getSender();
    }

    public void changeReceiver() {
        game.changeReceiver();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
