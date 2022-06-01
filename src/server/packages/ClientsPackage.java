package server.packages;

import util.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientsPackage implements Serializable {
    private int id;
    private ArrayList<Integer> clients;
    private ArrayList<Game> games;


    public ClientsPackage(ArrayList<Integer> clients, ArrayList<Game> games, int id) {
        this.clients = clients;
        this.games = games;
        this.id = id;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Integer> getClients() {
        return clients;
    }

    public int getId() {
        return id;
    }

}
