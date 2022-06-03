package util.transferObjects;

import javafx.util.Pair;
import util.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientsPackage implements Serializable {

    private final int id;
    private final String name;
    private final ArrayList<Pair<Integer, String>> players;
    private final ArrayList<Game> games;


    public ClientsPackage(ArrayList<Pair<Integer, String>> players, ArrayList<Game> games, String name, int id) {
        this.players = players;
        this.games = games;
        this.name = name;
        this.id = id;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Pair<Integer, String>> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
