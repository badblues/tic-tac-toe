package tictactoe.data;

import javafx.util.Pair;
import util.Game;
import util.transferObjects.Leaderboard;

import java.util.ArrayList;

public class ClientData {

    private static ClientData instance;
    private static ArrayList<Pair<Integer, String>> players;
    private static ArrayList<Game> games;
    private static Leaderboard leaderboard;
    private static int id;
    private static String name;
    public final int MAX_NAME_LENGTH = 12;

    private ClientData() {}

    public static ClientData getInstance() {
        if (instance == null)
            instance = new ClientData();
        return instance;
    }

    public void setPlayers(ArrayList<Pair<Integer, String>> array) {
        players = array;
    }

    public void setGames(ArrayList<Game> array) {
        games = array;
    }

    public void setId(int i) {
        id = i;
    }

    public void setName(String str) {
        name = str;
    }

    public void setLeaderboard(Leaderboard table) {
        leaderboard = table;
    }

    public ArrayList<Pair<Integer, String>> getPlayers() {
        return players;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public int getThisClientId() {
        return id;
    }

    public String getThisClientName() {
        return name;
    }

    public int getClientId(String name) {
        for (Pair<Integer, String> player : players) {
            if (player.getValue() == name)
                return player.getKey();
        }
        return -1;
    }

    public String getClientName(int id) {
        for (Pair<Integer, String> player : players) {
            if (player.getKey() == id)
                return player.getValue();
        }
        return "";
    }

    public Game getGameFromId(int id) {
        for (Game game : games)
            if (game.hasId(id))
                return game;
        return null;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

}
