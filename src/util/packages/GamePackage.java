package util.packages;

import java.io.Serializable;
import java.util.ArrayList;

public class GamePackage implements Serializable {

    int receiver;
    int sender;
    String message;
    ArrayList<Integer> board = new ArrayList<>();
    int lastTurnCell = -1;
    int turn = 0;

    public GamePackage(int sender, int reciever, String message) {
        this.sender = sender;
        this.receiver = reciever;
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
        return receiver;
    }

    public int getSender() {
        return sender;
    }

    public void changeReciever() {
        int tmp = receiver;
        receiver = sender;
        sender = tmp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
