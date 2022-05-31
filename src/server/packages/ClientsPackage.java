package server.packages;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientsPackage implements Serializable {
    private int id;
    private ArrayList<Integer> clients;

    public ClientsPackage(ArrayList<Integer> array, int id) {
        clients = array;
        this.id = id;
    }
    public ArrayList<Integer> getClients() {
        return clients;
    }

    public int getId() {
        return id;
    }

    public void putClients(ArrayList<Integer> array) {
        clients = array;
    }

}
