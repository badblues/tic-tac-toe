package util.packages;

import java.io.Serializable;
import java.util.ArrayList;

public class Leaderboard implements Serializable {

    int rowsNumber = 0;
    ArrayList<String> playersColumn = new ArrayList<>();
    ArrayList<Integer> gamesColumn = new ArrayList<>();
    ArrayList<Integer> winsColumn = new ArrayList<>();
    ArrayList<Integer> losesColumn = new ArrayList<>();
    ArrayList<Float> winrateColumn = new ArrayList<>();

    public void addPlayerRow(String name) {
        playersColumn.add(name);
    }

    public void addGamesRow(Integer games) {
        gamesColumn.add(games);
    }

    public void addWinsRow(Integer wins) {
        winsColumn.add(wins);
    }

    public void addLosesRow(Integer loses) {
        losesColumn.add(loses);
    }

    public void addWinrateRow(Float winrate) {
        winrateColumn.add(winrate);
    }

    public String getString(int id) {
        return "| " + playersColumn.get(id) + " |   " + gamesColumn.get(id) + "   |   "
                + winsColumn.get(id) + "   |  " + losesColumn.get(id) + "   |  "
                + winrateColumn.get(id) + "  |";
    }

    public void setRowsNumber(int n) {
        rowsNumber = n;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }
}
