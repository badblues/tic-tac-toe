package util;

import java.util.ArrayList;

public class Game {
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

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
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

}