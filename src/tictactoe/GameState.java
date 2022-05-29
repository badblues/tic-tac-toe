package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState {

    static GameState instance;
    ArrayList<Integer> board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
    int turn = 0;

    public GameState() {
    }

    public static GameState getInstance() {
        if (instance == null)
            instance = new GameState();
        return instance;
    }

    public ArrayList<Integer> getBoard() {
        return board;
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turn++;
    }

    public int gameWinner() {
        //returns 0 if none, 3 if draw
        int res = 0;
        for (int i = 0; i < 8; i++) {
            ArrayList<Integer> comb = new ArrayList<>();
            switch (i) {
                case 0 -> {comb.add(board.get(0)); comb.add(board.get(1)); comb.add(board.get(2));}
                case 1 -> {comb.add(board.get(3)); comb.add(board.get(4)); comb.add(board.get(5));}
                case 2 -> {comb.add(board.get(6)); comb.add(board.get(7)); comb.add(board.get(8));}
                case 3 -> {comb.add(board.get(0)); comb.add(board.get(3)); comb.add(board.get(6));}
                case 4 -> {comb.add(board.get(1)); comb.add(board.get(4)); comb.add(board.get(7));}
                case 5 -> {comb.add(board.get(2)); comb.add(board.get(5)); comb.add(board.get(8));}
                case 6 -> {comb.add(board.get(0)); comb.add(board.get(4)); comb.add(board.get(8));}
                case 7 -> {comb.add(board.get(2)); comb.add(board.get(4)); comb.add(board.get(6));}
            }
            if (comb.equals(Arrays.asList(1, 1, 1))) {
                res = 1;
                break;
            } else if (comb.equals(Arrays.asList(2, 2, 2))) {
                res = 2;
                break;
            }
        }
        if (res == 0 && !board.contains(0))
            res = 3;
        return res;
    }

    public void restart() {
        for (int i = 0; i < 9; i++)  board.set(i, 0);
        turn = 0;
    }

}
