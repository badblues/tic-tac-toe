package tictactoe.data;

import util.Game;
import util.transferObjects.GamePackage;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState {

    private static GameState instance;
    private ArrayList<Integer> board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
    private int turn = 0;
    private int lastTurnCellId = -1;

    private GameState() {}

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

    public void setLastTurnCellId(int cellId) {
        lastTurnCellId = cellId;
    }

    public int gameWinner() {
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
        lastTurnCellId = -1;
    }

    public void readGamePackage(GamePackage gamePackage) {
        board = gamePackage.getBoard();
        turn = gamePackage.getTurn();
    }

    public GamePackage writeGamePackage(Game game) {
        GamePackage gamePackage = new GamePackage(game.getSender(), game.getReceiver(), "GAME_TURN");
        gamePackage.setBoard(board);
        gamePackage.setTurn(turn);
        gamePackage.setLastTurnCell(lastTurnCellId);
        return gamePackage;
    }

}
