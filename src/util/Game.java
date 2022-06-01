package util;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int id1;
    private int id2;
    private ArrayList<Integer> spectators = new ArrayList<>();

    public Game(int x, int y)
    {
        this.id1 = x;
        this.id2 = y;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public void setSpectators(ArrayList<Integer> spectators) {
        this.spectators = spectators;
    }

    public ArrayList<Integer> getSpectators() {
        return spectators;
    }

    public boolean hasId(int id) {
        return (id == id1 | id == id2);
    }

    @Override
    public String toString() {
        return id1 + " " + id2;
    }

    public String getString() {
        return id1 + " vs " + id2;
    }

}